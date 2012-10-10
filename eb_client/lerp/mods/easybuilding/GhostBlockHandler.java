package lerp.mods.easybuilding;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;

public class GhostBlockHandler {
	private static GhostBlockHandler INSTANCE = new GhostBlockHandler();
	private int x, y, z;
	private boolean placed;
	
	private GhostBlockHandler() {
		x = 0;
		y = 0;
		z = 0;
		placed = false;
	}
	
	public static GhostBlockHandler instance() {
		return INSTANCE;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int X) {
		x = X;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int Y) {
		y = Y;
	}
	
	public int getZ() {
		return z;
	}
	
	public void setZ(int Z) {
		z = Z;
	}
	
	public void move(Direction dir) {
		FMLClientHandler.instance().sendPacket((new PacketMoveGhost(x, y, z, dir)).toCustomPayload());
	}
	
	public void place(int X, int Y, int Z) {		
		if(placed) {
			remove();
		}
		
		x = X;
		y = Y;
		z = Z;
		
		FMLClientHandler.instance().sendPacket((new PacketPlaceGhost(x, y, z)).toCustomPayload());
		placed = true;
	}
	
	public void remove() {
		if(placed) {
			placed = false;
			FMLClientHandler.instance().sendPacket((new PacketRemoveGhost(x, y, z)).toCustomPayload());
		}
	}
	
	public void update(EntityPlayer player, int X, int Y, int Z, int blockID) {
		TileEntity entity = player.worldObj.getBlockTileEntity(x, y, z); //should be X Y Z
		
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		x = X;
		y = Y;
		z = Z;
		((TileGhostBlock)entity).setBlockId(blockID);
	}
	
	public void placeBlock() {
		if(placed) {
			FMLClientHandler.instance().sendPacket((new PacketPlaceBlock(x, y, z)).toCustomPayload());
		}
	}
}
