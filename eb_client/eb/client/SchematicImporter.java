package eb.client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import net.minecraft.src.NBTBase;
import net.minecraft.src.NBTTagCompound;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import eb.client.macros.Macro;

@SideOnly(Side.CLIENT)
public class SchematicImporter {
	public static Macro importSchematic(String path) {
		DataInput input = getDataInput(path);
		
		try {
			System.out.println(NBTBase.getTagName(input.readByte()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static DataInput getDataInput(String path) {
		try {
			return new DataInputStream(new FileInputStream(path));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getName(String path) {
		int start = path.lastIndexOf('/');
		int end = path.lastIndexOf('.');
		
		if(end == -1) { end = path.length(); }
		
		return path.substring(start, end);
	}
}
