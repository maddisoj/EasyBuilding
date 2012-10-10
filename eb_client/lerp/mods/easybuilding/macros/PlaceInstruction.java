package lerp.mods.easybuilding.macros;

import lerp.mods.easybuilding.GhostBlockHandler;
import lerp.mods.easybuilding.network.PacketPlaceBlock;
import cpw.mods.fml.client.FMLClientHandler;
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
