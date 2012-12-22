package eb.mode;

import eb.core.EBHelper;
import eb.core.mode.GhostMode;
import eb.macro.instruction.UseInstruction;
import eb.network.packet.PacketRemoveBlock;


public class RemoveMode extends GhostMode {
	@Override
	public UseInstruction use() {
		if(isGhostPlaced()) {
			EBHelper.sendToServer(new PacketRemoveBlock(x, y, z));
		}
		
		return null;
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}
}
