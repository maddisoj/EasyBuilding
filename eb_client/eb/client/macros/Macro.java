package eb.client.macros;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Macro implements Runnable {
	private String name, description;
	private LinkedList<Instruction> instructions;
	private Iterator iterator;
	private boolean playing;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
		name = "Unnamed";
		description = "No Description";
		instructions = new LinkedList<Instruction>();
		iterator = null;
		playing = false;
		scheduler = Executors.newScheduledThreadPool(1);		
	}
	
	public void addInstruction(Instruction instruction) {
		instructions.add(instruction);
	}
	
	@Override
	public void run() {
		if(!playing) {
			playing = true;
			iterator = instructions.iterator();
		}
		
		if(iterator != null) {
			((Instruction)iterator.next()).execute();
			
			if(iterator.hasNext()) {
				scheduler.schedule(this, 250, TimeUnit.MILLISECONDS);
			} else {
				playing = false;
			}
		}
	}
	
	public void stopPlaying() {
		playing = false;
	}
	
	public LinkedList<Instruction> getInstructions() {
		return instructions;
	}
	
	public Instruction getLastInstruction() {
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
}
