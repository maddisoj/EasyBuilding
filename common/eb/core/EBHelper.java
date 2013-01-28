package eb.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import eb.network.packet.PacketEB;

/**
 * A bunch of helper methods
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */
public class EBHelper {
	public static int[] getPosition(int x, int y, int z, int side) {
		if(side == 0) { //bottom
			y--;
		} else if(side == 1) { //top
			y++;
		} else if(side == 2) { //east
			z--;
		} else if(side == 3) { //west
			z++;
		} else if(side == 4) { //north
			x--;
		} else if(side == 5) { //south
			x++;
		}
		
		return new int[]{x, y, z};
	}
	
	public static Vec3 getPlayerDirection() {
		return getPlayerDirection(getPlayer());
	}
	
	public static Vec3 getPlayerDirection(EntityPlayer player) {
		Vec3 look = player.getLookVec();
		Vec3 absLook = Vec3.createVectorHelper(Math.abs(look.xCoord), Math.abs(look.yCoord), Math.abs(look.zCoord));
		Vec3 playerDirection = Vec3.createVectorHelper(0.0, 0.0, 0.0);
		
		if(absLook.xCoord >= absLook.zCoord) {
			playerDirection.xCoord = 1.0 * (look.xCoord >= 0.0 ? 1.0 : -1.0);
		} else {
			playerDirection.zCoord = 1.0 * (look.zCoord >= 0.0 ? 1.0 : -1.0);
		}
		
		return playerDirection;
	}

	public static void sendToServer(PacketEB packet) {
		if(packet != null) {
			PacketDispatcher.sendPacketToServer(packet.toCustomPayload());
		}
	}
	
	public static void sendToPlayer(Player player, PacketEB packet) {
		if(packet != null) {
			 PacketDispatcher.sendPacketToPlayer(packet.toCustomPayload(), player);
		}
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
	
	public static ItemStack getCurrentItem() {
		EntityClientPlayerMP player = EBHelper.getPlayer();
		
		if(player != null) {
			return player.inventory.getCurrentItem();
		}
		
		return null;
	}
	
	public static void printMessage(String message) {
		if(getPlayer() != null) {
			getPlayer().addChatMessage(message);
		}
	}
	
	public static double angleBetweenAroundY(Vec3 a, Vec3 b) {
		return Math.atan2(a.xCoord, a.zCoord) - Math.atan2(b.xCoord, b.zCoord);
	}
}
