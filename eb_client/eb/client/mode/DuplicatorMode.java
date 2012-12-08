package eb.client.mode;

import net.minecraft.src.Vec3;
import eb.client.TileGhostBlock;
import static org.lwjgl.opengl.GL11.*;

public class DuplicatorMode extends GhostBlockMode {
	private int startX, startY, startZ;
	private Vec3 lastWorldPos, startWorldPos;
	
	public DuplicatorMode() {
		super();
		startX = -1;
		startY = -1;
		startZ = -1;
		lastWorldPos = Vec3.createVectorHelper(0, 0, 0);
		startWorldPos = null;
	}
	
	@Override
	public void use() {
		if(startX == -1 || startY == -1 || startZ == -1) {
			startX = x;
			startY = y;
			startZ = z;
			startWorldPos = lastWorldPos;
		} else {
			
		}
	}

	@Override
	public boolean allowsMacros() {
		return false;
	}

	public void render(TileGhostBlock ghost, double x, double y, double z)  {
		lastWorldPos.xCoord = x;
		lastWorldPos.yCoord = y;
		lastWorldPos.zCoord = z;
		
		if(startWorldPos != null) {
			glColor3f(1.0f, 0.0f, 0.0f);
			glBegin(GL_LINE_STRIP);
			
			glVertex3d(startWorldPos.xCoord, startWorldPos.yCoord, startWorldPos.zCoord);
			glVertex3d(x, startWorldPos.yCoord, startWorldPos.zCoord);
			glVertex3d(x, y, startWorldPos.zCoord);
			glVertex3d(startWorldPos.xCoord, y, startWorldPos.zCoord);
			
			glEnd();
		}
	}
}
