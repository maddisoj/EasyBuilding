package eb.core.gui;

import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static org.lwjgl.opengl.GL11.*;

import net.minecraft.client.renderer.RenderEngine;
import eb.core.Constants;
import eb.core.gui.GuiWindowDrawer.WindowPart;

public class GuiTabs extends GuiComponent {
	private static final float INTENSITY_NOT_SELECTED = 150.0f / 255.0f;
	private static final float INTENSITY_MOUSE_OVER = 200.0f / 255.0f;
	private static final int PADDING = 5;
	
	private class Tab {
		public String label;
		public int x, y, width, height;
		public boolean isSelected;
		public boolean isMouseOver;
		
		public Tab(String label, int x, int y, int height) {
			this.label = label;
			this.x = x;
			this.y = y;
			this.width = getWidth();
			this.height = height;
			this.isSelected = false;
		}
		
		public void draw() {
			float intensity = 1.0f;
			
			windowDrawer.setWidth(width);
			windowDrawer.setX(x);
			
			if(!isSelected) {
				if(isMouseOver) {
					intensity = INTENSITY_MOUSE_OVER;
				} else {				
					intensity = INTENSITY_NOT_SELECTED;
				}
			}
			
			glColor4f(intensity, intensity, intensity, 1.0f);
			
			windowDrawer.draw();
			GuiHelper.getFontRenderer().drawStringWithShadow(label, x + PADDING, y + PADDING, GuiHelper.RGBtoInt(255, 255, 255));
		}
		
		public int getWidth() {
			return GuiHelper.getFontRenderer().getStringWidth(label) + 2 * PADDING;
		}
	}
	
	private GuiWindowDrawer windowDrawer;
	private ArrayList<Tab> tabs;
	private Tab selected, mouseOver;
	private ChangeListener tabSelectListener;
	
	public GuiTabs() {
		tabs = new ArrayList<Tab>();
		selected = null;
		mouseOver = null;
		
		windowDrawer = new GuiWindowDrawer(x, y, width, height);
		windowDrawer.togglePartEnabled(WindowPart.BOTTOM_LEFT_CORNER);
		windowDrawer.togglePartEnabled(WindowPart.BOTTOM_EDGE);
		windowDrawer.togglePartEnabled(WindowPart.BOTTOM_RIGHT_CORNER);
	}
	
	public void addTab(String label) {
		int tabX = 0;
		
		for(Tab tab : tabs) {
			tabX += tab.getWidth() + 1;
		}
		
		tabs.add(new Tab(label, tabX, y, height));
		
		if(selected == null) {
			setSelected(tabs.get(0));
		}
	}
	
	public void setY(int y) {
		super.setY(y);
		windowDrawer.setY(y);
	}
	
	public void setHeight(int height) {
		super.setHeight(height);
		windowDrawer.setHeight(height);
	}
	
	public String getSelected() {
		if(selected != null) {
			return selected.label;
		}
		
		return null;
	}
	
	@Override
	public void draw() {		
		for(Tab tab : tabs) {	
			if(!tab.isSelected) {
				tab.draw();
			}
		}		
		
		glDisable(GL_TEXTURE_2D);
		glLineWidth(2.0f);
		glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		glBegin(GL_LINES);
		glVertex2f(x, y + height - 0.5f);
		glVertex2f(x + width, y + height - 0.5f);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		
		if(selected != null) {
			selected.draw();
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(button == 0 && mouseOver != null) {
			if(selected != mouseOver) {
				setSelected(mouseOver);
				
				if(tabSelectListener != null) {
					tabSelectListener.stateChanged(new ChangeEvent(this));
				}
			}
		}
	}
	
	@Override
	public void mouseMoved(int mouseX, int mouseY) {
		if(!GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height)) {
			setMouseOver(null);
			return;
		}
		
		boolean found = false;
		
		for(Tab tab : tabs) {
			if(GuiHelper.pointInRect(mouseX, mouseY, tab.x, tab.y, tab.width, tab.height)) {
				setMouseOver(tab);
				found = true;
				break;
			}
		}
		
		if(!found) {
			setMouseOver(null);
		}
	}
	
	public void setTabSelectListener(ChangeListener listener) {
		this.tabSelectListener = listener;
	}
	
	private void setSelected(Tab newSelected) {
		if(selected != null) {
			selected.isSelected = false;
		}

		selected = newSelected;

		if(selected != null) {
			selected.isSelected = true;
		}
	}
	
	private void setMouseOver(Tab newMouseOver) {
		if(mouseOver != null) {
			mouseOver.isMouseOver = false;
		}
		
		mouseOver = newMouseOver;
		
		if(mouseOver != null) {
			mouseOver.isMouseOver = true;
		}
	}
}
