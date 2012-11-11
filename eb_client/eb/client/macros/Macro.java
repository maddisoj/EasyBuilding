package eb.client.macros;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import eb.client.GhostKeyHandler;
import eb.common.Helper;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Macro implements Runnable {
	//how often to send an instruction in milliseconds
	private final static int INSTRUCTION_TIME = 50;
	
	private String name, description;
	private LinkedList<IInstruction> instructions;
	private Iterator iterator;
	private boolean playing, locked;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
		name = "Unnamed";
		description = "No Description";
		instructions = new LinkedList<IInstruction>();
		iterator = null;
		playing = false;
		locked = false;
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
		GhostKeyHandler.setControl(true);
	}	

	@Override
	public void run() {	
		if(playing && iterator != null && iterator.hasNext()) {
			if(!locked) {
				IInstruction current = (IInstruction)iterator.next();
				current.execute();
				setLocked(current.shouldLock());
			}
			
			scheduler.schedule(this, INSTRUCTION_TIME, TimeUnit.MILLISECONDS);
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
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void optimize() {
		optimizeFrom(0);
	}
	
	private void optimizeFrom(int index) {
		if(index < 0) {
			index = 0;
		} else if(index >= instructions.size()) {
			return;
		}

		int moveSequenceStart = -1;
		for(int i = index; i < instructions.size() - 1; ++i) {
			IInstruction current = instructions.get(i);
			IInstruction next = instructions.get(i + 1);

			if(current instanceof MoveInstruction && next instanceof MoveInstruction) {
				if(moveSequenceStart == -1) {
					moveSequenceStart = i;
				}
				
				MoveInstruction currentMove = (MoveInstruction)current;
				MoveInstruction nextMove = (MoveInstruction)next;
				
				if(nextMove.getDirection().isOpposite(currentMove.getDirection())) {
					instructions.remove(i + 1);
					instructions.remove(i);
					break; //so that we can reoptimize from start of move sequence
				}
			} else {
				moveSequenceStart = -1;
			}
			
			if(i + 1 == instructions.size() - 1) {
				moveSequenceStart = -1;
			}
		}
		
		if(moveSequenceStart != -1) {
			optimizeFrom(moveSequenceStart);
		}
	}
}
