package eb.common.network;

import java.io.DataOutputStream;
import java.io.IOException;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;
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
		World world = entityPlayer.worldObj;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock ghostBlock = (TileGhostBlock)entity;
		ghostBlock.place(entityPlayer, itemID);
	}
}
