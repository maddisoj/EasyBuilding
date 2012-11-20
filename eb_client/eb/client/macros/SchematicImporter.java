package eb.client.macros;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

public class SchematicImporter {
	public enum State {
		IDLE, IMPORTING, OPTIMZING
	}
	
	private State state;
	
	public SchematicImporter() {
		state = State.IDLE;
	}
	
	public State getState() {
		return state;
	}
	
	public Macro importSchematic(String path) {
		state = State.IMPORTING;
		DataInputStream input = getSchematicStream(path);
		
		if(input == null) {
			return null;
		}
		
		try {
			
			NBTTagCompound base = (NBTTagCompound)NBTBase.readNamedTag(input);
			short width = base.getShort("Width");			
			short length = base.getShort("Length");
			short height = base.getShort("Height");
			
			Macro schematic = createSchematicMacro(base.getShort("Width"),
												   base.getShort("Length"),
									   			   base.getShort("Height"),
									   			   base.getByteArray("Blocks"),
									   			   base.getByteArray("Data"));
			
			if(schematic != null) {
				schematic.setName(getFileName(path));
			}
			
			return schematic;			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private DataInputStream getSchematicStream(String path) {
		try {
			return new DataInputStream(new GZIPInputStream(new FileInputStream(path)));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Macro createSchematicMacro(short width, short length, short height, byte[] blocks, byte[] meta) {
		Macro schematic = new Macro();
		
		boolean leftToRight = true;
		
		float progressStep = 1 / (width * height * length);
		
		for(int y = 0; y < height; ++y) {
			for(int z = 0; z < length; ++z) {
				for(int x =  (leftToRight ? 0 : width - 1);
						x != (leftToRight ? width : -1);
						x += (leftToRight ? 1 : -1)) {
					
					int index = y * width * length + z * width + x;
					
					if(blocks[index] != 0) {
						schematic.addInstruction(new PlaceInstruction(blocks[index], meta[index]));
					}
					
					if(leftToRight && x != width - 1) {
						schematic.addInstruction(new MoveInstruction(Direction.RIGHT));
					} else if(!leftToRight && x != 0) {
						schematic.addInstruction(new MoveInstruction(Direction.LEFT));
					}
				}
				
				leftToRight = !leftToRight;
				schematic.addInstruction(new MoveInstruction(Direction.FORWARD));
			}
			
			schematic.addInstruction(new MoveInstruction(Direction.UP));
			
			for(int i = 0; i < length; ++i) {
				schematic.addInstruction(new MoveInstruction(Direction.BACKWARD));
			}
		}
		
		state = State.OPTIMZING;
		schematic.optimize();
		
		state = State.IDLE;
		return schematic;
	}
	
	private String getFileName(String path) {
		int start = path.lastIndexOf(File.separatorChar);
		int end = path.lastIndexOf('.');
		
		if(start == -1) { start = 0; }
		if(end == -1) { end = path.length(); }
		
		return path.substring(start + 1, end);
	}
}
