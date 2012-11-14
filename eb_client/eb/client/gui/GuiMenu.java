package eb.client.gui;

import java.util.HashMap;

import net.minecraft.src.GuiScreen;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> screens;
	
	public GuiMenu() {
		screens = new HashMap<String, GuiScreen>();
	}
	
	public void addScreen(String name, GuiScreen screen) {
		screens.put(name, screen);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
	}
}
