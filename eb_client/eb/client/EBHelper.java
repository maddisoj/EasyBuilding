package eb.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import eb.common.network.PacketEB;

public class EBHelper {
	public static void sendToServer(PacketEB packet) {
		FMLClientHandler.instance().sendPacket(packet.toCustomPayload());
	}
	
	public static Minecraft getClient() {
		return FMLClientHandler.instance().getClient();
	}
	
	public static EntityClientPlayerMP getPlayer() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}
	
	public static World getWorld() {
		EntityClientPlayerMP player = getPlayer();
		
		if(player != null) {
			return getPlayer().worldObj;
		} else {
			return null;
		}
	}
}
