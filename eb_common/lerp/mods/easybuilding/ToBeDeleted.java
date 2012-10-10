package lerp.mods.easybuilding;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.server.FMLServerHandler;

public class ToBeDeleted {
	private static final ToBeDeleted INSTANCE = new ToBeDeleted();
	
	private class GhostBlockInformation {
		public int x, y, z;
		public boolean placed;
		
		public GhostBlockInformation() {
			reset();
		}
		
		public GhostBlockInformation(int x, int y, int z, boolean placed) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.placed = placed;
		}
		
		public void reset() {
			x = -1;
			y = -1;
			z = -1;
			placed = false;
		}
	}
	
	private HashMap<String, GhostBlockInformation> ghostBlocks;
	
	private ToBeDeleted() {
		ghostBlocks = new HashMap<String, GhostBlockInformation>();
	}
	
	public static ToBeDeleted instance() {
		return INSTANCE;
	}
	
	public void updateGhostBlock(EntityPlayer player, int x, int y, int z) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		if(info != null) {
			info.x = x;
			info.y = y;
			info.z = z;
		} else {
			ghostBlocks.put(player.username, new GhostBlockInformation(x, y, z, true));
		}
	}
	
	public void removeGhostBlock(EntityPlayer player) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		if(info != null) {
			ghostBlocks.remove(info);
		}
	}
	
	public void requestPlace(EntityPlayer player, int x, int y, int z) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		if(info == null) {
			place(player, x, y, z);
		} else {
			remove(player, info.x, info.y, info.z);
			place(player, x, y, z);
		}
		
		/*World world = player.worldObj;
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock tileGhost = (TileGhostBlock)entity;*/
		
		updateGhostBlock(player, x, y, z);
	}
	
	private void place(EntityPlayer player, int x, int y, int z) {
		World world = player.worldObj;
		TileGhostBlock entity = new TileGhostBlock();
		entity.setBlockId(world.getBlockId(x, y, z));
		
		world.setBlock(x, y, z, 180);
		world.setBlockTileEntity(x, y, z, entity);
		EasyBuilding.proxy.sendToPlayer(player, new PacketUpdateGhost(x, y, z, entity.getBlockId()));
	}
	
	public void requestRemove(EntityPlayer player, int x, int y, int z) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		if(info == null) {
			return;
		} else if(info.x == x && info.y == y && info.z == z) {
			remove(player, x, y, z);
		}		

		removeGhostBlock(player);
	}
	
	private void remove(EntityPlayer player, int x, int y, int z) {
		World world = player.worldObj;
		if(world.getBlockId(x, y, z) != 180) {
			return;
		}
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if(!(tileEntity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock entity = (TileGhostBlock)tileEntity;
		world.setBlock(x, y, z, entity.getBlockId());
	}
	
	public void requestPlaceBlock(EntityPlayer player) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		/*if(blockID == -1) {
			Item current = player.inventory.getCurrentItem().getItem();
			
			if(!(current instanceof ItemBlock)) {
				return;
			}
			
			blockID = ((ItemBlock)current).getBlockID();
		} else {
			//if(player.inventory.hasItem())
		}*/
		
		if(info == null) {
			return;
		}
		
		placeBlock(player, info.x, info.y, info.z);
	}
	
	private void placeBlock(EntityPlayer player, int x, int y, int z) {
		World world = player.worldObj;
		
		place(player, x, y, z);
	}
}
