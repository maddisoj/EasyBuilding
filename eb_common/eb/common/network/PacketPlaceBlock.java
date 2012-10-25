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
import eb.client.TileGhostBlock;
import eb.common.Constants;
import eb.common.EasyBuilding;
import eb.common.Helper;

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

			stack.tryPlaceItemIntoWorld(entityPlayer, world, x, y - 1, z, 1, x, y, z);

			int blockID = world.getBlockId(x, y, z);
			int metadata = world.getBlockMetadata(x, y, z);
			
			if(blockID != 0) {
				EasyBuilding.sendToPlayer(player, new PacketUpdateGhost(blockID, metadata));
			}
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
