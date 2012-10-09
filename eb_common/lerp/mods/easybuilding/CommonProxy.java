package lerp.mods.easybuilding;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.World;

public class CommonProxy {
	public void registerKeyBindings() {}
	public void registerRenderInformation() {}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileGhostBlock.class, "tileGhostBlock");
	}
	
	public World getClientWorld() {
		return null;
	}
	
	public EntityClientPlayerMP getPlayer() {
		return null;
	}
	
	public void sendToPlayer(EntityPlayer player, PacketEB packet) {
		EntityPlayerMP playerMP = (EntityPlayerMP)player;
		playerMP.serverForThisPlayer.sendPacketToPlayer(packet.toCustomPayload());
	}
	
	public void sendToAllPlayers(PacketEB packet) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		
	    if(server != null) {
	      
	    }
	}
}
