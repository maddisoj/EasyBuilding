package eb.common.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public enum PacketType {
	PLACE_BLOCK(PacketPlaceBlock.class),
	TILE_UPDATE(PacketUpdateGhost.class),
	REMOVE_BLOCK(PacketRemoveBlock.class);

	private Class<? extends PacketEB> packetClass;

	private PacketType(Class<? extends PacketEB> packetClass) {
		this.packetClass = packetClass;
	}

	public static PacketEB createPacket(byte[] data) {
		ByteArrayDataInput bis = ByteStreams.newDataInput(data);
		int type = bis.readInt();

		PacketEB packet = null;

		try {
			for(PacketType pt : values()) {
				if(pt.ordinal() == type) {
					packet = pt.packetClass.newInstance();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(packet != null) {
			packet.read(bis);
		}

		return packet;
	}
}
