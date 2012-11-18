package eb.client.gui;

import java.io.File;

import org.lwjgl.opengl.GL11;

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
	private static final int TEXTURE_SIZE = 16;
	private static final int WINDOW_COLOUR[] = { 198, 198, 198 };
	
	private Minecraft mc;
	private int left, top, width, height;
	
	public GuiWindow(Minecraft mc, int x, int y, int width, int height) {
		this.mc = mc;
		this.left = x;
		this.top = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw() {
		int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "window.png");
		mc.renderEngine.bindTexture(texture);
		
		int edgeWidth = getEdgeWidth();
		int edgeHeight = getEdgeHeight();
		
		drawEdges();
		drawCorner(left, top, TOP_LEFT_CORNER);
		drawCorner(left + edgeWidth + getCornerSize(), top, TOP_RIGHT_CORNER);
		drawCorner(left + edgeWidth + getCornerSize(), top + edgeHeight + getCornerSize(), BOTTOM_RIGHT_CORNER);
		drawCorner(left, top + edgeHeight + getCornerSize(), BOTTOM_LEFT_CORNER);
		drawBackground();
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() { 
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	public int getTop() {
		return top;
	}
	
	public void setTop(int top) {
		this.top = top;
	}
	
	private void drawTexturedRect(int x, int y, int width, int height, int[] u, int[] v) {        
        float uf[] = new float[] { (float)u[0] / (float)TEXTURE_SIZE, (float)u[1] / (float)TEXTURE_SIZE };
        float vf[] = new float[] { (float)v[0] / (float)TEXTURE_SIZE, (float)v[1] / (float)TEXTURE_SIZE };
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(uf[0], vf[0]); GL11.glVertex2i(x, y);
        GL11.glTexCoord2f(uf[0], vf[1]); GL11.glVertex2i(x, y + height);
        GL11.glTexCoord2f(uf[1], vf[1]); GL11.glVertex2i(x + width, y + height);
        GL11.glTexCoord2f(uf[1], vf[0]); GL11.glVertex2i(x + width, y);
        GL11.glEnd();
	}
	
	private void drawCorner(int x, int y, int uv[][]) {
		drawTexturedRect(x, y, getCornerSize(), getCornerSize(), uv[0], uv[1]);
	}
	
	private void drawEdges() {
		int cornerSize = getCornerSize();
		int edgeWidth = getEdgeWidth();
		int edgeHeight = getEdgeHeight();
		
		drawTexturedRect(left + cornerSize, top, edgeWidth, cornerSize, TOP_EDGE[0], TOP_EDGE[1]);
		drawTexturedRect(left, top + cornerSize, cornerSize, edgeHeight, LEFT_EDGE[0], LEFT_EDGE[1]);
		drawTexturedRect(left + cornerSize, top + edgeHeight + cornerSize, edgeWidth, cornerSize, BOTTOM_EDGE[0], BOTTOM_EDGE[1]);
		drawTexturedRect(left + edgeWidth + cornerSize, top + cornerSize, cornerSize, edgeHeight, RIGHT_EDGE[0], RIGHT_EDGE[1]);
	}
	
	private void drawBackground() {
		int colour = ((((255 << 8) + WINDOW_COLOUR[0]) << 8) + WINDOW_COLOUR[1] << 8) + WINDOW_COLOUR[2];
		drawRect(left + getCornerSize(), top + getCornerSize(),
				 left + getEdgeWidth() + getCornerSize(),  top + getEdgeHeight() + getCornerSize(), colour);
		
	}
	
	private static int getCornerSize() {
		return TEXTURE_SIZE / 2;
	}
	
	private int getEdgeWidth() {
		return width - TEXTURE_SIZE;
	}
	
	private int getEdgeHeight() {
		return height - TEXTURE_SIZE;
	}
}
