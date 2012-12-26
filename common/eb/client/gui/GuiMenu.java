package eb.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import eb.core.gui.GuiIconButton;
import eb.core.gui.GuiIconMenu;
import eb.core.gui.GuiWindow;
import eb.core.mode.GhostMode;
import eb.core.mode.GhostModeManager;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> buttons;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	private GuiIconMenu modeSelectMenu;
	
	public GuiMenu(Minecraft mc) {
		buttons = new HashMap<String, GuiScreen>();
		buttonHeight = 20;
		buttonPadding = 5;
	}
	
	@Override
	public void initGui() {		
		window = new GuiWindow(100, getWindowHeight());
		
		modeSelectMenu = new GuiIconMenu(window.getWidth(), 3, 20, 20);
		
		for(GhostMode mode : GhostModeManager.instance()) {
			GuiIconButton button = new GuiIconButton();
			button.setWidth(20);
			button.setActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.out.println("Button clicked");
				}				
			});
			
			modeSelectMenu.addIcon(button);
		}
		
		window.addComponent(modeSelectMenu);
		
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
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		window.mouseClicked(mouseX, mouseY, button);
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
