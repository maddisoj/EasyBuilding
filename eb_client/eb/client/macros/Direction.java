package eb.client.macros;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Vec3;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public enum Direction {
	FORWARD, BACKWARD(FORWARD), LEFT, RIGHT(LEFT), UP, DOWN(UP);
	
	private Direction opposite;
	
	private Direction() {
		this(null);
	}
	
	private Direction(Direction opposite) {
		this.opposite = opposite;
		
		if(opposite != null) {
			opposite.setOpposite(this);
		}
	}
	
	public boolean isOpposite(Direction direction) {
		return (direction == opposite);
	}
	
	private void setOpposite(Direction direction) {
		opposite = direction;
	}
}
