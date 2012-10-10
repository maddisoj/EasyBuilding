package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketPlaceGhost extends PacketGhostPosition {
	public PacketPlaceGhost() {
		super(PacketType.PLACE_GHOST, false);
	}
	
	public PacketPlaceGhost(int X, int Y, int Z) {
		super(PacketType.PLACE_GHOST, false);
		x = X;
		y = Y;
		z = Z;
	}
	
	public void handle(NetworkManager manager, Player player) {
		//ToBeDeleted.instance().requestPlace((EntityPlayer)player, x, y, z);
		World world = ((EntityPlayer)player).worldObj;
		
		int blockID = world.getBlockId(x, y, z);
		world.setBlock(x, y, z, BlockIDs.ghostBlockID);
		
		TileGhostBlock ghostBlock = (TileGhostBlock)world.getBlockTileEntity(x, y, z);
		ghostBlock.setBlockId(blockID);
	}
}
