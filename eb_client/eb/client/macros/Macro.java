package eb.client.macros;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import eb.client.GhostKeyHandler;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Macro extends Observable implements Runnable {
	//how often to send an instruction in milliseconds
	private final static int PLAYBACK_SPEED = 25;
	
	private String name, description;
	private LinkedList<IInstruction> instructions;
	private Iterator iterator;
	private boolean playing, synced;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
		name = "Unnamed";
		description = "No Description";
		instructions = new LinkedList<IInstruction>();
		iterator = null;
		playing = false;
		synced = true;
		scheduler = Executors.newScheduledThreadPool(1);
	}
	
	public void addInstruction(IInstruction instruction) {
		instructions.add(instruction);
	}
	
	public void play() {
		if(instructions.isEmpty()) {
			return;
		}
		
		playing = true;
		iterator = instructions.iterator();
		GhostKeyHandler.setControl(false);
		
		run();
	}
	
	public void stop() {
		playing = false;
		iterator = null;
		setSynced(true);
		GhostKeyHandler.setControl(true);
	}	

	@Override
	public void run() {
		if(playing && iterator != null && iterator.hasNext()) {
			if(synced) {
				IInstruction current = (IInstruction)iterator.next();
				current.execute();
				setSynced(!current.shouldSync());
			}
			
			scheduler.schedule(this, PLAYBACK_SPEED, TimeUnit.MILLISECONDS);
		} else {
			stop();
		}
	}
	
	public LinkedList<IInstruction> getInstructions() {
		return instructions;
	}
	
	public IInstruction getLastInstruction() {
		if(instructions.size() > 0) {
			 return instructions.getLast();
		} else {
			return null;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	public ArrayList<ItemStack> getBlockUsage() {
		ArrayList<ItemStack> usage = new ArrayList<ItemStack>();
		
		for(IInstruction instruction : instructions) {
			if(instruction instanceof PlaceInstruction) {
				PlaceInstruction placeInstruction = (PlaceInstruction)instruction;
				int id = placeInstruction.getItemID();
				int metadata = placeInstruction.getMetadata();
				boolean found = false;
				
				for(int i = 0; i < usage.size(); ++i) {	
					ItemStack stack = usage.get(i);
					
					if(stack.itemID == id && stack.getItemDamage() == metadata) {
						++stack.stackSize;
						found = true;
						break;
					}
				}
				
				if(!found) {
					if(Item.itemsList[id] != null) {
						usage.add(new ItemStack(Item.itemsList[id], 1, metadata));
					}
				}
			}
		}
		
		return usage;
	}

	public boolean isPlaying() {
		return playing;
	}
	
	public void setSynced(boolean synced) {
		this.synced = synced;
	}
	
	public void optimize() {
		int index = 0;
		int moveSequenceStart = 0;
		
		while(moveSequenceStart != -1) {
			index = moveSequenceStart;
			
			if(index < 0) {
				index = 0;
			} else if(index >= instructions.size()) {
				return;
			}
	
			for(int i = index; i < instructions.size() - 1; ++i) {
				MoveInstruction current = getMoveInstructionAt(i);
				MoveInstruction next = getMoveInstructionAt(i + 1);
				MoveInstruction peek = getMoveInstructionAt(i + 2);
	
				if(current != null && next != null) {
					if(moveSequenceStart == -1) {
						moveSequenceStart = i;
					}
					
					if(next.getDirection().isOpposite(current.getDirection())) {
						instructions.remove(i + 1);
						instructions.remove(i);
						break;
					} else if(peek != null) {
						if(peek.getDirection().isOpposite(current.getDirection())) {
							instructions.remove(i + 2);
							instructions.remove(i);
							break;
						}
					}
				} else {
					moveSequenceStart = -1;
				}
				
				if(i + 1 == instructions.size() - 1) {
					moveSequenceStart = -1;
				}
			}
			
			notifyProgress(moveSequenceStart, instructions.size());
		}
	}
	
	public double getRuntime() {
		double milliseconds = instructions.size() * PLAYBACK_SPEED;
		
		return (milliseconds / 1000.0);
	}
	
	public void translateRawData(short width, short length, short height, byte[] blocks, byte[] meta) {
		boolean leftToRight = true;
		
		int progress = 0;
		final int maxProgress = width * length * height;
		
		for(int y = 0; y < height; ++y) {
			for(int z = 0; z < length; ++z) {
				for(int x =  (leftToRight ? 0 : width - 1);
						x != (leftToRight ? width : -1);
						x += (leftToRight ? 1 : -1)) {
					
					int index = y * width * length + z * width + x;
					
					if(blocks[index] != 0) {
						addInstruction(new PlaceInstruction(blocks[index], meta[index]));
					}
					
					if(leftToRight && x != width - 1) {
						addInstruction(new MoveInstruction(Direction.RIGHT));
					} else if(!leftToRight && x != 0) {
						addInstruction(new MoveInstruction(Direction.LEFT));
					}
					
					notifyProgress(++progress, maxProgress);
				}
				
				leftToRight = !leftToRight;
				addInstruction(new MoveInstruction(Direction.FORWARD));
				
				notifyProgress(++progress, maxProgress);
			}
			
			addInstruction(new MoveInstruction(Direction.UP));
			
			for(int i = 0; i < length; ++i) {
				addInstruction(new MoveInstruction(Direction.BACKWARD));
			}
			
			notifyProgress(++progress, maxProgress);
		}
	}
	
	private MoveInstruction getMoveInstructionAt(int index) {
		if(index < 0 || index >= instructions.size()) {
			return null;
		}
		
		IInstruction instruction = instructions.get(index);
		if(instruction instanceof MoveInstruction) {
			return (MoveInstruction)instruction;
		}
		
		return null;
	}
	
	private void notifyProgress(int progress, int maxProgress) {
		setChanged();
		notifyObservers((float)progress / (float)maxProgress);
	}
}
