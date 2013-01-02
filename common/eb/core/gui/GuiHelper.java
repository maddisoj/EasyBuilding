package eb.core.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
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
	public static boolean pointInRect(int pointX, int pointY, int x, int y, int width, int height) {
		return (pointY > y && pointY < (y + height)
				&& pointX > x && pointX < (x + width));
	}
	
	public static int RGBtoInt(int red, int green, int blue) {
		return 255 << 24 | red << 16 | + green << 8 | blue;
	}
	
	public static FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}
	
	public static RenderEngine getRenderEngine() {
		return Minecraft.getMinecraft().renderEngine;
	}
	
	public static ScaledResolution getScaledResolution(int width, int height) {
		return new ScaledResolution(Minecraft.getMinecraft().gameSettings, width, height);
	}
	
	public static void drawTexturedRect(int x, int y, int width, int height) {
        drawTexturedRect(x, y, width, height, 1, new int[] { 0, 1 }, new int[] { 0, 1 });
	}
	
	public static void drawTexturedRect(int x, int y, int width, int height, int textureSize, int[] u, int[] v) {        
        float uf[] = new float[] { (float)u[0] / (float)textureSize, (float)u[1] / (float)textureSize };
        float vf[] = new float[] { (float)v[0] / (float)textureSize, (float)v[1] / (float)textureSize };
        
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(uf[0], vf[0]); GL11.glVertex2i(x, y);
        GL11.glTexCoord2f(uf[0], vf[1]); GL11.glVertex2i(x, y + height);
        GL11.glTexCoord2f(uf[1], vf[1]); GL11.glVertex2i(x + width, y + height);
        GL11.glTexCoord2f(uf[1], vf[0]); GL11.glVertex2i(x + width, y);
        GL11.glEnd();
	}
}
