package eb.common;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemRedstone;
import net.minecraft.src.Vec3;

public class Helper {
	public static int[] getPosition(int x, int y, int z, int side) {
		if(side == 0) { //bottom
			y--;
		} else if(side == 1) { //top
			y++;
		} else if(side == 2) { //east
			z--;
		} else if(side == 3) { //west
			z++;
		} else if(side == 4) { //north
			x--;
		} else if(side == 5) { //south
			x++;
		}
		
		return new int[]{x, y, z};
	}
	
	public static Vec3 getPlayerDirection(EntityPlayer player) {
		Vec3 look = player.getLookVec();
		Vec3 absLook = Vec3.createVectorHelper(Math.abs(look.xCoord), Math.abs(look.yCoord), Math.abs(look.zCoord));
		Vec3 playerDirection = Vec3.createVectorHelper(0.0, 0.0, 0.0);
		
		if(absLook.xCoord >= absLook.zCoord) {
			playerDirection.xCoord = 1.0 * (look.xCoord >= 0.0 ? 1.0 : -1.0);
		} else {
			playerDirection.zCoord = 1.0 * (look.zCoord >= 0.0 ? 1.0 : -1.0);
		}
		
		return playerDirection;
	}
	
	public static Block getPlacedBlock(Item item) {
		if(item == null) {
			return null;
		}
		
		if(item instanceof ItemBlock) {
			return Block.blocksList[((ItemBlock)item).getBlockID()];
		} else if(item instanceof ItemRedstone) {
			return Block.redstoneWire;
		}
		
		return null;
	}
}
