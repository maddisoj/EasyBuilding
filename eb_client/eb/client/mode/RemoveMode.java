package eb.client.mode;

import eb.client.macros.instructions.UseInstruction;
import eb.common.EBHelper;
import eb.common.network.PacketRemoveBlock;


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
