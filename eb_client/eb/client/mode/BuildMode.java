package eb.client.mode;

import eb.common.EBHelper;
import eb.common.network.PacketPlaceBlock;

public class BuildMode extends GhostBlockMode {
	@Override
	public void use() {
		if(isGhostPlaced()) {
			EBHelper.sendToServer(new PacketPlaceBlock(x, y, z, itemID, itemMetadata));
		}
	}
	
	@Override
	public boolean allowsMacros() {
		return true;
	}
}
