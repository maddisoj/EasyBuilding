package eb.common.network;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.network.Player;
import eb.client.GhostBlockHandler;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class PacketUpdateGhost extends PacketEB {
	private boolean failed;
	private int blockID, metadata;
	
	public PacketUpdateGhost() {
		super(PacketType.TILE_UPDATE, true);
		init(0, 0, false);
	}
	
	public PacketUpdateGhost(int blockID, int metadata) {
		super(PacketType.TILE_UPDATE, true);
		init(blockID, metadata, false);
	}
	
	public PacketUpdateGhost(boolean failed) {
		super(PacketType.TILE_UPDATE, true);
		init(0, 0, true);
	}
	
	public void read(ByteArrayDataInput bis) {
		blockID = bis.readInt();
		metadata = bis.readInt();
		failed = bis.readBoolean();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(blockID);
		dos.writeInt(metadata);
		dos.writeBoolean(failed);
	}
	
	public void handle(INetworkManager manager, Player player) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			GhostBlockHandler.instance().update(blockID, metadata, failed);
		}
	}
	
	private void init(int blockID, int metadata, boolean failed) {
		this.blockID = blockID;
		this.metadata = metadata;
		this.failed = failed;
	}
}
