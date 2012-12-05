package eb.client.mode;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.EntityClientPlayerMP;
import eb.client.EBHelper;
import eb.client.macros.Direction;
import eb.client.macros.UseInstruction;
import eb.common.network.PacketPlaceBlock;

public class BuildMode extends GhostBlockMode {
	@Override
	public void use() {
		if(isGhostPlaced()) {
			int id = (itemID == -1 ? getCurrentItemID() : itemID);
			int metadata = (itemID == -1 ? getCurrentItemMetadata() : itemMetadata);
			
			EBHelper.sendToServer(new PacketPlaceBlock(x, y, z, id, metadata));
		}
	}
	
	@Override
	public boolean allowsMacros() {
		return true;
	}

	private int getCurrentItemID() {
		EntityClientPlayerMP player = EBHelper.getPlayer();
		
		if(player != null) {
			if(player.inventory.getCurrentItem() != null) {
				return player.inventory.getCurrentItem().itemID;
			}
		}
		
		return -1;
	}
	
	private int getCurrentItemMetadata() {
		EntityClientPlayerMP player = EBHelper.getPlayer();
		
		if(player != null) {
			if(player.inventory.getCurrentItem() != null) {
				return player.inventory.getCurrentItem().getItemDamage();
			}
		}
		
		return 0;
	}
}
