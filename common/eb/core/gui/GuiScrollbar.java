package eb.core.gui;

import org.lwjgl.input.Mouse;

/**
 * A generic scrollbar
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GuiScrollbar extends GuiComponent {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	private int mode;
	private int amountScrolled;
	private int scrollbarLength;
	private boolean hover, selected;
	private int deltaMouse;
	private int step, min, max, value;
	
	public GuiScrollbar(int mode, int step, int min, int max) {
		this.mode = mode;
		this.amountScrolled = 0;
		this.scrollbarLength = 0;
		this.hover = false;
		this.selected = false;
		this.deltaMouse = -1;
		this.step = step;
		this.min = min;
		this.max = max;
		this.value = 0;
	}
	
	@Override
	public void setDimensions(int x, int y, int width, int height) {
		super.setDimensions(x, y, width, height);

		updateScrollbarLength();
	}
	
	public int getValue() {
		return value;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
		updateScrollbarLength();
	}
	
	public void draw() {
		if(!isVisible()) { return; }

		int[] rect = getScrollbarRect();

		if(selected || hover) {
			drawRect(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3], GuiHelper.RGBtoInt(26, 26, 26));
		} else {
			drawRect(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3], GuiHelper.RGBtoInt(76, 76, 76));
		}
	}
	
	public void scroll(int amount) { 
		amountScrolled += amount;
		
		amountScrolled = Math.max(0, amountScrolled);
		
		if(mode == HORIZONTAL) {
			amountScrolled = Math.min(width - scrollbarLength, amountScrolled);
		} else {
			amountScrolled = Math.min(height - scrollbarLength, amountScrolled);
		}
		
		updateValue();
	}
	
	public void mouseMoved(int mouseX, int mouseY) {
		int[] rect = getScrollbarRect();
		
		if(GuiHelper.pointInRect(mouseX, mouseY, rect[0], rect[1], rect[2], rect[3])) {
			hover = true;
			selected = Mouse.isButtonDown(0);
			
			if(selected) {
				if(mode == HORIZONTAL) {
					scroll(mouseX - deltaMouse);
				} else {
					scroll(mouseY - deltaMouse);
				}
			}
		} else {
			hover = false;
			selected = false;
		}
		
		if(mode == HORIZONTAL) {
			deltaMouse = mouseX;
		} else {
			deltaMouse = mouseY;
		}
	}
	
	private int[] getScrollbarRect() {
		if(mode == HORIZONTAL) {
			return new int[] { x + amountScrolled, y, scrollbarLength, height };
		} else {
			return new int[] { x, y + amountScrolled, width, scrollbarLength };
		}
	}
	
	private void updateScrollbarLength() {
		if(max == 0) {
			if(mode == HORIZONTAL) {
				scrollbarLength = width;
			} else {
				scrollbarLength = height;
			}
		} else {
			if(mode == HORIZONTAL) {
				scrollbarLength = (int)(width * (step / (float)max));
			} else {
				scrollbarLength = (int)(height * (step / (float)max));
			}
		}
	}
	
	private void updateValue() {
		int[] rect = getScrollbarRect();
		
		float t = 0;
		if(mode == HORIZONTAL) {
			t = (rect[0] / (float)width);
		} else {
			t = (rect[1] / (float)height);
		}
		
		value = min + (int)((max - min) * t);
	}
}
