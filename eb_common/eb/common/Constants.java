package eb.common;

import java.io.File;

import cpw.mods.fml.common.Loader;

/**
 * Easy access to constants
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class Constants {
	public static final String VERSION = "1.1";
	
	public static int GHOST_BLOCK_ID = 0;
	
	public static final String GUI_PATH = "/eb/gui/";
	public static final String MACROS_PATH = new File(Loader.instance().getConfigDir().getParent(), "macros").toString();
}
