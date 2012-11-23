package eb.client.macros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import eb.common.Constants;

/**
 * The class responsible for saving and loading macros
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class MacroIO {
	private static Map<String, Macro> loadedMacros = new HashMap<String, Macro>();
	
	public static boolean save(Macro macro) {
		try {
			String path = getMacroPath(macro.getName());
			
			FileWriter writer = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(writer);
			
			out.write("DESC " + macro.getDescription());
			out.newLine();
			out.write("MACRO");
			out.newLine();

			List<IInstruction> instructions = macro.getInstructions();
			for(IInstruction instruction : instructions) {				
				out.write(instruction.getClass().getCanonicalName() + ", ");
				out.write(instruction.getParameters());
				out.newLine();
			}

			out.close();
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
		}
		
		try {
			requested = new Macro();
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			boolean inMacro = false;
			
			requested.setName(name);

			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if(line.isEmpty()) { continue; }

				if(inMacro) {
					int splitPoint = line.indexOf(',');
					String instructionName = line.substring(0, splitPoint);
					String[] parameters = line.substring(splitPoint + 1).trim().split(" ");
					
					Class klass = Class.forName(instructionName);
					
					if(IInstruction.class.isAssignableFrom(klass)) {
						IInstruction instruction = (IInstruction)klass.newInstance();
						
						if(instruction == null) {
							scanner.close();
							return null;
						}
						
						if(instruction.parseParameters(parameters)) {
							requested.addInstruction(instruction);
						} else {
							scanner.close();
							return null;
						}
					}
				} else {
					String[] tokens = line.split(" ");
					
					if(tokens[0].toLowerCase().equals("macro")) {
						inMacro = true;
					} else if(tokens[0].toLowerCase().equals("desc")) {
						requested.setDescription(line.substring(5)); //rest of the line after "desc "
					}
				}
			}

			scanner.close();
			loadedMacros.put(path, requested);
			return requested;
			
		} catch(Exception e) {
			e.printStackTrace();
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
		return Constants.MACROS_PATH + File.separator + name.trim().replace(" ", "_") + ".txt";
	}
	
	static {
		setUpDirectories();
	}
}
