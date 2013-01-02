package eb.mode;

import eb.core.EBHelper;
import eb.core.mode.GhostMode;
import eb.macro.instruction.UseInstruction;
import eb.network.packet.PacketRemoveBlock;


public class RemoveMode extends GhostMode {
	@Override
	public void use() {
		if(isGhostPlaced()) {
			EBHelper.sendToServer(new PacketRemoveBlock(x, y, z));
		}
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}
	
	public String toString() {
		return "Remove";
	}
}
