package eb.core.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import eb.core.Constants;

public class GuiWindow extends GuiComponent {
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
	private ArrayList<GuiComponent> components;
	
	public GuiWindow(int width, int height) {
		this(0, 0, width, height);
		centerOnScreen();
	}
	
	public GuiWindow(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		mc = FMLClientHandler.instance().getClient();
		components = new ArrayList<GuiComponent>();
	}
	
	@Override
	public void draw() {
		if(!isVisible()) { return; }
		
		int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "window.png");
		mc.renderEngine.bindTexture(texture);
		
		int edgeWidth = getEdgeWidth();
		int edgeHeight = getEdgeHeight();
		
		drawEdges();
		drawCorner(x, y, TOP_LEFT_CORNER);
		drawCorner(x + edgeWidth + getCornerSize(), y, TOP_RIGHT_CORNER);
		drawCorner(x + edgeWidth + getCornerSize(), y + edgeHeight + getCornerSize(), BOTTOM_RIGHT_CORNER);
		drawCorner(x, y + edgeHeight + getCornerSize(), BOTTOM_LEFT_CORNER);
		drawBackground();
		
		drawComponents();
	}
	
	@Override
	public void mouseMoved(int mouseX, int mouseY) {
		for(GuiComponent component : components) {
			component.mouseMoved(mouseX - x, mouseY - y);
		}
	}
	
	public void centerOnScreen() {
		ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		
		x = (res.getScaledWidth() - width) / 2;
		y = (res.getScaledHeight() - height) / 2;
	}
	
	public void addComponent(GuiComponent component) {
		components.add(component);
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
		
		drawTexturedRect(x + cornerSize, y, edgeWidth, cornerSize, TOP_EDGE[0], TOP_EDGE[1]);
		drawTexturedRect(x, y + cornerSize, cornerSize, edgeHeight, LEFT_EDGE[0], LEFT_EDGE[1]);
		drawTexturedRect(x + cornerSize, y + edgeHeight + cornerSize, edgeWidth, cornerSize, BOTTOM_EDGE[0], BOTTOM_EDGE[1]);
		drawTexturedRect(x + edgeWidth + cornerSize, y + cornerSize, cornerSize, edgeHeight, RIGHT_EDGE[0], RIGHT_EDGE[1]);
	}
	
	private void drawBackground() {
		int colour = GuiHelper.RGBtoInt(WINDOW_COLOUR[0], WINDOW_COLOUR[1], WINDOW_COLOUR[2]);
		drawRect(x + getCornerSize(), y + getCornerSize(),
				 x + getEdgeWidth() + getCornerSize(),  y + getEdgeHeight() + getCornerSize(), colour);
		
	}
	
	private void drawComponents() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
		
		for(GuiComponent component : components) {
			component.draw();
		}
		
		GL11.glPopMatrix();
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
