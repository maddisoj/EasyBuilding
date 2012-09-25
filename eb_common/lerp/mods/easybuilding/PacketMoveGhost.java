package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketMoveGhost extends PacketEB {
	private Direction direction;
	
	public PacketMoveGhost() {
		super(PacketType.MOVE, false);
		direction = Direction.FORWARD;
	}
	
	public PacketMoveGhost(Direction dir) {
		super(PacketType.MOVE, false);
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
		int dir = bis.readInt();
		setDirection(dir);
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(direction.ordinal());
	}
	
	@Override
	public void handle(NetworkManager manager, Player player) {
		GhostBlockHandler.instance().requestMove((EntityPlayer)player, direction);
	}
}
