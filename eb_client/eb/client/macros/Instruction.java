package eb.client.macros;

public abstract class Instruction {
	public Instruction() {}
	
	public abstract void execute();
	public abstract String getParameters();
	public abstract boolean parseParameters(String[] parameters);
}
