package eb.client.macros.gui;

import java.util.HashMap;
import java.util.Iterator;

import eb.client.gui.GuiWindow;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> screens;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	
	public GuiMenu(Minecraft mc) {		
		window = new GuiWindow(mc, 0, 0, 100, 20);
		screens = new HashMap<String, GuiScreen>();
		buttonHeight = 20;
		buttonPadding = 5;
		
		initGui();
	}
	
	@Override
	public void initGui() {
		updateWindowHeight();
		
		int left = (width - window.getWidth()) / 2;
		int top = (height - window.getHeight()) / 2;
		
		window.setLeft(left);
		window.setTop(top);
		
		updateButtonPositions();
	}
	
	public void addScreen(String name, GuiScreen screen) {
		screens.put(name, screen);
		updateWindowHeight();
		
		GuiButton button = new GuiButton(screens.size() - 1, 0, 0, window.getWidth() - 2 * buttonPadding, buttonHeight, name);
		controlList.add(button);
		
		updateButtonPositions();
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
