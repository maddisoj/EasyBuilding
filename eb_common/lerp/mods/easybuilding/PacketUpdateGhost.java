package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketUpdateGhost extends PacketEB {
	private int x, y, z, blockId;
	
	public PacketUpdateGhost() {
		super(PacketType.TILE_UPDATE, true);
		x = -1;
		y = -1;
		z = -1;
		blockId = 0;
	}
	
	public PacketUpdateGhost(int x, int y, int z, int blockId) {
		super(PacketType.TILE_UPDATE, true);
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockId;
	}
	
	public int getBlockId() {
		return blockId;
	}
	
	public void read(ByteArrayDataInput bis) {
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
		blockId = bis.readInt();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
		dos.writeInt(blockId);
	}
	
	public void handle(NetworkManager manager, Player player) {
		World world = ((EntityPlayer)player).worldObj;
		
		if(world.isRemote) {
			TileEntity entity = world.getBlockTileEntity(x, y, z);
			
			if(!(entity instanceof TileGhostBlock)) {
				return;
			}
			
			((TileGhostBlock)entity).handleUpdatePacket(this);
			GhostBlockHandler.instance().update(x, y, z);
		}
	}
	
}
