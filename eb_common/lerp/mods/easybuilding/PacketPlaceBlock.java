package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketPlaceBlock extends PacketEB {
	private int blockID;
	
	public PacketPlaceBlock() {
		super(PacketType.PLACE_BLOCK, true);
		this.blockID = -1;
	}
	
	public PacketPlaceBlock(int blockID) {
		super(PacketType.PLACE_BLOCK, true);
		this.blockID = blockID;
	}
	
	public void handle(NetworkManager manager, Player player) {
		GhostBlockHandler.instance().requestPlaceBlock((EntityPlayer)player, blockID);
	}
}
