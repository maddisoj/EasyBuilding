package eb.core.gui;

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
		return FMLClientHandler.instance().getClient().fontRenderer;
	}
	
	public static RenderEngine getRenderEngine() {
		return FMLClientHandler.instance().getClient().renderEngine;
	}
	
	public static ScaledResolution getScaledResolution(int width, int height) {
		return new ScaledResolution(FMLClientHandler.instance().getClient().gameSettings, width, height);
	}
}
