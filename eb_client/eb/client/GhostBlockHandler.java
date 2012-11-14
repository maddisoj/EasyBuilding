package eb.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet15Place;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import eb.client.gui.GuiMacro;
import eb.client.macros.Direction;
import eb.client.macros.IInstruction;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.client.macros.MoveInstruction;
import eb.client.macros.PlaceInstruction;
import eb.client.macros.SchematicImporter;
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
	private int x, y, z, blockID, metadata;
	private boolean placed, recording, autoplace;
	private Macro macro;
	private Vec3 lockedDirection;

	private GhostBlockHandler() {		
		x = 0;
		y = 0;
		z = 0;
		blockID = 0;
		metadata = 0;
		placed = false;
		recording = false;
		autoplace = false;
		macro = null;
		lockedDirection = null;
		
		MacroIO.setUpDirectory();
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				long start = (new Date()).getTime();
				macro = SchematicImporter.importSchematic(Constants.SCHEMATICS_PATH + "rose-cake.schematic");
				long end = (new Date()).getTime();
				
				System.out.println("Schematic imported in " + (end - start) + " milliseconds");
				
				if(macro != null) {
					System.out.println("Macro will take: " + macro.getRuntime() + " seconds");
				}
			}
		}).start();*/
	}

	public static GhostBlockHandler instance() {
		return INSTANCE;
	}
	
	public void move(Direction direction) {
		if(placed) {
			if(autoplace) {
				placeBlock();
			}
			
			EntityClientPlayerMP player = getPlayer();
			World world = getWorld();
			Vec3 moveDirection = null;
			
			if(macro != null && macro.isPlaying()) {
				moveDirection = relativeToAbsoluteDirection(lockedDirection, direction); 
			} else {
				moveDirection = relativeToAbsoluteDirection(Helper.getPlayerDirection(player), direction);
			}
					
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

	public void update(int blockID, int metadata, boolean failed) {
		if(!failed) {
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
		
		if(macro != null && macro.isPlaying()) {
			macro.setSynced(true);
		}
	}

	public void placeBlock() {
		if(getClient().currentScreen == null) {
			placeBlock(getCurrentItemID(), getCurrentItemMetadata());
		}
	}
	
	public void placeBlock(int itemID, int metadata) {
		if(placed) {
			if(itemID == -1) {
				return;
			}
			
			sendPacket((new PacketPlaceBlock(x, y, z, itemID, metadata)).toCustomPayload());

			if(recording && !(macro.getLastInstruction() instanceof PlaceInstruction)) {
				macro.addInstruction(new PlaceInstruction(itemID, metadata));
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
			sendMessage("You must stop recording before you can play the macro");
			return;
		}

		if(macro != null) {
			if(autoplace) {
				toggleAutoplace();
			}
			
			if(!macro.isPlaying()) {
				lockedDirection = Helper.getPlayerDirection(getPlayer());
				macro.play();
			} else {
				macro.stop();
			}
		}
	}

	public void openMacroGui() {
		if(getPlayer() == null || getClient().currentScreen != null) { return; }
		
		getClient().displayGuiScreen(new GuiMacro());
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
	
	private Minecraft getClient() {
		return FMLClientHandler.instance().getClient();
	}
	
	private EntityClientPlayerMP getPlayer() {
		return getClient().thePlayer;
	}

	private int getCurrentItemID() {
		EntityClientPlayerMP player = getPlayer();
		
		if(player != null) {
			if(player.inventory.getCurrentItem() != null) {
				return player.inventory.getCurrentItem().itemID;
			}
		}
		
		return -1;
	}
	
	private int getCurrentItemMetadata() {
		EntityClientPlayerMP player = getPlayer();
		
		if(player != null) {
			if(player.inventory.getCurrentItem() != null) {
				return player.inventory.getCurrentItem().getItemDamage();
			}
		}
		
		return 0;
	}

	private void sendMessage(String message) {
		getPlayer().addChatMessage(message);
	}
	
	private World getWorld() {
		return getPlayer().worldObj;
	}
	
	private Vec3 relativeToAbsoluteDirection(Vec3 forward, Direction direction) {
		Vec3 result = Vec3.createVectorHelper(forward.xCoord, forward.yCoord, forward.zCoord);
		
		if(direction == Direction.BACKWARD) {
			result.rotateAroundY((float)Math.PI);
		} else if(direction == Direction.LEFT) {
			result.rotateAroundY((float)Math.PI/2);
		} else if(direction == Direction.RIGHT) {
			result.rotateAroundY((float)-Math.PI/2);
		} else if(direction == Direction.UP) {
			result = Vec3.createVectorHelper(0.0, 1.0, 0.0);
		} else if(direction == Direction.DOWN) {
			result = Vec3.createVectorHelper(0.0, -1.0, 0.0);
		}
		
		return result;
	}
}
