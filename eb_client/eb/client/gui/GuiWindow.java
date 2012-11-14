package eb.client.gui;

import java.io.File;

import eb.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;

public class GuiWindow extends Gui {
	//UV coords of each window part
	private static final int[][] TOP_LEFT_CORNER = new int[][] { { 0, 8 }, { 0, 8 }, };
	private static final int[][] TOP_RIGHT_CORNER = new int[][] { { 8, 16 }, { 0, 8 }, };
	private static final int[][] BOTTOM_LEFT_CORNER = new int[][] { { 0, 8 }, { 8, 16 }, };
	private static final int[][] BOTTOM_RIGHT_CORNER = new int[][] { { 8, 16 }, { 8, 16 }, };
	private static final int[][] TOP_EDGE = new int[][] { { 4, 5 }, { 0, 8 }, };
	private static final int[][] LEFT_EDGE = new int[][] { { 0, 8 }, { 4, 5 }, };
	private static final int[][] BOTTOM_EDGE = new int[][] { { 4, 5 }, { 8, 16 }, };
	private static final int[][] RIGHT_EDGE = new int[][] { { 8, 16 }, { 4, 5 }, };
	
	private Minecraft mc;
	private int x, y, width, height, edgeWidth, edgeHeight;
	
	public GuiWindow(Minecraft mc, int x, int y, int width, int height) {
		this.mc = mc;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.edgeWidth = width - 16;
		this.edgeHeight = height - 16;
	}
	
	public void draw() {
		drawBackground();
	}
	
	private void drawBackground() {
		int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "window.png");
		mc.renderEngine.bindTexture(texture);
		
		drawCorner(x, y, TOP_LEFT_CORNER);
		drawCorner(x + edgeWidth, y, TOP_RIGHT_CORNER);
		drawCorner(x + edgeWidth, y + edgeHeight, BOTTOM_RIGHT_CORNER);
		drawCorner(x, y + edgeHeight, BOTTOM_LEFT_CORNER);
		
		drawEdges();
	}
	
	private void drawTexturedRect(int x, int y, int width, int height, int[] u, int[] v) {
		float uScale = 0.00390625F;
        float vScale = 0.00390625F;
        Tessellator tess = Tessellator.instance;
        
        tess.startDrawingQuads();
        tess.addVertexWithUV(x, y + height, this.zLevel, u[0] * uScale, v[1] * vScale);
        tess.addVertexWithUV(x + width, y + height, this.zLevel, u[1] * uScale, v[1] * vScale);
        tess.addVertexWithUV(x + width, y, this.zLevel, u[1] * uScale, v[0] * vScale);
        tess.addVertexWithUV(x, y, this.zLevel, u[0], v[0]);
        tess.draw();
	}
	
	private void drawCorner(int x, int y, int uv[][]) {
		drawTexturedRect(x, y, 8, 8, uv[0], uv[1]);
	}
	
	private void drawEdges() {
		int cornerSize = getCornerSize();
		drawTexturedRect(x + cornerSize, y, edgeWidth, cornerSize, TOP_EDGE[0], TOP_EDGE[1]);
		drawTexturedRect(x, y + cornerSize, cornerSize, edgeHeight, LEFT_EDGE[0], LEFT_EDGE[1]);
		drawTexturedRect(x + cornerSize, y + edgeHeight, edgeWidth, cornerSize, BOTTOM_EDGE[0], BOTTOM_EDGE[1]);
		drawTexturedRect(x + edgeWidth, y + cornerSize, cornerSize, edgeHeight, RIGHT_EDGE[0], RIGHT_EDGE[1]);
	}
	
	private static int getCornerSize() {
		return 8;
	}
}
