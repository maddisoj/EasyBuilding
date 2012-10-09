package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketPlaceGhost extends PacketEB {
	private int x, y, z;
	
	public PacketPlaceGhost() {
		super(PacketType.PLACE_GHOST, false);
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public PacketPlaceGhost(int x, int y, int z) {
		super(PacketType.PLACE_GHOST, false);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void read(ByteArrayDataInput bis) {
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
	}
	
	public void handle(NetworkManager manager, Player player) {
		ToBeDeleted.instance().requestPlace((EntityPlayer)player, x, y, z);
	}
}
