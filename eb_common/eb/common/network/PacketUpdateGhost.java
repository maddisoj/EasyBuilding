package eb.common.network;

import java.io.DataOutputStream;
import java.io.IOException;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.Player;
import eb.client.GhostBlockHandler;

public class PacketUpdateGhost extends PacketEB {
	private int x, y, z, blockID, metadata;
	
	public PacketUpdateGhost() {
		super(PacketType.TILE_UPDATE, true);
		x = -1;
		y = -1;
		z = -1;
		blockID = 0;
		metadata = 0;
	}
	
	public PacketUpdateGhost(int x, int y, int z, int blockID, int metadata) {
		super(PacketType.TILE_UPDATE, true);
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockID = blockID;
		this.metadata = metadata;
	}
	
	public int getBlockId() {
		return blockID;
	}
	
	public int getMetadata() {
		return metadata;
	}
	
	public void read(ByteArrayDataInput bis) {
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
		blockID = bis.readInt();
		metadata = bis.readInt();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
		dos.writeInt(blockID);
		dos.writeInt(metadata);
	}
	
	public void handle(NetworkManager manager, Player player) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			GhostBlockHandler.instance().update(x, y, z, blockID, metadata);
		}
	}
}
