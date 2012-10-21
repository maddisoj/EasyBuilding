package eb.common;

import eb.common.network.PacketUpdateGhost;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemRedstone;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class TileGhostBlock extends TileEntity {
	private int blockID;
	private int metadata;

	public TileGhostBlock() {
		blockID = 0;
	}

	public int getBlockId() {
		return blockID;
	}

	public void setBlockId(int blockId) {
		this.blockID = blockId;
	}
	
	public int getBlockMetadata() {
		return metadata;
	}
	
	public void setBlockMetadata(int metadata) {
		this.metadata = metadata;
	}

	public PacketUpdateGhost getUpdatePacket() {
		return new PacketUpdateGhost(xCoord, yCoord, zCoord, blockID);
	}

	@Override
	public Packet getAuxillaryInfoPacket() {
		return getUpdatePacket().toCustomPayload();
	}

	public void move(EntityPlayer player, Direction direction) {
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

		int oldX = xCoord;
		int oldY = yCoord;
		int oldZ = zCoord;
		int newX = xCoord + (int)moveDirection.xCoord;
		int newY = yCoord + (int)moveDirection.yCoord;
		int newZ = zCoord + (int)moveDirection.zCoord;

		int newBlockID = worldObj.getBlockId(newX, newY, newZ);
		int newMetadata = worldObj.getBlockMetadata(newX, newY, newZ);	
		worldObj.setBlock(newX, newY, newZ, Constants.GHOST_BLOCK_ID);

		TileEntity entity = worldObj.getBlockTileEntity(newX, newY, newZ);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}

		TileGhostBlock newGhostTile = (TileGhostBlock)entity;
		newGhostTile.setBlockId(newBlockID);
		newGhostTile.setBlockMetadata(newMetadata);

		worldObj.setBlockAndMetadata(oldX, oldY, oldZ, blockID, metadata);
	}

	public void place(EntityPlayer player, int itemID) {
		if(blockID != 0) {
			return;
		}

		if(player.inventory.hasItem(itemID)) {
			Item item = Item.itemsList[itemID];
			if(!(item instanceof ItemBlock)) {
				return;
			}
			
			blockID = ((ItemBlock)item).getBlockID();			
			player.inventory.consumeInventoryItem(itemID);
			EasyBuilding.sendToAllPlayers(getUpdatePacket());
		}
		
		/*ItemStack stack = searchInventory(player.inventory, itemID);
		if(stack == null) {
			return;
		}
		
		Item item = stack.getItem();*/
	}

	public void remove() {
		worldObj.setBlock(xCoord, yCoord, zCoord, blockID);
	}
	
    public AxisAlignedBB getContainedBoundingBox() {
        if(blockID == 0) {
        	return null;
        }
        
        Block block = Block.blocksList[blockID];
        if(block != null) {
        	return block.getCollisionBoundingBoxFromPool(this.worldObj, xCoord, yCoord, zCoord);
        }
        
        return null;
    }
	
	private ItemStack searchInventory(InventoryPlayer inventory, int itemID) {
		for(ItemStack itemStack : inventory.mainInventory) {
			if (itemStack != null && itemStack.itemID == itemID) {
				return itemStack;
			}
		}
	
		return null;
	}
	
	/*private int getPlacedBlockID(Item item) {
		if(item instanceof ItemBlock) {
			return ((ItemBlock)item).getBlockID();
		} else if(item instanceof ItemRedstone) {
			return 
		}
		
		return -1;
	}*/
}
