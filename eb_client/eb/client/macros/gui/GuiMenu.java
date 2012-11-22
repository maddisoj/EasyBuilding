package eb.client.macros.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import eb.client.gui.GuiWindow;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> buttons;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	
	public GuiMenu(Minecraft mc) {
		buttons = new HashMap<String, GuiScreen>();
		buttonHeight = 20;
		buttonPadding = 5;
	}
	
	@Override
	public void initGui() {
		final int guiWidth = 100;
		final int guiHeight = getWindowHeight();
		final int guiLeft = (width - guiWidth) / 2;
		final int guiTop = (height - guiHeight) / 2;
		
		System.out.println(width);
		
		window = new GuiWindow(100, getWindowHeight());
		createButtons();
	}
	
	public void addScreen(String name, GuiScreen screen) {
		buttons.put(name, screen);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.draw();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			GuiScreen screen = buttons.get(button.displayString);
			mc.displayGuiScreen(screen);
		}
	}
	
	private int getWindowHeight() {
		return ((2 * buttonPadding) + buttons.size() * (buttonHeight + buttonPadding));
	}
	
	private void createButtons() {
		int id = 0;
		int x = window.getX() + buttonPadding;
		int y = window.getY() + (int)(1.5 * buttonPadding);
		
		for(Entry<String, GuiScreen> entry : buttons.entrySet()) {
			controlList.add(new GuiButton(id++, x, y, window.getWidth() - 2 * buttonPadding, buttonHeight, entry.getKey()));
			y += buttonHeight + buttonPadding;
		}
	}
}
