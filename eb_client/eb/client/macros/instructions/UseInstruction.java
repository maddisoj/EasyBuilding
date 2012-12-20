package eb.client.macros.instructions;

import eb.client.GhostBlockHandler;

/**
 * The instruction representing the placing of a block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class UseInstruction implements IInstruction {
	@Override
	public void execute() {
		GhostBlockHandler.instance().use();
	}

	@Override
	public String getParameters() {
		return null;
	}

	@Override
	public boolean parseParameters(String[] parameters) {
		return true;
	}
}
