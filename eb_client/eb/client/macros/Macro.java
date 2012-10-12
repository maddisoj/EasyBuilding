package eb.client.macros;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Macro implements Runnable {
	private LinkedList<IInstruction> instructions;
	private Iterator iterator;
	private boolean playing;
	private ScheduledExecutorService scheduler;
	
	public Macro() {
		instructions = new LinkedList<IInstruction>();
		iterator = null;
		playing = false;
		scheduler = Executors.newScheduledThreadPool(1);		
	}
	
	public void addInstruction(IInstruction instruction) {
		instructions.add(instruction);
	}
	
	@Override
	public void run() {
		if(!playing) {
			playing = true;
			iterator = instructions.iterator();
		}
		
		if(iterator != null) {
			((IInstruction)iterator.next()).execute();
			
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
	
	public IInstruction getLastInstruction() {
		if(instructions.size() > 0) {
			 return instructions.getLast();
		} else {
			return null;
		}
	}
	
	public boolean save(String path) {
		return false;
	}
	
	@Override
	public String toString() {
		String macro = "";
		
		for(IInstruction i : instructions) {
			macro += i.toString() + "\n";
		}
		
		return macro;
	}
}
