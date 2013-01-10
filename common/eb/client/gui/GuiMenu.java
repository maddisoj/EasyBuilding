package eb.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import eb.core.gui.GuiTabs;
import eb.core.gui.GuiWindow;
import eb.core.gui.GuiWindowDrawer.WindowPart;
import eb.core.handlers.GhostHandler;
import eb.core.mode.GhostMode;
import eb.core.mode.GhostModeManager;

public class GuiMenu extends GuiScreen {
	private HashMap<String, GuiScreen> buttons;
	private GuiWindow window;
	private int buttonHeight, buttonPadding;
	private GuiTabs tabs;
	
	public GuiMenu() {
		mc = Minecraft.getMinecraft();
		buttons = new HashMap<String, GuiScreen>();
		buttonHeight = 20;
		buttonPadding = 5;
	}
	
	@Override
	public void initGui() {
		window = new GuiWindow(100, getWindowHeight());
		window.togglePartEnabled(WindowPart.TOP_EDGE);
		window.togglePartEnabled(WindowPart.TOP_LEFT_CORNER);
		window.togglePartEnabled(WindowPart.TOP_RIGHT_CORNER);
		
		tabs = new GuiTabs();
		tabs.setWidth(window.getWidth());
		tabs.setHeight(15);
		tabs.setY(-15);
		tabs.setTabSelectListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tabChanged();
			}
		});
		
		tabs.addTab("Tools");
		tabs.addTab("Mode");
		
		window.addComponent(tabs);
		
		createToolsButtons();
	}
	
	public void addScreen(String name, GuiScreen screen) {
		buttons.put(name, screen);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {	
		window.mouseMoved(mouseX, mouseY);
		
		window.draw();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public void tabChanged() {
		if(tabs.getSelected().equals("Tools")) {
			controlList = new ArrayList();
			createToolsButtons();
		} else if(tabs.getSelected().equals("Mode")) {
			controlList = new ArrayList();
			createModeButtons();
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(tabs.getSelected().equals("Tools")) {
			if(button.enabled) {
				GuiScreen screen = buttons.get(button.displayString);
				mc.displayGuiScreen(screen);
			}
		} else if(tabs.getSelected().equals("Mode")) {
			if(button.enabled) { 
				GhostHandler.instance().setMode(button.displayString);
				//createModeButtons();
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		window.mouseClicked(mouseX, mouseY, button);
		super.mouseClicked(mouseX, mouseY, button);
    }
	
	private int getWindowHeight() {
		return ((2 * buttonPadding) + buttons.size() * (buttonHeight + buttonPadding));
	}
	
	private void createToolsButtons() {
		int id = 0;
		int x = window.getX() + buttonPadding;
		int y = window.getY() + buttonPadding;
		
		for(Entry<String, GuiScreen> entry : buttons.entrySet()) {
			controlList.add(new GuiButton(id++, x, y, window.getWidth() - 2 * buttonPadding, buttonHeight, entry.getKey()));
			y += buttonHeight + buttonPadding;
		}
		
		window.setHeight(y - window.getY());
	}
	
	private void createModeButtons() {
		int id = 0;
		int x = window.getX() + buttonPadding;
		int y = window.getY() + buttonPadding;
		
		for(GhostMode mode : GhostModeManager.instance()) {
			if(GhostHandler.instance().getMode() != mode) {
				controlList.add(new GuiButton(id++, x, y, window.getWidth() - 2 * buttonPadding, buttonHeight, mode.toString()));
				y += buttonHeight + buttonPadding;
			}
		}
		
		window.setHeight(y - window.getY());
	}
}
