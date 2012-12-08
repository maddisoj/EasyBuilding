package eb.common;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import eb.client.TileGhostBlock;
import eb.common.network.PacketEB;

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
	
	@SideOnly(Side.CLIENT)
	public static TileGhostBlock getGhostBlock(World world, int x, int y, int z) {
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			return null;
		}
		
		return (TileGhostBlock)entity;
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
}
