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
import eb.client.Direction;
import eb.client.GhostKeyHandler;
import eb.client.macros.instructions.IInstruction;
import eb.client.macros.instructions.MoveInstruction;
import eb.client.macros.instructions.UseInstruction;
import eb.client.mode.BuildMode;
import eb.client.mode.GhostMode;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Macro extends Observable implements Runnable {
	//how often to send an instruction in milliseconds
	private final static int PLAYBACK_SPEED = 25;
	
	private Class<? extends GhostMode> requiredMode;
	private String name, description;
	private LinkedList<IInstruction> instructions;
	private Iterator iterator;
	private boolean playing;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
		this(null);
	}
	
	public Macro(Class<? extends GhostMode> mode) {
		requiredMode = mode;
		name = "Unnamed";
		description = "No Description";
		instructions = new LinkedList<IInstruction>();
		iterator = null;
		playing = false;
		scheduler = Executors.newScheduledThreadPool(1);
	}
	
	public Class<? extends GhostMode> getRequiredMode() {
		return requiredMode;
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
		GhostKeyHandler.setControl(true);
	}

	@Override
	public void run() {
		if(playing && iterator != null && iterator.hasNext()) {
			IInstruction current = (IInstruction)iterator.next();
			current.execute();
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

	public boolean isPlaying() {
		return playing;
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
