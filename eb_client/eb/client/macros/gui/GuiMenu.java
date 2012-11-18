package eb.client.macros.gui;

import java.util.HashMap;

import eb.client.gui.GuiWindow;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> screens;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	
	public GuiMenu() {
		initGui();
	}
	
	@Override
	public void initGui() {		
		int guiWidth = 100;
		int guiHeight = 20;
		int left = (width - guiWidth) / 2;
		int top = (height - guiHeight) / 2;
		
		screens = new HashMap<String, GuiScreen>();
		window = new GuiWindow(mc, left, top, guiWidth, guiHeight);
		buttonHeight = 20;
		buttonPadding = 5;
		
		updateWindowHeight();
	}
	
	public void addScreen(String name, GuiScreen screen) {
		screens.put(name, screen);
		updateWindowHeight();
		
		GuiButton button = new GuiButton(screens.size() - 1,
										 window.getLeft() + buttonPadding, window.getHeight() - buttonHeight - buttonPadding,
										 window.getWidth() - 2 * buttonPadding, buttonHeight, name);
		
		System.out.println("Adding button : " + name);
		controlList.add(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			GuiScreen screen = screens.get(button.displayString);
			mc.displayGuiScreen(screen);
		}
	}
	
	private void updateWindowHeight() {
		if(window != null) {
			window.setHeight(screens.size() * (buttonHeight + buttonPadding) + 2 * buttonPadding);
		}
	}
}
