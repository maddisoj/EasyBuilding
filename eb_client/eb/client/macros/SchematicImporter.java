package eb.client.macros;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class SchematicImporter {
	public static Macro importSchematic(String path) {
		DataInputStream input = getDataInput(path);
		
		if(input == null) {
			return null;
		}
		
		try {
			NBTTagCompound base = (NBTTagCompound)NBTBase.readNamedTag(input);
			short width = base.getShort("Width");
			short length = base.getShort("Length");
			short height = base.getShort("Height");
			
			Macro schematic = createMacro(base.getShort("Width"),
							   			  base.getShort("Length"),
							   			  base.getShort("Height"),
							   			  base.getByteArray("Blocks"));
			
			if(schematic != null) {
				schematic.setName(getName(path));
			}
			
			return schematic;			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static DataInputStream getDataInput(String path) {
		try {
			return new DataInputStream(new GZIPInputStream(new FileInputStream(path)));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getName(String path) {
		int start = path.lastIndexOf(File.separatorChar);
		int end = path.lastIndexOf('.');
		
		if(end == -1) { end = path.length(); }
		
		return path.substring(start, end);
	}
	
	private static Macro createMacro(short width, short length, short height, byte[] blocks) {
		Macro schematic = new Macro();
		
		boolean leftToRight = true;
		
		for(int y = 0; y < height; ++y) {
			for(int z = 0; z < length; ++z) {
				for(int x =  (leftToRight ? 0 : width - 1);
						x != (leftToRight ? width : -1);
						x += (leftToRight ? 1 : -1)) {
					
					int index = y * width * length + z * width + x;
					
					if(blocks[index] != 0) {
						schematic.addInstruction(new PlaceInstruction(blocks[index], 0));
					}
					
					if(leftToRight) {
						schematic.addInstruction(new MoveInstruction(Direction.RIGHT));
					} else {
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
		
		//schematic.optimize();		
		return schematic;
	}
}
