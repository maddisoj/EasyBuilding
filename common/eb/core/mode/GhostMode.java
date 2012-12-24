package eb.core.mode;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import eb.core.Direction;
import eb.core.EBHelper;
import eb.macro.Macro;
import eb.macro.instruction.IInstruction;
import eb.macro.instruction.MoveInstruction;
import eb.macro.instruction.UseInstruction;

public abstract class GhostMode {
	protected final float[] DEFAULT_COLOUR = { 1.0f, 1.0f, 1.0f, 0.5f };
	
	protected int x, y, z;
	protected boolean ghostPlaced;
	protected boolean recording;
	protected Macro macro;
	protected Vec3 lockedDirection;
	
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
	
	public boolean isRecording() {
		return recording;
	}
	
	public void setLockedDirection(Vec3 direction) {
		this.lockedDirection = direction;
	}
	
	public void move(Direction direction) {
		if(isGhostPlaced()) {
			moveBy(direction, 1);
			
			if(isRecording() && allowsMacros()) {
				addInstruction(new MoveInstruction(direction));
			}
		}
	}
	
	public void toggleRecording() {
		if(allowsMacros()) {
			recording = !recording;
			
			if(isRecording()) {
				setLockedDirection(EBHelper.getPlayerDirection(EBHelper.getPlayer()));
				macro = new Macro(getClass());
			} else {
				setLockedDirection(null);
				macro.optimize();
			}
		}
	}
	
	public abstract void use();
	public abstract boolean allowsMacros();
	
	public void setMacro(Macro macro) {
		if(allowsMacros()) {
			this.macro = macro;
		}
	}

	public Macro getMacro() {
		return macro;
	}
	
	public void playMacro() {
		if(macro != null) {
			if(!macro.isPlaying()) {
				setLockedDirection(EBHelper.getPlayerDirection(EBHelper.getPlayer()));
				macro.play();
			} else {
				macro.stop();
			}
		}
	}

	protected void addInstruction(IInstruction instruction) {
		if(macro != null && instruction != null) {
			macro.addInstruction(instruction);
		}
	}
	
	public void render(float partialTicks) {
		if(!isGhostPlaced()) { return; }
		
		float size = 1.1f;
		double[] min = new double[] { 0, 0, 0 };
		double[] max = new double[] { size, size, size };
		
        float centering = (size - 1.0f) / 2.0f;
		
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glPushMatrix();
		glTranslatef(x - centering, y - centering, z - centering);
		applyWorldTranslation(partialTicks);

		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		tess.setColorRGBA_F(DEFAULT_COLOUR[0], DEFAULT_COLOUR[1], DEFAULT_COLOUR[2], DEFAULT_COLOUR[3]);
		
		renderTopFace(min, max);
		renderBottomFace(min, max);
		renderFrontFace(min, max);
		renderBackFace(min, max);
		renderLeftFace(min, max);
		renderRightFace(min, max);
		
		tess.draw();
		
		glColor3f(0.0f, 0.0f, 0.0f);
		glBegin(GL_LINE_STRIP);
		renderTopFaceOutline(min, max);
		renderBottomFaceOutline(min, max);
		renderFrontFaceOutline(min, max);
		renderBackFaceOutline(min, max);
		renderLeftFaceOutline(min, max);
		renderRightFaceOutline(min, max);
		glEnd();
		
		glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glPopMatrix();
	}

	protected final void applyWorldTranslation(float partialTicks) {
		EntityPlayer player = EBHelper.getPlayer();
		
		double xOffset = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double yOffset = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double zOffset = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        
        glTranslated(-xOffset, -yOffset, -zOffset);
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
	
	protected final Vec3 relativeToAbsoluteDirection(Vec3 forward, Direction direction) {
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
	
	protected void renderTopFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 1.0f, 0.0f);
		t.addVertex(min[0], max[1], min[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], max[1], min[2]);
	}
	
	protected void renderTopFaceOutline(double[] min, double[] max) {
		glVertex3d(min[0], max[1], min[2]);
		glVertex3d(min[0], max[1], max[2]);
		glVertex3d(max[0], max[1], max[2]);
		glVertex3d(max[0], max[1], min[2]);
		glVertex3d(min[0], max[1], min[2]);
	}
	
	protected void renderBottomFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, -1.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(min[0], min[1], max[2]);
	}
	
	protected void renderBottomFaceOutline(double[] min, double[] max) {
		glVertex3d(min[0], min[1], min[2]);
		glVertex3d(max[0], min[1], min[2]);
		glVertex3d(max[0], min[1], max[2]);
		glVertex3d(min[0], min[1], max[2]);
		glVertex3d(min[0], min[1], min[2]);
	}
	
	protected void renderFrontFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, 1.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(min[0], max[1], min[2]);
	}
	
	protected void renderFrontFaceOutline(double[] min, double[] max) {
		glVertex3d(min[0], min[1], min[2]);
		glVertex3d(min[0], min[1], max[2]);
		glVertex3d(min[0], max[1], max[2]);
		glVertex3d(min[0], max[1], min[2]);
		glVertex3d(min[0], min[1], min[2]);
	}
	
	protected void renderBackFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, -1.0f);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
	}
	
	protected void renderBackFaceOutline(double[] min, double[] max) {
		glVertex3d(max[0], min[1], min[2]);
		glVertex3d(max[0], max[1], min[2]);
		glVertex3d(max[0], max[1], max[2]);
		glVertex3d(max[0], min[1], max[2]);
		glVertex3d(max[0], min[1], min[2]);
	}
	
	protected void renderLeftFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
	}
	
	protected void renderLeftFaceOutline(double[] min, double[] max) {
		glVertex3d(min[0], min[1], max[2]);
		glVertex3d(max[0], min[1], max[2]);
		glVertex3d(max[0], max[1], max[2]);
		glVertex3d(min[0], max[1], max[2]);
		glVertex3d(min[0], min[1], max[2]);
	}
	
	protected void renderRightFace(double[] min, double[] max) {
		double[][] minMax = correctMinMax(min, max);
		
		min = minMax[0];
		max = minMax[1];
		
		Tessellator t = Tessellator.instance;
		
		t.setNormal(-1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], max[1], min[2]);
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(max[0], min[1], min[2]);
	}
	
	protected void renderRightFaceOutline(double[] min, double[] max) {
		glVertex3d(min[0], min[1], min[2]);
		glVertex3d(min[0], max[1], min[2]);
		glVertex3d(max[0], max[1], min[2]);
		glVertex3d(max[0], min[1], min[2]);
		glVertex3d(min[0], min[1], min[2]);
	}
	
	private double[][] correctMinMax(double[] min, double[] max) {
		double[][] minMax = { new double[3], new double[3] };
		
		minMax[0][0] = Math.min(min[0], max[0]);
		minMax[0][1] = Math.min(min[1], max[1]);
		minMax[0][2] = Math.min(min[2], max[2]);
		
		minMax[1][0] = Math.max(min[0], max[0]);
		minMax[1][1] = Math.max(min[1], max[1]);
		minMax[1][2] = Math.max(min[2], max[2]);
		
		return minMax;
	}
}
