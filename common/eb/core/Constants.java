package eb.core;

import java.io.File;

import net.minecraft.client.Minecraft;

/**
 * Easy access to mod-wide constants
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Constants {
	public static final String MOD_NAME = "EasyBuilding";
	public static final String VERSION = "1.2pre1.3";
	public static final String ROOT_PATH = Minecraft.getMinecraftDir().getAbsolutePath() + File.separator;
	public static final String GUI_PATH = "/eb/gui/";
	public static final String BACKGROUND_PATH = GUI_PATH + "window.png";
	public static final String ICONS_PATH = GUI_PATH + "icons/";
	public static final String MACROS_PATH = ROOT_PATH + "macros/";
	public static final String SCHEMATICS_PATH = ROOT_PATH + "schematics/";
	
	/* Key binding names */
	public static final String BN_PLACE = "Place Ghost";
	public static final String BN_FORWARD = "Move Ghost Forward";
	public static final String BN_BACKWARD = "Move Ghost Backward";
	public static final String BN_LEFT = "Move Ghost Left";
	public static final String BN_RIGHT = "Move Ghost Right";
	public static final String BN_UP = "Move Ghost Up";
	public static final String BN_DOWN = "Move Ghost Down";
	public static final String BN_USE = "Place Block";
	public static final String BN_RECORD = "Toggle Recording";
	public static final String BN_PLAY = "Play Macro";
	public static final String BN_MENU = "Open Menu";
}
