package eb.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

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
import eb.client.macros.MoveInstruction;
import eb.client.macros.PlaceInstruction;
import eb.common.Constants;
import eb.common.Direction;
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
		macro = new Macro();
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
			sendPacket((new PacketMoveGhost(x, y, z, dir)).toCustomPayload());

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

		sendPacket((new PacketPlaceGhost(x, y, z)).toCustomPayload());
		placed = true;
	}

	public void remove() {
		if(placed) {
			placed = false;
			sendPacket((new PacketRemoveGhost(x, y, z)).toCustomPayload());
		}
	}

	public void update(EntityPlayer player, int X, int Y, int Z, int blockID) {
		TileEntity entity = player.worldObj.getBlockTileEntity(X, Y, Z);

		if(!(entity instanceof TileGhostBlock)) {
			return;
		}

		x = X;
		y = Y;
		z = Z;
		((TileGhostBlock)entity).setBlockId(blockID);
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
		} else {
			sendMessage("Finished Recording");
		}
	}

	public void playMacro() {
		if(recording) {
			sendMessage("You must stop recording before you can play the macro");
			return;
		}

		System.out.println(macro.toString());
		macro.run();
	}

	public void openMacroGui() {
		FMLClientHandler.instance().getClient().displayGuiScreen(new GuiMacro());
	}
	
	public boolean saveMacro(String name, String desc) {
		if(macro == null) { return false; }
		
		String path = Constants.MACROS_PATH + name;
		
		try {
			File file = new File(path);
			if(!file.createNewFile()) {
				System.out.println("Could not create new file");
			}
			
			System.out.println("File opened");
			PrintStream out = new PrintStream(file);
			System.out.println("PrintStream opened");
			
			out.println("NAME" + name);
			out.println("DESC" + desc + "\n");
			out.println("MACRO");
			out.print(macro.toString());
			out.close();
			
			System.out.println("Saved to " + file.getAbsolutePath());
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}
	
	public boolean loadMacro() {
		return false;
	}

	private void sendPacket(Packet packet) {
		FMLClientHandler.instance().sendPacket(packet);
	}

	private int getCurrentItem() {
		EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
		
		if(player != null) {
			return player.inventory.getCurrentItem().itemID;
		} else {
			return -1;
		}
	}

	private void sendMessage(String message) {
		FMLClientHandler.instance().getClient().thePlayer.addChatMessage(message);
	}
}
