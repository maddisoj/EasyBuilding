package lerp.mods.easybuilding.macros;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Macro implements Runnable {
	private LinkedList<Instruction> instructions;
	private Iterator iterator;
	private boolean playing;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
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
				scheduler.schedule(this, 500, TimeUnit.MILLISECONDS);
			} else {
				playing = false;
			}
		}
	}
	
	public void stopPlaying() {
		playing = false;
	}
	
	public Instruction getLastInstruction() {
		if(instructions.size() > 0) {
			 return instructions.getLast();
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		String macro = "";
		
		for(Instruction i : instructions) {
			macro += i.toString() + "\n";
		}
		
		return macro;
	}
}
