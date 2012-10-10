package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketRemoveGhost extends PacketGhostPosition {
	public PacketRemoveGhost() {
		super(PacketType.REMOVE_GHOST, false);
	}
	
	public PacketRemoveGhost(int X, int Y, int Z) {
		super(PacketType.REMOVE_GHOST, false);
		x = X;
		y = Y;
		z = Z;
	}
	
	public void handle(NetworkManager manager, Player player) {
		World world = ((EntityPlayer)player).worldObj;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock ghostBlock = (TileGhostBlock)entity;
		ghostBlock.remove();
	}
}
