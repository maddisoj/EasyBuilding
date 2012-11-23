package eb.client.gui;

import net.minecraft.src.FontRenderer;

public class GuiLabel extends GuiComponent {
	private FontRenderer fontRenderer;
	private String text;
	private boolean centered;
	private int red, green, blue;
	
	public GuiLabel(String text, int x, int y) {
		this.fontRenderer = GuiHelper.getFontRenderer();
		this.text = text;
		this.red = 255;
		this.green = 255;
		this.blue = 255;
		this.visible = true;
		this.centered = false;
		
		setDimensions(x, y, fontRenderer.getStringWidth(text), fontRenderer.FONT_HEIGHT);
	}
	
	public void setText(String text) {
		this.text = text;
		width = fontRenderer.getStringWidth(text);
	}
	
	public String getText() {
		return text;
	}
	
	public void setCentered(boolean centered) {
		this.centered = centered;
	}
	
	public void setColour(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public void draw() {
		if(!isVisible()) { return; }
		
		int colour = GuiHelper.RGBtoInt(red, green, blue);

		if(centered) {
			drawCenteredString(fontRenderer, text, x, y, colour);
		} else {
			fontRenderer.drawStringWithShadow(text, x, y, colour);
		}
	}
}
