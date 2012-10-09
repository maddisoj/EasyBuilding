package lerp.mods.easybuilding;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.InventoryPlayer;

public class PlaceInstruction implements Instruction {	
	@Override
	public void execute() {
		FMLClientHandler.instance().sendPacket((new PacketPlaceBlock()).toCustomPayload());
	}
}
