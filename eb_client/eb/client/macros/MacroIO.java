package eb.client.macros;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MacroIO {
	public static boolean save(Macro macro, String path) {
		try {
			File file = new File(path);

			file.getParentFile().mkdirs();			
			if(!file.createNewFile()) {
				return false;
			}

			PrintStream out = new PrintStream(file);			
			out.println("DESC " + macro.getDescription());
			out.println("MACRO");

			List<Instruction> instructions = macro.getInstructions();
			for(Instruction instruction : instructions) {
				out.print(instruction.getClass() + ", ");
				out.println(instruction.getParameters());
			}

			out.close();

			System.out.println("Saved to " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static Macro load(String path) {
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
					String[] parameters = line.substring(splitPoint + 1).split(" ");
					
					Class klass = Class.forName(instructionName);
					
					if(Instruction.class.isAssignableFrom(klass)) {
						Instruction instruction = (Instruction)klass.newInstance();
						instruction.parseParameters(parameters);
						
						macro.addInstruction(instruction);
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
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
