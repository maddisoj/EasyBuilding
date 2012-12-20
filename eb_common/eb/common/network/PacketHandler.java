package eb.common.network;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import eb.common.EBHelper;
import eb.common.EasyBuilding;
import eb.common.PermissionHandler;

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
