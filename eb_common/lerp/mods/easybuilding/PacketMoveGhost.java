package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketMoveGhost extends PacketEB {
	private int x, y, z;
	private Direction direction;
	
	public PacketMoveGhost() {
		super(PacketType.MOVE, false);
		this.x = -1;
		this.y = -1;
		this.z = -1;
		direction = null;
	}
	
	public PacketMoveGhost(int x, int y, int z, Direction dir) {
		super(PacketType.MOVE, false);
		this.x = x;
		this.y = y;
		this.z = z;
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
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
		int dir = bis.readInt();
		setDirection(dir);
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
		dos.writeInt(direction.ordinal());
	}
	
	@Override
	public void handle(NetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		World world = entityPlayer.worldObj;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(!(entity instanceof TileGhostBlock)) {
			System.out.println("Entity not ghost block (" + x + " " + y + " " + z + ")");
			return;
		}
		
		TileGhostBlock ghostBlock = (TileGhostBlock)entity;
		ghostBlock.move((EntityPlayer)player, direction);
	}
}
