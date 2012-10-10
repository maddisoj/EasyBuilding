package lerp.mods.easybuilding.network;

import java.io.DataOutputStream;
import java.io.IOException;

import lerp.mods.easybuilding.Direction;
import lerp.mods.easybuilding.TileGhostBlock;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketMoveGhost extends PacketGhostPosition {
	private Direction direction;
	
	public PacketMoveGhost() {
		super(PacketType.MOVE, true);
		direction = null;
	}
	
	public PacketMoveGhost(int X, int Y, int Z, Direction dir) {
		super(PacketType.MOVE, true);
		x = X;
		y = Y;
		z = Z;
		direction = dir;
	}
	
	public void setDirection(Direction dir) {
		direction = dir;
	}
	
	public void setDirection(int dir) {
		for(Direction d : Direction.values()) {
			if(dir == d.ordinal()) {
				direction = d;
				break;
			}
		}
	}
	
	@Override
	public void read(ByteArrayDataInput bis) {
		super.read(bis);
		int dir = bis.readInt();
		setDirection(dir);
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		super.getData(dos);
		dos.writeInt(direction.ordinal());
	}
	
	@Override
	public void handle(NetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		World world = entityPlayer.worldObj;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			return;
		}
		
		TileGhostBlock ghostBlock = (TileGhostBlock)entity;
		ghostBlock.move((EntityPlayer)player, direction);
	}
}
