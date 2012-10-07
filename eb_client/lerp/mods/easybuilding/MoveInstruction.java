package lerp.mods.easybuilding;

import cpw.mods.fml.client.FMLClientHandler;

public class MoveInstruction implements Instruction {
	private PacketEB packet;

	public MoveInstruction(Direction dir) {
		packet = new PacketMoveGhost(dir);
	}
	
	public void setDirection(Direction dir) {
		packet = new PacketMoveGhost(dir);
	}
	
	@Override
	public void execute() {
		FMLClientHandler.instance().sendPacket(packet.toCustomPayload());
	}
}
