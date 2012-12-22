package eb.network;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import eb.core.handlers.PermissionHandler;
import eb.network.packet.PacketEB;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class PacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		PacketEB packetEB = PacketType.createPacket(packet.data);
		
		if(packetEB != null) {
			EntityPlayer entityPlayer = (EntityPlayer)player;
			
			if(!PermissionHandler.instance().hasPermission(entityPlayer.username)) {
				return;
			}
			
			packetEB.handle(manager, player);
		}
	}
}
