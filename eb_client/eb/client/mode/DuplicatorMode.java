package eb.client.mode;

import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glVertex3d;
import eb.client.macros.instructions.UseInstruction;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;

public class DuplicatorMode extends GhostMode {
	private int startX, startY, startZ;
	
	public DuplicatorMode() {
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
		Tessellator tess = Tessellator.instance;
		tess.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.7f);
	}
}
