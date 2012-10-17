package eb.client.macros;

import cpw.mods.fml.client.FMLClientHandler;
import eb.client.GhostBlockHandler;
import eb.common.Direction;

public class MoveInstruction extends Instruction {
	private Direction dir;
	
	public MoveInstruction(Direction dir) {
		this.dir = dir;
	}
	
	@Override
	public void execute() {
		GhostBlockHandler.instance().move(dir);
	}

	@Override
	public String getParameters() {
		return dir.name();
	}

	@Override
	public boolean parseParameters(String[] parameters) {
		try {
			//int ordinal = Integer.parseInt(parameters[0]);
			//dir = Direction.values()[ordinal];
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
}
