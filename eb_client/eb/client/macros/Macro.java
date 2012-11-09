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
	
	public HashMap<Item, Integer> getBlockUsage() {
		HashMap<Item, Integer> usage = new HashMap<Item, Integer>();
		
		for(IInstruction instruction : instructions) {
			if(instruction instanceof PlaceInstruction) {
				int id = ((PlaceInstruction)instruction).getItemID();
				Item item = Item.itemsList[id];
				
				Integer count = usage.get(item);
				if(count == null) {
					usage.put(item, 1);
				} else {
					usage.put(item, count + 1);
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
}
