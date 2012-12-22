package eb.macro;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import eb.mode.BuildMode;

public class SchematicImporter {
	private Macro macro;
	
	public SchematicImporter() {
		macro = null;
	}
	
	public Macro getMacro() {
		return macro;
	}
	
	public void importSchematic(String path) {
		DataInputStream input = getSchematicStream(path);
		
		try {
			NBTTagCompound base = (NBTTagCompound)NBTBase.readNamedTag(input);
			short width = base.getShort("Width");
			short length = base.getShort("Length");
			short height = base.getShort("Height");
			
			macro = BuildMode.translateRawData(width, length, height,
											   base.getByteArray("Blocks"),
											   base.getByteArray("Data"));
			macro.setName(getFileName(path));
			macro.setDescription("Imported from " + path);
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
