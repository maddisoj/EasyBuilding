package eb.client.mode;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import eb.client.EBHelper;
import eb.client.TileGhostBlock;
import eb.client.macros.Direction;
import eb.client.macros.Macro;
import eb.client.macros.MoveInstruction;
import eb.common.Constants;
import eb.common.Helper;

public abstract class GhostBlockMode {
	protected int x, y, z;
	protected boolean ghostPlaced;
	protected int itemID, itemMetadata;
	protected boolean recording;
	private Vec3 lockedDirection;
	
	public void setGhostPlaced(boolean placed) {
		this.ghostPlaced = placed;
	}
	
	public boolean isGhostPlaced() {
		return ghostPlaced;
	}
	
	public void setGhostPosition(int x, int y, int z) {
		if(isGhostPlaced()) {
			removeGhost();
		}
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		placeGhost();
	}
	
	public int getGhostX() {
		return x;
	}
	
	public int getGhostY() {
		return y;
	}
	
	public int getGhostZ() {
		return z;
	}
	
	public void clearItem() {
		itemID = -1;
		itemMetadata = 0;
	}
	
	public void setItem(int itemID, int itemMetadata) {
		this.itemID = itemID;
		this.itemMetadata = itemMetadata;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public int getItemMetadata() {
		return itemMetadata;
	}
	
	/*public void setRecording(boolean recording) {
		this.recording = recording;
		
		if(recording) {
			macro = new Macro();
		}
	}
	
	public boolean isRecording() {
		return recording;
	}
	
	public void setMacro(Macro macro) {
		this.macro = macro;
	}
	
	public Macro getMacro() {
		return macro;
	}
	
	public boolean hasMacro() {
		return macro != null;
	}*/
	
	public void setLockedDirection(Vec3 direction) {
		this.lockedDirection = direction;
	}
	
	public void move(Direction direction) {
		if(isGhostPlaced()) {			
			EntityClientPlayerMP player = EBHelper.getPlayer();
			World world = EBHelper.getWorld();
			Vec3 moveDirection = null;
			
			if(lockedDirection != null) {
				moveDirection = relativeToAbsoluteDirection(lockedDirection, direction); 
			} else {
				moveDirection = relativeToAbsoluteDirection(Helper.getPlayerDirection(player), direction);
			}
			
			setGhostPosition(x + (int)moveDirection.xCoord,
							 y + (int)moveDirection.yCoord,
							 z = z + (int)moveDirection.zCoord);
		}
	}
	
	public void placeGhost() {
		World world = EBHelper.getWorld();
		
		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);

		world.setBlock(x, y, z, Constants.GHOST_BLOCK_ID);
		TileGhostBlock ghost = Helper.getGhostBlock(world, x, y, z);
		
		if(ghost != null) {
			ghost.setBlockId(blockID);
			ghost.setBlockMetadata(metadata);
		}
		
		setGhostPlaced(true);
	}
	
	public void removeGhost() {
		if(isGhostPlaced()) {			
			TileGhostBlock ghost = Helper.getGhostBlock(EBHelper.getWorld(), x, y, z);
			
			if(ghost != null) {
				ghost.remove();
			}

			setGhostPlaced(false);
		}
	}
	
	public abstract void use();
	public abstract boolean allowsMacros();
	
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
