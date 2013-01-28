package eb.core;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public enum Direction {
	FORWARD, RIGHT, BACKWARD, LEFT, UP, DOWN;
	
	private static Direction[] oppositeTable = { BACKWARD, LEFT, FORWARD, RIGHT, DOWN, UP };
	
	public boolean isOpposite(Direction direction) {
		return oppositeTable[ordinal()] == direction;
	}
	
	public Direction rotate(int numRotations) {
		if(ordinal() >= 4) { return this; }
				
		int index = (ordinal() + numRotations) % 4;
		
		return values()[index];
	}
}