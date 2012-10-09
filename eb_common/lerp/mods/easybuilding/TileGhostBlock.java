package lerp.mods.easybuilding;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class TileGhostBlock extends TileEntity {
	private int blockId;
	private String owner;
	
	public TileGhostBlock() {
		blockId = 0;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public void handleUpdatePacket(PacketUpdateGhost packet) {
		if(worldObj.isRemote) {
			blockId = packet.getBlockId();
		}
	}
	
	public PacketUpdateGhost getUpdatePacket() {
		return new PacketUpdateGhost(xCoord, yCoord, zCoord, blockId);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
	}

	public void move(EntityPlayer player, Direction direction) {
		Vec3 moveDirection = Vec3.createVectorHelper(0.0, 0.0, 0.0);
		
		//if(true) {//owner != player.username) {	
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
			
			System.out.println("moving " + direction.toString());
		//}
		
		int oldX = xCoord;
		int oldY = yCoord;
		int oldZ = zCoord;
		int newX = xCoord + (int)moveDirection.xCoord;
		int newY = yCoord + (int)moveDirection.yCoord;
		int newZ = zCoord + (int)moveDirection.zCoord;
		
		int newBlockID = worldObj.getBlockId(newX, newY, newZ);
		worldObj.setBlock(newX, newY, newZ, BlockIDs.ghostBlockID);
		
		TileEntity entity = worldObj.getBlockTileEntity(newX, newY, newZ);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock ghost = (TileGhostBlock)entity;
		ghost.setBlockId(newBlockID);
		
		worldObj.setBlock(oldX, oldY, oldZ, blockId);
	}
}
