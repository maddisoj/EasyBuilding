package eb.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import eb.client.gui.GuiMacro;
import eb.client.macros.IInstruction;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.client.macros.MoveInstruction;
import eb.client.macros.PlaceInstruction;
import eb.common.Constants;
import eb.common.Direction;
import eb.common.Helper;
import eb.common.TileGhostBlock;
import eb.common.network.PacketMoveGhost;
import eb.common.network.PacketPlaceBlock;
import eb.common.network.PacketPlaceGhost;
import eb.common.network.PacketRemoveGhost;

public class GhostBlockHandler {
	private static GhostBlockHandler INSTANCE = new GhostBlockHandler();
	private int x, y, z;
	private boolean placed, recording;
	private Macro macro;

	private GhostBlockHandler() {
		x = 0;
		y = 0;
		z = 0;
		placed = false;
		recording = false;
		macro = null;
	}

	public static GhostBlockHandler instance() {
		return INSTANCE;
	}

	public int getX() {
		return x;
	}

	public void setX(int X) {
		x = X;
	}

	public int getY() {
		return y;
	}

	public void setY(int Y) {
		y = Y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int Z) {
		z = Z;
	}

	public void move(Direction dir) {
		if(placed) {
			TileGhostBlock ghost = Helper.getGhostBlock(getWorld(), x, y, z);
			if(ghost != null) {
				ghost.move(getPlayer(), dir);
			}
			
			if(recording) {
				macro.addInstruction(new MoveInstruction(dir));
			}
		}
	}

	public void place(int X, int Y, int Z) {		
		if(placed) {
			remove();
		}

		x = X;
		y = Y;
		z = Z;

		getWorld().setBlock(x, y, z, Constants.GHOST_BLOCK_ID);		
		placed = true;
	}

	public void remove() {
		if(placed) {			
			TileGhostBlock ghost = Helper.getGhostBlock(getWorld(), x, y, z);
			if(ghost != null) {
				ghost.remove();
			}
			
			placed = false;
		}
	}

	public void update(int X, int Y, int Z, int blockID, int metadata) {
		TileGhostBlock ghost = Helper.getGhostBlock(getWorld(), X, Y, Z);
		if(ghost == null) {
			System.out.println("broke");
			if(placed) {
				place(X, Y, Z);
				ghost = Helper.getGhostBlock(getWorld(), X, Y, Z);
			} else {
				return;
			}
		}

		x = X;
		y = Y;
		z = Z;
		ghost.setBlockId(blockID);
		ghost.setBlockMetadata(metadata);
	}

	public void placeBlock() {
		placeBlock(getCurrentItem());
	}
	
	public void placeBlock(int itemID) {		
		if(placed) {
			if(itemID == -1) {
				return;
			}
			
			sendPacket((new PacketPlaceBlock(x, y, z, itemID)).toCustomPayload());

			if(recording && !(macro.getLastInstruction() instanceof PlaceInstruction)) {
				macro.addInstruction(new PlaceInstruction(itemID));
			}
		}
	}

	public void toggleRecording() {
		recording = !recording;

		if(recording) {
			sendMessage("Started Recording");
			macro = new Macro();
		} else {
			sendMessage("Finished Recording");
		}
	}

	public void playMacro() {
		if(recording) {
			sendMessage("You must stop recording before you can play the macro");
			return;
		}

		macro.run();
	}

	public void openMacroGui() {
		FMLClientHandler.instance().getClient().displayGuiScreen(new GuiMacro());
	}
	
	public boolean saveMacro(String name, String desc) {
		if(macro == null) { return false; }
		
		macro.setName(name);
		macro.setDescription(desc);
		
		return MacroIO.save(macro);
	}
	
	public void setMacro(String name) {
		macro = MacroIO.load(name);
	}

	private void sendPacket(Packet packet) {
		FMLClientHandler.instance().sendPacket(packet);
	}
	
	private EntityClientPlayerMP getPlayer() {
		return FMLClientHandler.instance().getClient().thePlayer;
	}

	private int getCurrentItem() {
		EntityClientPlayerMP player = getPlayer();
		
		if(player != null) {
			if(player.inventory.getCurrentItem() != null) {
				return player.inventory.getCurrentItem().itemID;
			}
		}
		
		return -1;
	}

	private void sendMessage(String message) {
		getPlayer().addChatMessage(message);
	}
	
	private World getWorld() {
		return getPlayer().worldObj;
	}
}
