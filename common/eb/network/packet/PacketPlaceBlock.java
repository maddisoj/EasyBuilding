package eb.network.packet;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;
import eb.network.PacketType;

/**
 * The packet that lets the server know where the ghost block wants to put a block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class PacketPlaceBlock extends PacketGhostPosition {
	private int itemID;
	private int metadata;
	
	public PacketPlaceBlock() {
		super(PacketType.PLACE_BLOCK, true);
		itemID = -1;
		metadata = 0;
	}
	
	public PacketPlaceBlock(int x, int y, int z, int itemID, int metadata) {
		super(PacketType.PLACE_BLOCK, true);
		this.x = x;
		this.y = y;
		this.z = z;
		this.itemID = itemID;
		this.metadata = metadata;
	}
	
	@Override
	public void read(ByteArrayDataInput bis) {
		super.read(bis);
		itemID = bis.readInt();
		metadata = bis.readInt();
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		super.getData(dos);
		dos.writeInt(itemID);
		dos.writeInt(metadata);
	}
	
	@Override
	public void handle(INetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		World world = entityPlayer.worldObj;
		ItemStack stack = null;
		int slot = -1;
		
		if(entityPlayer.capabilities.isCreativeMode) {
			stack = new ItemStack(Item.itemsList[itemID]);
			stack.setItemDamage(metadata);
		} else {
			slot = searchInventory(entityPlayer.inventory, itemID, metadata);
			
			if(slot == -1 || slot > entityPlayer.inventory.getSizeInventory()) {
				return;
			}
			
			stack = entityPlayer.inventory.mainInventory[slot];
		}
		
		if(!stack.tryPlaceItemIntoWorld(entityPlayer, world, x, y - 1, z, 1, x, y, z)) {
			return;
		}

		if(stack.stackSize <= 0 && !entityPlayer.capabilities.isCreativeMode) {
			entityPlayer.inventory.mainInventory[slot] = null;
		}

		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
	}
	
	private int searchInventory(InventoryPlayer inventory, int itemID, int metadata) {
		int closestMatch = -1;
		
		for(int i = 0; i < inventory.mainInventory.length; ++i) {
			if(inventory.mainInventory[i] == null) {
				continue;
			}
			
			if(inventory.mainInventory[i].itemID == itemID) {
				if(inventory.mainInventory[i].getItemDamage() == metadata) {
					return i;
				} else {
					closestMatch = i;
				}
			}
		}
		
		return closestMatch;
	}
}
