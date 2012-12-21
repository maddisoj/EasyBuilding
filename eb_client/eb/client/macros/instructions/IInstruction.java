package eb.client.macros.instructions;

/**
 * The interface for a macro instruction
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public interface IInstruction {
	public abstract void execute();
	public abstract String[] getParameters();
	public abstract boolean parseParameters(String[] parameters);
}
