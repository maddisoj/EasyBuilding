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
import net.minecraft.client.renderer.Tessellator;
import eb.core.mode.GhostMode;
import eb.macro.instruction.UseInstruction;

public class SelectionMode extends GhostMode {
	private int startX, startY, startZ;
	
	public SelectionMode() {
		super();
		startX = -1;
		startY = -1;
		startZ = -1;
	}
	
	@Override
	public UseInstruction use() {
		if(startX == -1 || startY == -1 || startZ == -1) {
			startX = x;
			startY = y;
			startZ = z;
		} else {
			
		}
		
		return null;
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}

	@Override
	public void render(float partialTicks)  {
		if(startX == -1 || startY == -1 || startZ == -1 ||
		   (x == startX && y == startY && z == startZ)) {
			super.render(partialTicks);
		} else {
			/*int[] start = new int[3];
			int[] end = new int[3];
			
			start[0] = Math.min(startX, x);
			start[1] = Math.min(startY, y);
			start[2] = Math.min(startZ, z);
			end[0] = Math.max(startX, x);
			end[1] = Math.max(startY, y);
			end[2] = Math.max(startZ, z);*/
			
			int[] start = { startX, startY, startZ };
			int[] end = { x, y, z };
			
			int width = end[0] - start[0];
			int height = end[1] - start[1];
			int length = end[2] - start[2];
			
			width = (width == 0 ? 1 : width);
			height = (height == 0 ? 1 : height);
			length = (length == 0 ? 1 : length);
			
			double[] min = { 0, 0, 0 };
			double[] max = { width, height, length };
			
			glDisable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glPushMatrix();
			glTranslatef(startX, startY, startZ);
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
	}
}
