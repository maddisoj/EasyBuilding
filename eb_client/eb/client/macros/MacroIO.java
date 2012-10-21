package eb.client.macros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import eb.common.Constants;

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
		String path = Constants.MACROS_PATH + name;
		Macro requested = null;
		
		requested = loadedMacros.get(path);
		if(requested != null) {
			return requested;
		}
		
		try {
			Macro macro = new Macro();
			File file = new File(path);
			Scanner scanner = new Scanner(file);
			boolean inMacro = false;

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
							macro.addInstruction(instruction);
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
						macro.setDescription(line.substring(5)); //rest of the line after "desc "
					}
				}
			}

			scanner.close();
			loadedMacros.put(path, macro);
			return macro;
			
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
	
	private static String getMacroPath(String name) {
		return Constants.MACROS_PATH + name.trim().replace(" ", "_") + ".txt";
	}
}
