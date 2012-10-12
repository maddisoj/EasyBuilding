package eb.client.gui;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSlot;
import net.minecraft.src.Tessellator;

public class GuiList extends Gui {
	private Minecraft mc;
	private ArrayList<GuiListItem> items;
	private GuiScreen parent;
	private int x, y, width, height, padding;
	private GuiListItem hover, selected;
	
	public GuiList(Minecraft mc, GuiScreen parent, int x, int y, int width, int height) {
		this.mc = mc;
		this.items = new ArrayList<GuiListItem>();
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.padding = 0;
		this.hover = null;
		this.selected = null;
	}
	
	public void addItem(GuiListItem item) {
		items.add(item);
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void draw() {
		this.drawRect(x, y, x + width, y + height, Integer.MIN_VALUE);
		
		int itemWidth = width - (padding * 2);
		int currentY = y + padding;
		for(GuiListItem item : items) {
			item.draw(this, x + padding, currentY, itemWidth);
			currentY += item.getHeight();
		}
	}
	
	public FontRenderer getFontRenderer() {
		return mc.fontRenderer;
	}
	
	public GuiListItem getSelected() {
		return selected;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(button == 0) {
			if(selected != null) {
				selected.setSelected(false);
			}
			
			selected = hover;
			selected.setSelected(true);
		}
	}

	public void mouseMoved(int mouseX, int mouseY) {
		if(pointInRect(mouseX, mouseY, x, y, width, height)) {
			
			int itemWidth = width - (padding * 2);
			int currentY = y + padding;;
			for(GuiListItem item : items) {
				if(pointInRect(mouseX, mouseY, x + padding, currentY, itemWidth, item.getHeight())) {
					if(hover != null) {
						hover.setMouseOver(false);
					}
					
					hover = item;
					hover.setMouseOver(true);
					break;
				}
			}	
		} else {
			if(hover != null) {
				hover.setMouseOver(false);
			}
		}
	}
	
	private boolean pointInRect(int pointX, int pointY, int x, int y, int width, int height) {
		return (pointY > y && pointY < (y + height)
				&& pointX > x && pointX < (x + width));
	}
}
