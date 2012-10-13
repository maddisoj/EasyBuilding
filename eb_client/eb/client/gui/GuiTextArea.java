package eb.client.gui;

import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTextArea extends Gui {
	private FontRenderer fontRenderer;
	private int x, y, width, height;
	private String text;
	private int lines;
	private int charsPerLine;
	private boolean focused, enabled;
	private int cursorCounter;
	
	public GuiTextArea(FontRenderer fontRenderer, int x, int y, int width, int height) {
		this.fontRenderer = fontRenderer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = "";
		this.focused = false;
		this.enabled = true;
		this.cursorCounter = 0;
		
		lines = (int)Math.floor((double)(this.height - 2) / (double)fontRenderer.FONT_HEIGHT);
		charsPerLine = (int)Math.floor((double)(this.width - 2) / (double)fontRenderer.getCharWidth('W'));
	}
	
	public void updateCursorCounter() {
		++cursorCounter;
		
		if(cursorCounter > 1) { cursorCounter -= 2; }
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void keyTyped(char key, int keyCode) {
		if(focused && enabled) {
			System.out.println(key + " " + keyCode);
			
			switch(keyCode) {
				case 14: break; //backspace
				case 28: break; //enter
				case 54: break; //shift
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		focused = GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height);
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public void draw() {
		drawRect(x - 1, y - 1, x + width + 1, y + height + 1, -6250336);
        drawRect(x, y, x + width, y + height, -16777216);
        
        if(text.length() < charsPerLine) {
        	drawStringOnLine(text, 0);
        } else {
	        int pos = 0;
	        int currentLine = 0;
	        while(pos < text.length()) {
	        	String lineText;
	        	
	        	if(pos + charsPerLine >= text.length()) {
	        		lineText = text.substring(pos);
	        	} else {
	        		lineText = text.substring(pos, pos + charsPerLine);
	        	}
	        	
	        	drawStringOnLine(lineText, currentLine);
	        	
	        	++currentLine;
	        	pos += charsPerLine;
	        }
        }
	}
	
	private void drawStringOnLine(String str, int line) {
    	fontRenderer.drawStringWithShadow(str, x + 1, y + line * fontRenderer.FONT_HEIGHT + 1, Integer.MAX_VALUE);
	}
}
