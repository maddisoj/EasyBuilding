package eb.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import eb.client.TileGhostBlock;
import eb.common.network.PacketEB;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet;
import net.minecraft.src.World;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class CommonProxy {
	public void registerKeyBindings() {}
	public void registerRenderInformation() {}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileGhostBlock.class, "tileGhostBlock");
	}
	
	public void sendToPlayer(Player player, PacketEB packet) {
		if(packet == null) { return; }
		
	    PacketDispatcher.sendPacketToPlayer(packet.toCustomPayload(), player);
	}
}
