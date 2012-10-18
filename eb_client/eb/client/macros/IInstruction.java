package eb.client.macros;

public interface IInstruction {
	public abstract void execute();
	public abstract String getParameters();
	public abstract boolean parseParameters(String[] parameters);
}
