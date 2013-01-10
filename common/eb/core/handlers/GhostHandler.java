package eb.core.handlers;

import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import eb.client.gui.GuiMacro;
import eb.client.gui.GuiMenu;
import eb.client.gui.GuiSchematic;
import eb.core.Direction;
import eb.core.EBHelper;
import eb.core.mode.GhostMode;
import eb.core.mode.GhostModeManager;
import eb.macro.Macro;
import eb.macro.instruction.IInstruction;
import eb.mode.BuildMode;
import eb.mode.RemoveMode;
import eb.mode.SelectionMode;

/**
 * The class responsible for tracking and operating the ghost
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GhostHandler {
	private static GhostHandler INSTANCE = new GhostHandler();
	
	private GuiMenu menu;
	private GhostMode mode;
	private KeyBindingHandler keyBindingHandler;

	private GhostHandler() {
		GhostModeManager.instance().addMode(new BuildMode());
		GhostModeManager.instance().addMode(new RemoveMode());
		GhostModeManager.instance().addMode(new SelectionMode());
		
		mode = GhostModeManager.instance().getMode(SelectionMode.class);
		
		menu = new GuiMenu();
		menu.addScreen("Load/Save Macro", new GuiMacro());
		menu.addScreen("Import Schematic", new GuiSchematic());
		
		keyBindingHandler = new KeyBindingHandler();
	}

	public static GhostHandler instance() {
		return INSTANCE;
	}
	
	@ForgeSubscribe
	public void render(RenderWorldLastEvent event) {
		mode.render(event.partialTicks);
	}
	
	public void setMode(String name) {
		if(name == null || name.isEmpty()) {
			return;
		}
		
		setMode(GhostModeManager.instance().getMode(name));
	}
	
	public void setMode(GhostMode mode) {
		if(mode != null) {
			this.mode = mode;
			
			EBHelper.printMessage(mode + " mode active");
		}
	}
	
	public GhostMode getMode() {
		return mode;
	}
	
	public void move(Direction direction) {		
		mode.move(direction);
	}

	public void place() {
		MovingObjectPosition target = EBHelper.getClient().objectMouseOver;
		
		if(target != null) {
			int[] pos = EBHelper.getPosition(target.blockX, target.blockY, target.blockZ, target.sideHit);
			
			mode.setGhostPosition(pos[0], pos[1], pos[2]);
			mode.setGhostPlaced(true);
		}
	}

	public void use() {
		mode.use();
	}

	public void toggleRecording() {
		if(mode.allowsMacros()) {
			mode.toggleRecording();
			
			if(mode.isRecording()) {
				EBHelper.printMessage("Started Recording");
			} else {
				EBHelper.printMessage("Finished Recording");
			}
		} else {
			EBHelper.printMessage("This mode does not support macros");
		}
	}

	public void playMacro() {
		if(mode.allowsMacros()) {	
			if(mode.isRecording()) {
				EBHelper.printMessage("You must stop recording before you can play the macro.");
				return;
			}
			
			mode.playMacro();
		} else {
			EBHelper.printMessage("This mode does not support macros");
		}
	}

	public void openMacroGui() {
		if(EBHelper.getPlayer() == null || EBHelper.getClient().currentScreen != null) { return; }
		
		EBHelper.getClient().displayGuiScreen(menu);
	}
	
	public void setMacro(Macro macro) {
		if(macro != null) {
			mode = GhostModeManager.instance().getMode(macro.getRequiredMode());
			
			if(mode != null) {
				mode.setMacro(macro);
				EBHelper.printMessage("Macro changed to \"" + macro.getName() + "\"");
				EBHelper.printMessage(macro.getName() + " has a " + macro.getRuntime() + " second runtime");
			}
		}
	}
	
	public KeyBindingHandler getKeyHandler() {
		return keyBindingHandler;
	}
}
