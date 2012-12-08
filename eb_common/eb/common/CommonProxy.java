package eb.common;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import eb.common.network.PacketEB;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {}
	public void init(FMLInitializationEvent event) {}
	public void registerKeyBindings() {}
	public void registerRenderInformation() {}
	public void registerTileEntities() {}
	
	public void sendToPlayer(Player player, PacketEB packet) {
		if(packet == null) { return; }
		
	    PacketDispatcher.sendPacketToPlayer(packet.toCustomPayload(), player);
	}
}
