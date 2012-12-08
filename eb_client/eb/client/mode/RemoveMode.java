package eb.client.mode;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import eb.client.TileGhostBlock;
import eb.common.EBHelper;
import eb.common.network.PacketPlaceBlock;
import eb.common.network.PacketRemoveBlock;

public class RemoveMode extends GhostBlockMode {
	@Override
	public void use() {
		if(isGhostPlaced()) {
			TileGhostBlock ghost = EBHelper.getGhostBlock(EBHelper.getWorld(), x, y, z);
			
			if(ghost != null && ghost.getBlockId() != 0) {
				EBHelper.sendToServer(new PacketRemoveBlock(x, y, z));
			}
		}
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}
}
