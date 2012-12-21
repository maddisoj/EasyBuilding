package eb.client.mode;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import eb.client.Direction;
import eb.client.macros.instructions.MoveInstruction;
import eb.client.macros.instructions.UseInstruction;
import eb.common.Constants;
import eb.common.EBHelper;
import static org.lwjgl.opengl.GL11.*;

public abstract class GhostMode {
	protected final float[] DEFAULT_COLOUR = { 1.0f, 1.0f, 1.0f, 0.5f };
	
	protected int x, y, z;
	protected boolean ghostPlaced;
	protected boolean recording;
	private Vec3 lockedDirection;
	
	public void setGhostPlaced(boolean placed) {
		this.ghostPlaced = placed;
	}
	
	public boolean isGhostPlaced() {
		return ghostPlaced;
	}
	
	public void setGhostPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	
	public void setLockedDirection(Vec3 direction) {
		this.lockedDirection = direction;
	}
	
	public MoveInstruction move(Direction direction) {
		if(isGhostPlaced()) {
			moveBy(direction, 1);
			
			return new MoveInstruction(direction);
		}
		
		return null;
	}
	
	public abstract UseInstruction use();
	public abstract boolean allowsMacros();
	
	public void render(float partialTicks) {
		if(!isGhostPlaced()) { return; }
		
		double size = 1.1;
		double[] min = new double[] { 0, 0, 0 };
		double[] max = new double[] { size, size, size };
		
        double centering = (size - 1.0) / 2.0;
		EntityPlayer player = EBHelper.getPlayer();
		double xOffset = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks + centering;
        double yOffset = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks + centering;
        double zOffset = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks + centering;
		
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glPushMatrix();
		glTranslated(-xOffset, -yOffset, -zOffset);

		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.setTranslation(x, y, z);
		tess.setColorRGBA_F(DEFAULT_COLOUR[0], DEFAULT_COLOUR[1], DEFAULT_COLOUR[2], DEFAULT_COLOUR[3]);
		
		renderTopFace(min, max);
		renderBottomFace(min, max);
		renderFrontFace(min, max);
		renderBackFace(min, max);
		renderLeftFace(min, max);
		renderRightFace(min, max);
		
		tess.draw();
		tess.setTranslation(0.0, 0.0, 0.0);
		
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glPopMatrix();
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
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(min[0], min[1], max[2]);
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
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
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
		t.addVertex(min[0], max[1], min[2]);
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(max[0], min[1], min[2]);
	}
}
