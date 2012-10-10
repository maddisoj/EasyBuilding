package lerp.mods.easybuilding.network;

import java.io.DataOutputStream;
import java.io.IOException;

import lerp.mods.easybuilding.TileGhostBlock;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketPlaceBlock extends PacketGhostPosition {	
	public PacketPlaceBlock() {
		super(PacketType.PLACE_BLOCK, true);
	}
	
	public PacketPlaceBlock(int X, int Y, int Z) {
		super(PacketType.PLACE_BLOCK, true);
		x = X;
		y = Y;
		z = Z;
	}
	
	public void handle(NetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		World world = entityPlayer.worldObj;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock ghostBlock = (TileGhostBlock)entity;
		ghostBlock.place(entityPlayer);
	}
}
