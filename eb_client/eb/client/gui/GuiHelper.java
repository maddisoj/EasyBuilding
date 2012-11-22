package eb.client.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.ScaledResolution;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

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
		return (((((255 << 8) + red) << 8) + green) << 8) + blue;
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
