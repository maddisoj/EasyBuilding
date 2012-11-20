package eb.client.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

public class GuiLabel extends Gui {
	private FontRenderer fontRenderer;
	private String text;
	private boolean visible, centered;
	private int x, y, red, green, blue;
	
	public GuiLabel(FontRenderer fontRenderer, String text, int x, int y) {
		this.fontRenderer = fontRenderer;
		this.text = text;
		this.x = x;
		this.y = y;
		this.red = 255;
		this.green = 255;
		this.blue = 255;
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
	
	public void setColour(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void draw() {
		if(visible) {
			int colour = GuiHelper.RGBtoInt(red, green, blue);
			
			if(centered) {
				drawCenteredString(fontRenderer, text, x, y, colour);
			} else {
				fontRenderer.drawStringWithShadow(text, x, y, colour);
			}
		}
	}
}
