package eb.network.packet;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;
import eb.network.PacketType;

public class PacketRemoveBlock extends PacketGhostPosition {
	public PacketRemoveBlock() {
		super(PacketType.REMOVE_BLOCK, true);
	}

	public PacketRemoveBlock(int x, int y, int z) {
		super(PacketType.REMOVE_BLOCK, true);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void handle(INetworkManager manager, Player player) {
		EntityPlayer entityPlayer = (EntityPlayer)player;
		World world = entityPlayer.worldObj;
		
		if(entityPlayer.capabilities.isCreativeMode) {
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			
			if(block == null) {
				return;
			} else {
				int metadata = world.getBlockMetadata(x, y, z);
				
				if(block.removeBlockByPlayer(world, entityPlayer, x, y, z)) {
					block.onBlockDestroyedByPlayer(world, x, y, z, metadata);
					
					int newID = world.getBlockId(x, y, z);
					int newMetadata = world.getBlockMetadata(x, y, z);
					
					return;
				}
			}
		}
	}
}
