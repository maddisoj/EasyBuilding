package eb.client.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

public class GuiLabel extends Gui {
	private FontRenderer fontRenderer;
	private String text;
	private boolean visible, centered;
	private int x, y;
	
	public GuiLabel(FontRenderer fontRenderer, String text, int x, int y) {
		this.fontRenderer = fontRenderer;
		this.text = text;
		this.x = x;
		this.y = y;
		this.visible = true;
		this.centered = false;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public void setX(int X) {
		x = X;
	}
	
	public void setY(int Y) {
		y = Y;
	}
	
	public void draw() {
		if(visible) {
			if(centered) {
				drawCenteredString(fontRenderer, text, x, y, Integer.MAX_VALUE);
			} else {
				fontRenderer.drawStringWithShadow(text, x, y, Integer.MAX_VALUE);
			}
		}
	}
}
