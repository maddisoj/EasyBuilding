package eb.core.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderEngine;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiHelper {
	public static final int[][] TOP_LEFT_CORNER = new int[][] { { 0, 8 }, { 0, 8 }, };
	public static final int[][] TOP_RIGHT_CORNER = new int[][] { { 8, 16 }, { 0, 8 }, };
	public static final int[][] BOTTOM_LEFT_CORNER = new int[][] { { 0, 8 }, { 8, 16 }, };
	public static final int[][] BOTTOM_RIGHT_CORNER = new int[][] { { 8, 16 }, { 8, 16 }, };
	public static final int[][] TOP_EDGE = new int[][] { { 4, 5 }, { 0, 8 }, };
	public static final int[][] LEFT_EDGE = new int[][] { { 0, 8 }, { 4, 5 }, };
	public static final int[][] BOTTOM_EDGE = new int[][] { { 4, 5 }, { 8, 16 }, };
	public static final int[][] RIGHT_EDGE = new int[][] { { 8, 16 }, { 4, 5 }, };
	public static final int BACKGROUND_TEXTURE_SIZE = 16;
	public static final int BACKGROUND_COLOUR[] = { 198, 198, 198 };
	
	public static boolean pointInRect(int pointX, int pointY, int x, int y, int width, int height) {
		return (pointY > y && pointY < (y + height)
				&& pointX > x && pointX < (x + width));
	}
	
	public static int RGBtoInt(int red, int green, int blue) {
		return 255 << 24 | red << 16 | + green << 8 | blue;
	}
	
	public static FontRenderer getFontRenderer() {
		return FMLClientHandler.instance().getClient().fontRenderer;
	}
	
	public static RenderEngine getRenderEngine() {
		return FMLClientHandler.instance().getClient().renderEngine;
	}
	
	public static ScaledResolution getScaledResolution(int width, int height) {
		return new ScaledResolution(FMLClientHandler.instance().getClient().gameSettings, width, height);
	}
	
	private static void drawTexturedRect(int x, int y, int width, int height, int textureSize, int[] u, int[] v) {        
        float uf[] = new float[] { (float)u[0] / (float)textureSize, (float)u[1] / (float)textureSize };
        float vf[] = new float[] { (float)v[0] / (float)textureSize, (float)v[1] / (float)textureSize };
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(uf[0], vf[0]); GL11.glVertex2i(x, y);
        GL11.glTexCoord2f(uf[0], vf[1]); GL11.glVertex2i(x, y + height);
        GL11.glTexCoord2f(uf[1], vf[1]); GL11.glVertex2i(x + width, y + height);
        GL11.glTexCoord2f(uf[1], vf[0]); GL11.glVertex2i(x + width, y);
        GL11.glEnd();
	}
	
	public static void drawCorner(int x, int y, int[][] uv) {
		int cornerSize = BACKGROUND_TEXTURE_SIZE / 2;
		
		drawTexturedRect(x, y, cornerSize, cornerSize, BACKGROUND_TEXTURE_SIZE, uv[0], uv[1]);
	}
	
	public static void drawEdge(int x, int y, int width, int height, int[][] uv) {		
		drawTexturedRect(x, y, width, height, BACKGROUND_TEXTURE_SIZE, uv[0], uv[1]);
	}
	
	public static void drawEdges(int x, int y, int edgeWidth, int edgeHeight) {
		int cornerSize = BACKGROUND_TEXTURE_SIZE / 2;
		
		drawEdge(x + cornerSize, y, edgeWidth, cornerSize, TOP_EDGE);
		drawEdge(x, y + cornerSize, cornerSize, edgeHeight, LEFT_EDGE);
		drawEdge(x + cornerSize, y + edgeHeight + cornerSize, edgeWidth, cornerSize, BOTTOM_EDGE);
		drawEdge(x + edgeWidth + cornerSize, y + cornerSize, cornerSize, edgeHeight, RIGHT_EDGE);
	}
}
