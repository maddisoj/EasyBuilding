package eb.common.network;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

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
	private int blockID, metadata;
	
	public PacketUpdateGhost() {
		super(PacketType.TILE_UPDATE, true);
		blockID = 0;
		metadata = 0;
	}
	
	public PacketUpdateGhost(int blockID, int metadata) {
		super(PacketType.TILE_UPDATE, true);
		this.blockID = blockID;
		this.metadata = metadata;
	}
	
	public void read(ByteArrayDataInput bis) {
		blockID = bis.readInt();
		metadata = bis.readInt();
	}
	
	public void getData(DataOutputStream dos) throws IOException {
		dos.writeInt(blockID);
		dos.writeInt(metadata);
	}
	
	public void handle(INetworkManager manager, Player player) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			GhostBlockHandler.instance().update(blockID, metadata);
		}
	}
}
