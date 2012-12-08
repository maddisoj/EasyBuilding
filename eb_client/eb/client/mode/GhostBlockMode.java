package eb.client.mode;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import eb.client.TileGhostBlock;
import eb.client.macros.Direction;
import eb.common.Constants;
import eb.common.EBHelper;
import static org.lwjgl.opengl.GL11.*;

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
	
	public void setLockedDirection(Vec3 direction) {
		this.lockedDirection = direction;
	}
	
	public void move(Direction direction) {
		moveBy(direction, 1);
	}
	
	public void placeGhost() {
		if(isGhostPlaced()) {
			removeGhost();
		}
		
		World world = EBHelper.getWorld();
		
		int blockID = world.getBlockId(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);

		world.setBlock(x, y, z, Constants.GHOST_BLOCK_ID);
		TileGhostBlock ghost = EBHelper.getGhostBlock(world, x, y, z);
		
		if(ghost != null) {
			ghost.setBlockId(blockID);
			ghost.setBlockMetadata(metadata);
		}
		
		setGhostPlaced(true);
	}
	
	public void removeGhost() {
		if(isGhostPlaced()) {			
			TileGhostBlock ghost = EBHelper.getGhostBlock(EBHelper.getWorld(), x, y, z);
			
			if(ghost != null) {
				ghost.remove();
			}

			setGhostPlaced(false);
		}
	}
	
	public abstract void use();
	public abstract boolean allowsMacros();
	
	public void render(TileGhostBlock ghost, double x, double y, double z) {
		Tessellator t = Tessellator.instance;

		glDisable(GL_TEXTURE_2D);
		glDisable(GL_CULL_FACE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		double[] min = new double[3];
		double[] max = new double[3];
		min[0] = x;
		min[1] = y;
		min[2] = z;
		max[0] = x + 1.0;
		max[1] = y + 1.0;
		max[2] = z + 1.0;
		t.startDrawingQuads();
		
		if(ghost.getBlockId() == 0) {			
			t.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.9f);
		} else {
			t.setColorRGBA_F(1.0f, 0.0f, 0.0f, 0.9f);
		}
		
		renderTopFace(min, max);
		renderBottomFace(min, max);
		renderFrontFace(min, max);
		renderBackFace(min, max);
		renderLeftFace(min, max);
		renderRightFace(min, max);
		
		t.draw();
		
		glDisable(GL_BLEND);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
	}
	
	protected final void moveBy(Direction direction, int amount) {
		if(isGhostPlaced()) {			
			EntityClientPlayerMP player = EBHelper.getPlayer();
			World world = EBHelper.getWorld();
			Vec3 moveDirection = null;
			
			if(lockedDirection != null) {
				moveDirection = relativeToAbsoluteDirection(lockedDirection, direction); 
			} else {
				moveDirection = relativeToAbsoluteDirection(EBHelper.getPlayerDirection(player), direction);
			}
			
			setGhostPosition(x + ((int)moveDirection.xCoord * amount),
							 y + ((int)moveDirection.yCoord * amount),
							 z + ((int)moveDirection.zCoord * amount));
		}
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
	
	private void renderTopFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 1.0f, 0.0f);
		t.addVertex(min[0], max[1], min[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], max[1], min[2]);
	}
	
	private void renderBottomFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, -1.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], min[1], min[2]);
	}
	
	private void renderFrontFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, 1.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(min[0], max[1], min[2]);
	}
	
	private void renderBackFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, -1.0f);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], max[1], min[2]);
	}
	
	private void renderLeftFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
	}
	
	private void renderRightFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(-1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(min[0], max[1], min[2]);
	}
}
