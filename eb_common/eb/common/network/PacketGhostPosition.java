package eb.common.network;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;

/**
 * A packet that handles position information
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public abstract class PacketGhostPosition extends PacketEB {
	protected int x, y, z;
	
	public PacketGhostPosition(PacketType type, boolean isChunkDataPacket) {
		super(type, isChunkDataPacket);
		x = -1;
		y = -1;
		z = -1;
	}
	
	@Override
	public void read(ByteArrayDataInput bis) {
		x = bis.readInt();
		y = bis.readInt();
		z = bis.readInt();
	}
	
	@Override
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(x);
		dos.writeInt(y);
		dos.writeInt(z);
	}	
}
