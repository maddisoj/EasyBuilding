package eb.client.macros.instructions;

import net.minecraftforge.common.ForgeDirection;
import eb.client.Direction;
import eb.client.GhostBlockHandler;

/**
 * The instruction representing the moving of the ghost block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class MoveInstruction implements IInstruction {
	private Direction dir;
	
	public MoveInstruction() {
		this.dir = null;
	}
	
	public MoveInstruction(Direction dir) {
		this.dir = dir;
	}
	
	@Override
	public void execute() {
		GhostBlockHandler.instance().move(dir);
	}

	@Override
	public String[] getParameters() {
		return new String[] { dir.name() };
	}

	@Override
	public boolean parseParameters(String[] parameters) {
		try {
			dir = Direction.valueOf(parameters[0]);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	public Direction getDirection() {
		return dir;
	}
}
