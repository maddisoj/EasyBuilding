package eb.client;

import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.client.macros.gui.GuiMacro;
import eb.client.macros.gui.GuiMenu;
import eb.client.macros.gui.GuiSchematic;
import eb.client.macros.instructions.IInstruction;
import eb.client.macros.instructions.MoveInstruction;
import eb.client.macros.instructions.UseInstruction;
import eb.client.mode.BuildMode;
import eb.client.mode.GhostMode;
import eb.common.EBHelper;

/**
 * The class responsible for tracking and operating the client's ghost block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GhostBlockHandler {
	private static GhostBlockHandler INSTANCE = new GhostBlockHandler();
	
	private Macro macro;
	private boolean autoplace, recording;
	private GuiMenu menu;
	private GhostMode mode;

	private GhostBlockHandler() {
		autoplace = false;
		recording = false;
		mode = new BuildMode();
		
		menu = new GuiMenu(EBHelper.getClient());
		menu.addScreen("Load/Save Macro", new GuiMacro());
		menu.addScreen("Import Schematic", new GuiSchematic());
	}

	public static GhostBlockHandler instance() {
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
		addInstruction(mode.move(direction));
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
		addInstruction(mode.use());
	}

	public void toggleRecording() {
		if(mode.allowsMacros()) {
			recording = !recording;
	
			if(recording) {
				EBHelper.printMessage("Started Recording");
				macro = new Macro(mode.getClass());
			} else {
				EBHelper.printMessage("Finished Recording");
				macro.optimize();
			}
		}
	}

	public void playMacro() {
		if(recording) {
			EBHelper.printMessage("You must stop recording before you can play the macro.");
			return;
		}

		if(macro != null) {
			if(!macro.isPlaying()) {
				mode.setLockedDirection(EBHelper.getPlayerDirection(EBHelper.getPlayer()));
				macro.play();
			} else {
				macro.stop();
			}
		}
	}

	public void openMacroGui() {
		if(EBHelper.getPlayer() == null || EBHelper.getClient().currentScreen != null) { return; }
		
		EBHelper.getClient().displayGuiScreen(menu);
	}
	
	public void setMacro(Macro macro) {
		if(macro != null) {
			this.macro = macro;
			EBHelper.printMessage("Macro changed to \"" + macro.getName() + "\"");
			EBHelper.printMessage(macro.getName() + " has a " + macro.getRuntime() + " second runtime");
		}
	}
	
	public Macro getMacro() {
		return macro;
	}
	
	private void addInstruction(IInstruction instruction) {
		if(recording && macro != null && instruction != null) {
			macro.addInstruction(instruction);
		}
	}
}
