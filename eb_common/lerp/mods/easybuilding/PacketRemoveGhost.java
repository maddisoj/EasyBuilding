package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketRemoveGhost extends PacketEB {
	private int x, y, z;
	
	public PacketRemoveGhost() {
		super(PacketType.REMOVE_GHOST, false);
	}

	public void read(ByteArrayDataInput bis) {
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(type.ordinal());
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
	}
	
	public void handle(NetworkManager manager, Player player) {
		ToBeDeleted.instance().requestRemove((EntityPlayer)player, x, y, z);
	}
}
