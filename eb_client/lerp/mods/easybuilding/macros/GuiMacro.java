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
	
	//I don't know what these arguments are for
	public void drawScreen(int a, int b, int c) {
		//drawRect(guiLeft, guiTop, guiLeft - guiWidth, guiTop - guiHeight, Integer.MIN_VALUE);
        //this.drawGradientRect(guiLeft, guiTop, guiWidth, guiHeight, 1615855616, -1602211792);
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);

        this.drawCenteredString(this.fontRenderer, "why isn't this displaying ):", this.width / 2, 40, 16777215);
		
		super.drawScreen(a, b, c);
	}
	
	public boolean doesGuiPauseGame() {
		return false;
	}
}
