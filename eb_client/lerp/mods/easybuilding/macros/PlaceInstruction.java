package lerp.mods.easybuilding.macros;

import lerp.mods.easybuilding.GhostBlockHandler;
import lerp.mods.easybuilding.network.PacketPlaceBlock;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.InventoryPlayer;

public class PlaceInstruction implements Instruction {
	private int slot;
	
	public PlaceInstruction(int slot) {
		this.slot = slot;
	}
	
	@Override
	public void execute() {
		FMLClientHandler.instance().getClient().thePlayer.inventory.currentItem = slot;
		GhostBlockHandler.instance().placeBlock();
	}
	
	@Override
	public String toString() {
		return "Place " + slot;
	}
}
