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

public class GhostBlockHandler {
	private static final GhostBlockHandler INSTANCE = new GhostBlockHandler();
	
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
	
	private GhostBlockHandler() {
		ghostBlocks = new HashMap<String, GhostBlockInformation>();
	}
	
	public static GhostBlockHandler instance() {
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
		
		if(info == null) {
			return;
		}
		
		placeBlock(player, info.x, info.y, info.z);
	}
	
	private void placeBlock(EntityPlayer player, int x, int y, int z) {
		World world = player.worldObj;
		ItemStack stack = player.inventory.getCurrentItem();
		
		if(stack == null) { return; }
		
		Item i = stack.getItem();
		if(!(i instanceof ItemBlock)){
			return;
		}
		
		if(world.getBlockId(x, y, z) == 180) {
			TileGhostBlock entity = (TileGhostBlock)world.getBlockTileEntity(x, y, z);
			if(entity.getBlockId() != 0) {
				return;
			}
		} else {
			return;
		}
		
		ItemBlock item = (ItemBlock)i;
		item.placeBlockAt(stack, player, world, x, y, z, -1, x, y, z);
		--stack.stackSize;
		
		place(player, x, y, z);
	}

	public void requestMove(EntityPlayer player, Direction direction) {
		GhostBlockInformation info = ghostBlocks.get(player.username);
		
		if(info == null) {
			return;
		} else if(!info.placed) {
			return;
		} else {
			move(player, info.x, info.y, info.z, direction);
		}
	}
	
	private void move(EntityPlayer player, int x, int y, int z, Direction direction) {
		Vec3 moveDirection = Vec3.createVectorHelper(0.0, 0.0, 0.0);
		
		if(direction == Direction.FORWARD) {
			moveDirection = Helper.getPlayerDirection(player);
		} else if(direction == Direction.BACKWARD) {
			moveDirection = Helper.getPlayerDirection(player);
			moveDirection.rotateAroundY((float)Math.PI);
		} else if(direction == Direction.LEFT) {
			moveDirection = Helper.getPlayerDirection(player);
			moveDirection.rotateAroundY((float)Math.PI/2);
		} else if(direction == Direction.RIGHT) {
			moveDirection = Helper.getPlayerDirection(player);
			moveDirection.rotateAroundY((float)-Math.PI/2);
		} else if(direction == Direction.UP) {
			moveDirection = Vec3.createVectorHelper(0.0, 1.0, 0.0);
		} else if(direction == Direction.DOWN) {
			moveDirection = Vec3.createVectorHelper(0.0, -1.0, 0.0);
		}
		
		int targetX = x + (int)moveDirection.xCoord;
		int targetY = y + (int)moveDirection.yCoord;
		int targetZ = z + (int)moveDirection.zCoord;
		
		remove(player, x, y, z);
		place(player, targetX, targetY, targetZ);
		updateGhostBlock(player, targetX, targetY, targetZ);
	}
}
