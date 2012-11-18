package eb.client.macros.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import eb.client.gui.GuiWindow;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiMenu extends GuiScreen {
	private HashMap<GuiButton, GuiScreen> buttons;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	
	public GuiMenu(Minecraft mc) {		
		window = new GuiWindow(mc, 0, 0, 100, 20);
		buttons = new HashMap<GuiButton, GuiScreen>();
		buttonHeight = 20;
		buttonPadding = 5;
		
		initGui();
	}
	
	@Override
	public void initGui() {
		window.setLeft((width - window.getWidth()) / 2);
		window.setTop((height - window.getHeight()) / 2);
	}
	
	public void addScreen(String name, GuiScreen screen) {
		buttons.put(createButton(name), screen);
		
		updateWindowHeight();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.draw();
		
		for(Entry<GuiButton, GuiScreen> entry : buttons.entrySet()) {
			entry.getKey().drawButton(mc, mouseX, mouseY);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			GuiScreen screen = buttons.get(button);
			mc.displayGuiScreen(screen);
		}
	}
	
	private GuiButton createButton(String name) {
		int x = window.getLeft() + buttonPadding;
		int y = window.getTop() + buttonPadding;
		y += buttons.size() * (buttonHeight + buttonPadding);
		
		return new GuiButton(buttons.size() - 1, x, y, window.getWidth() - 2 * buttonPadding, buttonHeight, name);
	}
	
	private void updateWindowHeight() {
		if(window != null) {
			window.setHeight(buttons.size() * (buttonHeight + buttonPadding) + 2 * buttonPadding);
		}
	}
	
	private void updateButtonPositions() {
		Iterator it = controlList.iterator();
		
		int y = window.getTop() + buttonPadding;
		while(it.hasNext()) {
			GuiButton button = (GuiButton)it.next();
			
			button.xPosition = window.getLeft() + buttonPadding;
			button.yPosition = y;
			
			y += buttonHeight + buttonPadding;
		}
	}
}
