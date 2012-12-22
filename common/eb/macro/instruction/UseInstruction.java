package eb.macro.instruction;

import eb.core.handlers.GhostHandler;

/**
 * The instruction representing the placing of a block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class UseInstruction implements IInstruction {
	@Override
	public void execute() {
		GhostHandler.instance().use();
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	@Override
	public boolean parseParameters(String[] parameters) {
		return true;
	}
}
