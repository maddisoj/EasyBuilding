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
	private boolean firstUse;
	private int startX, startY, startZ;
	
	public SelectionMode() {
		super();
		firstUse = true;
		startX = -1;
		startY = -1;
		startZ = -1;
	}
	
	@Override
	public void use() {
		if(firstUse) {
			startX = x;
			startY = y;
			startZ = z;
		} else {			
			EBHelper.printMessage("Selection made");
		}
		
		firstUse = !firstUse;
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
		if(startX == -1 || startY == -1 || startZ == -1 || 
		   (startX == x && startY == y && startZ == z)) {
			super.render(partialTicks);
		} else {
			int[][] range = getRange();
			int[] start = range[0];
			int[] end = range[1];			
			
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
	
	private int[][] getRange() {
		int[] start = { startX, startY, startZ };
		int[] end = { x + 1, y + 1, z + 1 };
		
		if(start[0] >= end[0]) {
			int temp = start[0];
			start[0] = end[0] - 1;
			end[0] = temp + 1;
		}
		
		if(start[1] >= end[1]) {
			int temp = start[1];
			start[1] = end[1] - 1;
			end[1] = temp + 1;
		}
		
		if(start[2] >= end[2]) {
			int temp = start[2];
			start[2] = end[2] - 1;
			end[2] = temp + 1;
		}
		
		return new int[][] { start, end };
	}
	
	public String toString() {
		return "Selection";
	}
}
