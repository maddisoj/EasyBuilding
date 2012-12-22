package eb.macro;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import eb.core.Constants;
import eb.core.mode.GhostMode;
import eb.macro.instruction.IInstruction;

/**
 * The class responsible for saving and loading macros
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class MacroIO {
	private static Map<String, Macro> loadedMacros = new HashMap<String, Macro>();

	static {
		setUpDirectories();
	}
	
	public static boolean save(Macro macro) {
		try {
			String path = getMacroPath(macro.getName());
			
			FileOutputStream fileOS = new FileOutputStream(path);
			DataOutputStream dataOS = new DataOutputStream(fileOS);
			
			NBTTagCompound instructionsTag = new NBTTagCompound();

			List<IInstruction> instructions = macro.getInstructions();
			for(int i = 0; i < instructions.size(); ++i) {
				instructionsTag.setCompoundTag("instruction" + i, instructionToTag(instructions.get(i)));
			}
			
			instructionsTag.setInteger("size", instructions.size());

			NBTTagCompound macroTag = new NBTTagCompound();
			macroTag.setString("requiredMode", macro.getRequiredMode().getName());
			macroTag.setString("name", macro.getName());
			macroTag.setString("description", macro.getDescription());
			macroTag.setCompoundTag("instructions", instructionsTag);
			
			NBTBase.writeNamedTag(macroTag, dataOS);

			dataOS.close();
			fileOS.close();
			
			loadedMacros.put(path, macro);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static Macro load(String name) {
		String path = getMacroPath(name);
		Macro requested = null;
		
		requested = loadedMacros.get(path);
		if(requested != null) {
			return requested;
		} else {
			try {
				return loadFromNBT(path);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static boolean macroExists(String name) {
		String path = getMacroPath(name);
		File file = new File(path);
		return file.exists();
	}
	
	public static void setUpDirectories() {
		String directories[] = new String[] { Constants.MACROS_PATH, Constants.SCHEMATICS_PATH };
		
		for(String directory : directories) {
			File file = new File(directory);
			if(file.exists()) {
				continue;
			}
			
			file.mkdirs();
		}
	}
	
	private static String getMacroPath(String name) {
		return Constants.MACROS_PATH + File.separator + name.trim().replace(" ", "_") + ".macro";
	}
	
	private static NBTTagCompound instructionToTag(IInstruction instruction) {
		NBTTagCompound instructionTag = new NBTTagCompound();
		
		instructionTag.setString("class", instruction.getClass().getName());
		
		String[] parameters = instruction.getParameters();
		
		if(parameters != null) {
			instructionTag.setInteger("numParameters", parameters.length);
			
			for(int i = 0; i < parameters.length; ++i) {
				instructionTag.setString("parameter" + i, parameters[i]);
			}
		}
		
		return instructionTag;
	}
	
	private static Macro loadFromNBT(String path) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileInputStream fileIS = new FileInputStream(path);
		DataInputStream dataIS = new DataInputStream(fileIS);
		NBTTagCompound macroTag = (NBTTagCompound) NBTBase.readNamedTag(dataIS);
		
		String name = macroTag.getString("name");
		String description = macroTag.getString("description");
		NBTTagCompound instructions = macroTag.getCompoundTag("instructions");
		int numInstructions = instructions.getInteger("size");
		
		Macro macro = new Macro(getRequriedModeFromName(macroTag.getString("requiredMode")));
		macro.setName(name);
		macro.setDescription(description);
		
		for(int i = 0; i < numInstructions; ++i) {
			NBTTagCompound instructionTag = instructions.getCompoundTag("instruction" + i);
			String instructionClassName = instructionTag.getString("class");
			
			Class instructionClass = Class.forName(instructionClassName);
			
			if(IInstruction.class.isAssignableFrom(instructionClass)) {
				IInstruction instruction = (IInstruction)instructionClass.newInstance();
				
				if(instruction == null) {
					dataIS.close();
					fileIS.close();
					return null;
				}
				
				String[] params = convertTagToParameters(instructionTag);
				
				if(instruction.parseParameters(params)) {
					macro.addInstruction(instruction);
				} else {
					dataIS.close();
					fileIS.close();
					return null;
				}
			}
		}
		
		dataIS.close();
		fileIS.close();
		
		return macro;
	}
	
	private static Class<? extends GhostMode> getRequriedModeFromName(String name) throws ClassNotFoundException {
		return (Class<? extends GhostMode>) Class.forName(name);
	}
	
	private static String[] convertTagToParameters(NBTTagCompound instructionTag) {
		int numParams = instructionTag.getInteger("numParameters");
		
		String[] parameters = new String[numParams];
		
		for(int i = 0; i < numParams; ++i) {
			parameters[i] = instructionTag.getString("parameter" + i);
		}
		
		return parameters;
	}
}
