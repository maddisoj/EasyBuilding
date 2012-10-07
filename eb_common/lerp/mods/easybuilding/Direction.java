package lerp.mods.easybuilding;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Vec3;

public enum Direction {
	FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN;/*, EAST, WEST, NORTH, SOUTH;
	
	public Direction toAbsoluteDirection(EntityPlayer player) {
		if(this == UP || this == DOWN || this == EAST || this == WEST || this == NORTH || this == SOUTH) {
			return this;
		} else {
			Vec3 playerDirection = Helper.getPlayerDirection(player);
			
			if(this == FORWARD) {
				if(playerDirection.xCoord > 0.0) {
					return EAST;
				} else if(playerDirection.xCoord < 0.0) {
					return WEST;
				} else if(playerDirection.zCoord > 0.0) {
					return NORTH;
				} else if(playerDirection.zCoord < 0.0) {
					return SOUTH;
				}
			} else if(this == BACKWARD) {
				if(playerDirection.xCoord > 0.0) {
					return WEST;
				} else if(playerDirection.xCoord < 0.0) {
					return EAST;
				} else if(playerDirection.zCoord > 0.0) {
					return SOUTH;
				} else if(playerDirection.zCoord < 0.0) {
					return NORTH;
				}
			} else if(this == LEFT) {
				if(playerDirection.xCoord > 0.0) {
					return NORTH;
				} else if(playerDirection.xCoord < 0.0) {
					return SOUTH;
				} else if(playerDirection.zCoord > 0.0) {
					return EAST;
				} else if(playerDirection.zCoord < 0.0) {
					return WEST;
				}
			} else if(this == RIGHT) {
				if(playerDirection.xCoord > 0.0) {
					return SOUTH;
				} else if(playerDirection.xCoord < 0.0) {
					return NORTH;
				} else if(playerDirection.zCoord > 0.0) {
					return WEST;
				} else if(playerDirection.zCoord < 0.0) {
					return EAST;
				}
			}
		}
		
		return null;
	}*/
}
