package eb.common.network;

import java.io.DataOutputStream;
import java.io.IOException;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;
import eb.common.Constants;
import eb.common.EasyBuilding;
import eb.common.Helper;
import eb.common.TileGhostBlock;

public class PacketPlaceBlock extends PacketGhostPosition {
	private int itemID;
	
	public PacketPlaceBlock() {
		super(PacketType.PLACE_BLOCK, true);
		itemID = -1;
	}
	
	public PacketPlaceBlock(int X, int Y, int Z, int itemID) {
		super(PacketType.PLACE_BLOCK, true);
		x = X;
		y = Y;
		z = Z;
		this.itemID = itemID;
	}
	
	@Override
	public void read(ByteArrayDataInput bis) {
		super.read(bis);
		itemID = bis.readInt();
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		super.getData(dos);
		dos.writeInt(itemID);
	}	
	
	public void handle(NetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		
		if(entityPlayer.inventory.hasItem(itemID)) {
			World world = entityPlayer.worldObj;
			ItemStack stack = searchInventory(entityPlayer.inventory, itemID);
			
			if(stack.stackSize == 0 && entityPlayer.capabilities.isCreativeMode) {
				stack.stackSize = 1;
			}

			//world.setBlockAndMetadata(xCoord, yCoord, zCoord, blockID, metadata);
			stack.tryPlaceItemIntoWorld(entityPlayer, world, x, y - 1, z, 1, x, y, z);

			int blockID = world.getBlockId(x, y, z);
			int metadata = world.getBlockMetadata(x, y, z);
			
			EasyBuilding.sendToPlayer(player, new PacketUpdateGhost(x, y, z, blockID, metadata));
			
			/*world.setBlock(xCoord, yCoord, zCoord, Constants.GHOST_BLOCK_ID);
			TileGhostBlock ghostBlock = Helper.getGhostBlock(world, xCoord, yCoord, zCoord);
			
			if(ghostBlock != null) {
				ghostBlock.setBlockId(blockID);
				ghostBlock.setBlockMetadata(metadata);
			}*/
		}
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
