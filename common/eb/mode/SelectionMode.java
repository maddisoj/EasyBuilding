package eb.mode;

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
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import eb.core.Constants;
import eb.core.Direction;
import eb.core.EBHelper;
import eb.core.mode.GhostMode;
import eb.macro.instruction.UseInstruction;

public class SelectionMode extends GhostMode {
	private enum Mode {
		MOVE, RESIZE;
	}
	
	private Mode mode;
	private int[] start, end;
	private int startX, startY, startZ;
	
	public SelectionMode() {
		super();
		mode = Mode.MOVE;
		start = new int[3];
		end = new int[3];
		
		startX = -1;
		startY = -1;
		startZ = -1;
	}
	
	@Override
	public void setGhostPlaced(boolean placed) {
		super.setGhostPlaced(placed);
		
		if(placed) {
			start[0] = x;
			start[1] = y;
			start[2] = z;			
			end[0] = x;
			end[1] = y;
			end[2] = z;
		}
	}
	
	@Override
	public void use() {
		if(mode == Mode.MOVE) {
			mode = Mode.RESIZE;
		} else if(mode == Mode.RESIZE) {			
			mode = Mode.MOVE;
		}
	}
	
	@Override
	public void move(Direction direction) {	
		System.out.println(mode.name());
		Vec3 moveDirection = getAbsoluteMoveDirection(direction);
		
		if(mode == Mode.MOVE) {
			start[0] += moveDirection.xCoord;
			start[1] += moveDirection.yCoord;
			start[2] += moveDirection.zCoord;
			end[0] += moveDirection.xCoord;
			end[1] += moveDirection.yCoord;
			end[2] += moveDirection.zCoord;
		} else if(mode == Mode.RESIZE) {
			end[0] += moveDirection.xCoord;
			end[1] += moveDirection.yCoord;
			end[2] += moveDirection.zCoord;
			
			swapStartEnd();
		}
		
		System.out.println("(" + start[0] + " " + start[1] + " " + start[2] + ")");
		System.out.println("(" + end[0] + " " + end[1] + " " + end[2] + ")");
	}

	@Override
	public void setGhostPosition(int x, int y, int z) {
		int dx = this.x - x;
		int dy = this.y - y;
		int dz = this.z - z;
		
		super.setGhostPosition(x, y, z);
		end[0] += dx;
		end[1] += dy;
		end[2] += dz;
		
		super.setGhostPosition(x, y, z);
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}

	@Override
	public boolean repeatsUse() {
		return false;
	}

	@Override
	public void render(float partialTicks)  {
		if(start[0] == end[0] && start[1] == end[1] && start[2] == end[2]) {
			super.render(partialTicks);
		} else {			
			int width = end[0] - start[0];
			int height = end[1] - start[1];
			int length = end[2] - start[2];
			
			double padding = 0.05;
			double[] min = { -padding, -padding, -padding };
			double[] max = { width + padding, height + padding, length + padding };
			
			glDisable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glPushMatrix();
			glTranslatef(start[0], start[1], start[2]);
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
			renderLeftFaceOutline(min, max);
			renderRightFaceOutline(min, max);
			glEnd();
	
			glDisable(GL_BLEND);
			glEnable(GL_TEXTURE_2D);
			glPopMatrix();
		}
	}
	
	private void swapStartEnd() {
		if(start[0] > end[0]) {
			int temp = start[0];
			start[0] = end[0];
			end[0] = temp;
		}
		
		if(start[1] > end[1]) {
			int temp = start[1];
			start[1] = end[1];
			end[1] = temp;
		}
		
		if(start[2] > end[2]) {
			int temp = start[2];
			start[2] = end[2];
			end[2] = temp;
		}
	}
	
	public String toString() {
		return "Selection";
	}
}
