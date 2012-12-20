package eb.client.macros;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import eb.client.mode.BuildMode;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

public class SchematicImporter {
	public enum State {
		IDLE, IMPORTING, OPTIMZING
	}
	
	private Macro macro;
	private State state;
	
	public SchematicImporter() {
		macro = null;
		state = State.IDLE;
	}
	
	public State getState() {
		return state;
	}
	
	public Macro getMacro() {
		return macro;
	}
	
	public void importSchematic(String path) {
		state = State.IMPORTING;
		DataInputStream input = getSchematicStream(path);
		
		try {
			NBTTagCompound base = (NBTTagCompound)NBTBase.readNamedTag(input);
			short width = base.getShort("Width");
			short length = base.getShort("Length");
			short height = base.getShort("Height");
			
			macro = BuildMode.translateRawData(base.getShort("Width"), base.getShort("Length"), base.getShort("Height"),
											   base.getByteArray("Blocks"),
											   base.getByteArray("Data"));
			macro.setName(getFileName(path));
			macro.setDescription("Imported from " + path);
			
			state = State.OPTIMZING;
			macro.optimize();
			
			state = State.IDLE;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private DataInputStream getSchematicStream(String path) {
		try {
			return new DataInputStream(new GZIPInputStream(new FileInputStream(path)));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getFileName(String path) {
		int start = path.lastIndexOf(File.separatorChar);
		int end = path.lastIndexOf('.');
		
		if(start == -1) { start = 0; }
		if(end == -1) { end = path.length(); }
		
		return path.substring(start + 1, end);
	}
}
