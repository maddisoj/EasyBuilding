package lerp.mods.easybuilding;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TileGhostBlock extends TileEntity {
	private int blockId;
	
	public TileGhostBlock() {
		blockId = 0;
	}

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
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
}
