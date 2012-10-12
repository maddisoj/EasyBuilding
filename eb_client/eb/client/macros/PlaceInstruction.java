package eb.client.macros;

import cpw.mods.fml.client.FMLClientHandler;
import eb.client.GhostBlockHandler;
import eb.common.network.PacketPlaceBlock;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.InventoryPlayer;

public class PlaceInstruction implements Instruction {
	private int itemID;
	
	public PlaceInstruction(int itemID) {
		this.itemID = itemID;
	}
	
	@Override
	public void execute() {	
		GhostBlockHandler.instance().placeBlock(itemID);
	}
	
	@Override
	public String toString() {
		return "Place " + itemID;
	}
}
