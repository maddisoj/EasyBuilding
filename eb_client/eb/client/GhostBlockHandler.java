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
import net.minecraft.src.Vec3;
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
import eb.common.network.PacketPlaceBlock;

public class GhostBlockHandler {
	private static GhostBlockHandler INSTANCE = new GhostBlockHandler();
	private int x, y, z, blockID, metadata;
	private boolean placed, recording;
	private Macro macro;

	private GhostBlockHandler() {
		x = 0;
		y = 0;
		z = 0;
		blockID = 0;
		metadata = 0;
		placed = false;
		recording = false;
		macro = null;
	}

	public static GhostBlockHandler instance() {
		return INSTANCE;
	}
	
	public void move(Direction direction) {
		if(placed) {
			EntityClientPlayerMP player = getPlayer();
			World world = getWorld();
			Vec3 moveDirection = relativeToAbsoluteDirection(Helper.getPlayerDirection(player), direction); 
					
			int newX = x + (int)moveDirection.xCoord;
			int newY = y + (int)moveDirection.yCoord;
			int newZ = z + (int)moveDirection.zCoord;

			place(newX, newY, newZ);
			
			if(recording) {
				macro.addInstruction(new MoveInstruction(direction));
			}
		}
	}

	public void place(int X, int Y, int Z) {		
		if(placed) {
			remove();
		}

		World world = getWorld();
		x = X;
		y = Y;
		z = Z;
		blockID = world.getBlockId(x, y, z);
		metadata = world.getBlockMetadata(x, y, z);

		world.setBlock(x, y, z, Constants.GHOST_BLOCK_ID);
		TileGhostBlock ghost = Helper.getGhostBlock(world, x, y, z);
		if(ghost != null) {
			ghost.setBlockId(blockID);
			ghost.setBlockMetadata(metadata);
		}
		
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

	public void update(int blockID, int metadata) {
		World world = getWorld();
		world.setBlock(x, y, z, Constants.GHOST_BLOCK_ID);
		TileGhostBlock ghost = Helper.getGhostBlock(world, x, y, z);
		if(ghost != null) {
			ghost.setBlockId(blockID);
			ghost.setBlockMetadata(metadata);
			
			this.blockID = blockID;
			this.metadata = metadata;
		}
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

		if(macro != null) {
			macro.run();
		}
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
		sendMessage("Macro changed to \"" + macro.getName() + "\"");
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
	
	private Vec3 relativeToAbsoluteDirection(Vec3 forward, Direction direction) {
		if(direction == Direction.BACKWARD) {
			forward.rotateAroundY((float)Math.PI);
		} else if(direction == Direction.LEFT) {
			forward.rotateAroundY((float)Math.PI/2);
		} else if(direction == Direction.RIGHT) {
			forward.rotateAroundY((float)-Math.PI/2);
		} else if(direction == Direction.UP) {
			forward = Vec3.createVectorHelper(0.0, 1.0, 0.0);
		} else if(direction == Direction.DOWN) {
			forward = Vec3.createVectorHelper(0.0, -1.0, 0.0);
		}
		
		return forward;
	}
}
