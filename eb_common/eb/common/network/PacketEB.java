package eb.common.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketEB {
	protected PacketType type;
	protected boolean isChunkDataPacket;
	
	public PacketEB(PacketType type, boolean isChunkDataPacket) {
		this.type = type;
		this.isChunkDataPacket = isChunkDataPacket;
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		
		try {
			dos.writeInt(type.ordinal());
			getData(dos);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return bos.toByteArray();
	}
	
	public void read(ByteArrayDataInput bis) {}
	public void getData(DataOutputStream dos) throws IOException {}
	public void handle(NetworkManager manager, Player player) {}
	
	public Packet toCustomPayload() {
		byte[] data = toByteArray();
		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EasyBuilding";
		packet.data = data;
		packet.length = data.length;
		packet.isChunkDataPacket = isChunkDataPacket;
		
		return packet;
	}
}
