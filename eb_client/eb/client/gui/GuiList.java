package eb.client.gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;

/**
 * A list component
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiList<T extends GuiListItem> extends GuiComponent {
	private ArrayList<T> items;
	private GuiListItem hover, selected;
	private boolean drawBackground;
	private GuiScrollbar scrollbar;
	private int padding;
	
	public GuiList(int x, int y, int width, int height) {
		this.items = new ArrayList<T>();
		this.padding = 0;
		this.hover = null;
		this.selected = null;
		this.drawBackground = true;
		
		setDimensions(x, y, width, height);
		setupScrollbar();		
	}

	public void addItem(T item) {
		items.add(item);
		
		updateMaxValue();
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void setDrawBackground(boolean draw) {
		drawBackground = draw;
	}
	
	public void draw() {
		if(!isVisible()) { return; }
		
		if(drawBackground) {
			drawRect(x, y, x + width - scrollbar.getWidth(), y + height, Integer.MIN_VALUE);
		}
		
		int itemWidth = getItemWidth();
		int currentY = y + padding;
		
		for(int i = scrollbar.getValue(); i < items.size(); ++i) {
			GuiListItem item = items.get(i);
			
			if(currentY + item.getHeight() > y + height) {
				break;
			}
			
			item.draw(x + padding, currentY, itemWidth);
			currentY += item.getHeight();
		}
		
		scrollbar.draw();
	}
	
	public GuiListItem getSelected() {
		return selected;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(button == 0) {
			if(selected != null) {
				selected.setSelected(false);
			}
			
			if(hover != null) {
				if(hover.equals(selected)) {
					selected.setSelected(false);
					selected = null;
				} else {
					selected = hover;
					selected.setSelected(true);
				}
			}
		}
	}

	public void mouseMoved(int mouseX, int mouseY) {
		scrollbar.mouseMoved(mouseX, mouseY);
		setHoverItem(null);
		
		if(GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height)) {
			int itemWidth = getItemWidth();
			int currentY = y + padding;
			
			for(GuiListItem item : items) {
				if(GuiHelper.pointInRect(mouseX, mouseY, x + padding, currentY, itemWidth, item.getHeight())) {
					setHoverItem(item);
					break;
				}
				
				currentY += item.getHeight();
				
				if(currentY > y + height) {
					break;
				}
			}
		}
	}
	
	public void clear() {
		items = new ArrayList<T>();
	}

	private void setupScrollbar() {
		scrollbar = new GuiScrollbar(GuiScrollbar.VERTICAL, 1, 0, 0);
		scrollbar.setDimensions(x + width - 6, y, 6, height);
	}
	
	private void setHoverItem(GuiListItem item) {
		if(hover != null) {
			hover.setMouseOver(false);
		}
		
		hover = item;
		
		if(hover != null) {
			hover.setMouseOver(true);
		}
	}
	
	private int getItemWidth() {
		return width - (2 * padding) - scrollbar.getWidth();
	}
	
	private void updateMaxValue() {
		if(items.isEmpty()) {
			scrollbar.setMax(0);
			return;
		}
		
		//int itemsPerView = (height)
	}
}
