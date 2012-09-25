package lerp.mods.easybuilding;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import net.minecraft.src.Block;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player player) {
		PacketEB packetEB = PacketType.createPacket(packet.data);
		
		if(packetEB != null) {
			packetEB.handle(manager, player);
		}
	}
	
	/*private void placeGhost(World world, int x, int y, int z) {
		TileGhostBlock entity = new TileGhostBlock();
		
		if(world.getBlockId(x, y, z) != 0) {
			entity.setBlockId(world.getBlockId(x, y, z));
		}
		
		world.setBlock(x, y, z, 180);
		world.setBlockTileEntity(x, y, z, entity);
	}
	
	private void removeGhost(World world, int x, int y, int z) {		
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
	
	private void placeBlock(EntityPlayer player, int x, int y, int z) {		
		ItemStack stack = player.inventory.getCurrentItem();
		if(stack == null) {
			return;
		}
		
		Item item = stack.getItem();
		if(item == null || !(item instanceof ItemBlock)) {
			return;
		}
		
		ItemBlock itemBlock = (ItemBlock)item;
		itemBlock.placeBlockAt(stack, player, player.worldObj, x, y, z, -1, x, y, z);
		--stack.stackSize;
		
		placeGhost(player.worldObj, x, y, z);
	}*/
}
