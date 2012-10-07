package lerp.mods.easybuilding;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.InventoryPlayer;

public class PlaceInstruction implements Instruction {
	private int blockID;
	
	public PlaceInstruction(int blockID) {
		this.blockID = blockID;
	}
	
	public void setBlockID(int blockID) {
		this.blockID = blockID;
	}
	
	@Override
	public void execute() {
		EntityClientPlayerMP player = EasyBuilding.proxy.getPlayer();
		InventoryPlayer inv = player.inventory;
		
		if(inv.hasItem(blockID)) {
			//player.inventorySlots;
		}
	}
}
