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
 * The class responsible for tracking and operating the client's ghost block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GhostHandler {
	private static GhostHandler INSTANCE = new GhostHandler();
	
	private GuiMenu menu;
	private GhostMode mode;

	private GhostHandler() {
		GhostModeManager.addMode(new BuildMode());
		GhostModeManager.addMode(new RemoveMode());
		GhostModeManager.addMode(new SelectionMode());
		
		mode = GhostModeManager.getMode(BuildMode.class);
		
		menu = new GuiMenu(EBHelper.getClient());
		menu.addScreen("Load/Save Macro", new GuiMacro());
		menu.addScreen("Import Schematic", new GuiSchematic());
	}

	public static GhostHandler instance() {
		return INSTANCE;
	}
	
	@ForgeSubscribe
	public void render(RenderWorldLastEvent event) {
		mode.render(event.partialTicks);
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
	
			/*if(macro != null) {
				if(!macro.isPlaying()) {
					mode.setLockedDirection(EBHelper.getPlayerDirection(EBHelper.getPlayer()));
					macro.play();
				} else {
					macro.stop();
				}
			}*/
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
			mode = GhostModeManager.getMode(macro.getRequiredMode());
			
			if(mode != null) {
				mode.setMacro(macro);
				EBHelper.printMessage("Macro changed to \"" + macro.getName() + "\"");
				EBHelper.printMessage(macro.getName() + " has a " + macro.getRuntime() + " second runtime");
			}
		}
	}
}
