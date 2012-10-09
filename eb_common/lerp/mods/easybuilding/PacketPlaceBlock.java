package lerp.mods.easybuilding;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetworkManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.Player;

public class PacketPlaceBlock extends PacketEB {
	public PacketPlaceBlock() {
		super(PacketType.PLACE_BLOCK, true);
	}
	
	public void handle(NetworkManager manager, Player player) {
		ToBeDeleted.instance().requestPlaceBlock((EntityPlayer)player);
	}
}
