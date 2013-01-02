package eb.core;

import java.io.File;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.Loader;

/**
 * Easy access to mod-wide constants
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Constants {
	public static final String MOD_NAME = "EasyBuilding";
	public static final String VERSION = "1.2pre1.3";
	public static final String ROOT_PATH = new File(Loader.instance().getConfigDir().getParent()).toString() + File.separator;
	public static final String GUI_PATH = "/eb/gui/";
	public static final String BACKGROUND_PATH = GUI_PATH + "window.png";
	public static final String ICONS_PATH = GUI_PATH + "icons/";
	public static final String MACROS_PATH = Minecraft.getMinecraftDir() + "macros/";//ROOT_PATH + "macros";
	public static final String SCHEMATICS_PATH = Minecraft.getMinecraftDir() + "schematics/";//ROOT_PATH + "schematics" + File.separator;
}
