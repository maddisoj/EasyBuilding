package eb.client;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import eb.client.macros.Direction;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.client.macros.MoveInstruction;
import eb.client.macros.UseInstruction;
import eb.client.macros.gui.GuiMacro;
import eb.client.macros.gui.GuiMenu;
import eb.client.macros.gui.GuiSchematic;
import eb.client.macros.gui.GuiSubBlock;
import eb.client.mode.BuildMode;
import eb.client.mode.GhostBlockMode;
import eb.common.Constants;
import eb.common.Helper;
import eb.common.network.PacketPlaceBlock;

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
	private GhostBlockMode mode;

	private GhostBlockHandler() {
		autoplace = false;
		recording = false;
		mode = new BuildMode();
		
		menu = new GuiMenu(EBHelper.getClient());
		menu.addScreen("Load/Save Macro", new GuiMacro());
		menu.addScreen("Import Schematic", new GuiSchematic());
		//menu.addScreen("Substitute Block", new GuiSubBlock());
	}

	public static GhostBlockHandler instance() {
		return INSTANCE;
	}
	
	public void move(Direction direction) {
		if(autoplace) {
			placeBlock();
		}
		
		mode.move(direction);
		
		if(recording) {
			macro.addInstruction(new MoveInstruction(direction));
		}
	}

	public void place(int x, int y, int z) {
		mode.setGhostPosition(x, y, z);
	}

	public void update(int blockID, int metadata, boolean failed) {
		if(!failed) {
			if(mode.isGhostPlaced()) {
				int x = mode.getGhostX();
				int y = mode.getGhostY();
				int z = mode.getGhostZ();
				
				TileGhostBlock ghost = Helper.getGhostBlock(EBHelper.getWorld(), x, y, z);
				
				if(ghost != null) {
					ghost.setBlockId(blockID);
					ghost.setBlockMetadata(metadata);
				}
			}
		}
		
		if(macro != null) {
			if(macro.isPlaying()) {
				macro.setSynced(true);
			} else {
				mode.setLockedDirection(null);
			}
		}
	}

	public void placeBlock() {
		mode.clearItem();
		mode.use();
		
		if(recording) {
			macro.addInstruction(new UseInstruction(mode.getItemID(), mode.getItemMetadata()));
		}
	}
	
	public void placeBlock(int itemID, int metadata) {
		mode.setItem(itemID, metadata);
		mode.use();
		
		if(recording) {
			macro.addInstruction(new UseInstruction(mode.getItemID(), mode.getItemMetadata()));
		}
	}

	public void toggleRecording() {
		recording = !recording;

		if(recording) {
			sendMessage("Started Recording");
			macro = new Macro();
		} else {
			sendMessage("Finished Recording");
			macro.optimize();
		}
	}
	
	public void toggleAutoplace() {
		autoplace = !autoplace;

		if(autoplace) {
			sendMessage("Autoplace enabled");
		} else {
			sendMessage("Autoplace disabled");
		}
	}

	public void playMacro() {
		if(recording) {
			sendMessage("You must stop recording before you can play the macro.");
			return;
		}

		if(macro != null) {
			if(autoplace) {
				toggleAutoplace();
			}
			
			if(!macro.isPlaying()) {
				mode.setLockedDirection(Helper.getPlayerDirection(EBHelper.getPlayer()));
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
	
	public boolean saveMacro(String name, String desc) {
		if(macro != null) { return false; }
		
		macro.setName(name);
		macro.setDescription(desc);
		
		return MacroIO.save(macro);
	}
	
	public void setMacro(String name) {
		setMacro(MacroIO.load(name));
	}
	
	public void setMacro(Macro macro) {		
		if(macro != null) {
			sendMessage("Macro changed to \"" + macro.getName() + "\"");
			sendMessage(macro.getName() + " has a " + macro.getRuntime() + " second runtime");
		}
	}
	
	public Macro getMacro() {
		return macro;
	}

	public void sendMessage(String message) {
		EBHelper.getPlayer().addChatMessage(message);
	}
}
