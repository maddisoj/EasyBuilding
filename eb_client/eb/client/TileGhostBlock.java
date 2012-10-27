package eb.client;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
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

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class TileGhostBlock extends TileEntity {
	private int blockID;
	private int metadata;

	public TileGhostBlock() {
		blockID = 0;
		metadata = 0;
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

	public void remove() {
		worldObj.setBlockAndMetadata(xCoord, yCoord, zCoord, blockID, metadata);
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
}
