package eb.client.macros;

import cpw.mods.fml.client.FMLClientHandler;
import eb.client.GhostBlockHandler;
import eb.common.Direction;

public class MoveInstruction implements Instruction {
	private Direction dir;

	public MoveInstruction(Direction dir) {
		this.dir = dir;
	}
	
	@Override
	public void execute() {
		GhostBlockHandler.instance().move(dir);
	}
	
	@Override
	public String toString() {
		return "Move " + dir.name();
	}
}
