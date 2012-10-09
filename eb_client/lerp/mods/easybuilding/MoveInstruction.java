package lerp.mods.easybuilding;

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
}
