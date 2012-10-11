package lerp.mods.easybuilding.macros;

import net.minecraft.src.GuiScreen;

public class GuiMacro extends GuiScreen {
	private static final String MACRO_PATH = "./macros/";
	private int guiLeft, guiTop, guiWidth, guiHeight;
	
	public GuiMacro() {
		guiWidth = 200;
		guiHeight = 150;
		guiLeft = (width - guiWidth) / 2;
		guiTop = (height - guiHeight) / 2;
	}
}
