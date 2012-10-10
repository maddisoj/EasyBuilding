package lerp.mods.easybuilding.macros;

import lerp.mods.easybuilding.Direction;
import lerp.mods.easybuilding.GhostBlockHandler;
import cpw.mods.fml.client.FMLClientHandler;

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
