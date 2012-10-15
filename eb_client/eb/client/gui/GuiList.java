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
import net.minecraft.src.Tessellator;

@SideOnly(Side.CLIENT)
public class GuiList extends Gui {
	private Minecraft mc;
	private ArrayList<GuiListItem> items;
	private GuiScreen parent;
	private int x, y, width, height, padding;
	private GuiListItem hover, selected;
	private GuiScrollbar scrollbar;
	private int containedHeight;
	
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
		this.containedHeight = 0;
		this.scrollbar = new GuiScrollbar(mc, x + width - 6, y, 6, height);
		this.scrollbar.setContainedHeight(containedHeight);
	}
	
	public void addItem(GuiListItem item) {
		items.add(item);
		
		containedHeight += item.getHeight() + padding;
		scrollbar.setContainedHeight(containedHeight);
		
		System.out.println(containedHeight);
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void draw() {
		this.drawRect(x, y, x + width - scrollbar.getWidth(), y + height, Integer.MIN_VALUE);
		
		int itemWidth = width - (padding * 2) - scrollbar.getWidth();
		int currentY = y + padding;
		for(GuiListItem item : getVisibleItems()) {
			if(currentY + item.getHeight() >= (y + height - padding)) {
				break;
			}
			
			item.draw(this, x + padding, currentY, itemWidth);
			currentY += item.getHeight();
		}
		
		scrollbar.draw();
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
		if(GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height)) {
			int itemWidth = width - (padding * 2) - scrollbar.getWidth();
			int currentY = y + padding;
			for(GuiListItem item : items) {
				if(GuiHelper.pointInRect(mouseX, mouseY, x + padding, currentY, itemWidth, item.getHeight())) {
					setHoverItem(item);
					break;
				}
				
				currentY += item.getHeight();
				
				if(items.get(items.size() - 1).equals(item)) {
					setHoverItem(null);
				}
			}
		} else {
			setHoverItem(null);
		}
		
		scrollbar.mouseMoved(mouseX, mouseY);
	}

	public int getHeight() {
		return height;
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
	
	private ArrayList<GuiListItem> getVisibleItems() {
		ArrayList<GuiListItem> visibleItems = new ArrayList<GuiListItem>();
		
		int currentY = 0;
		for(GuiListItem item : items) {
			if(currentY > scrollbar.getScroll()) {
				visibleItems.add(item);
			}
			
			currentY += item.getHeight();
			if(currentY > scrollbar.getScroll() + height) {
				break;
			}
		}
		
		return visibleItems;
	}
}
