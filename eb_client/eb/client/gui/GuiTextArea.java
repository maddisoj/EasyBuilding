package eb.client.gui;

import org.lwjgl.input.Keyboard;

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
	private boolean focused, enabled;
	private int cursorCounter, cursorPosition;
	private int maxLength;
	
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
		this.cursorPosition = 0;
		this.maxLength = 100;
		
		lines = (int)Math.floor((double)(this.height - 2) / (double)fontRenderer.FONT_HEIGHT);
	}
	
	public void updateCursorCounter() {
		++cursorCounter;
	}
	
	public void setText(String text) {
		if(text.length() > maxLength) {
			this.text = text.substring(0, maxLength);
		} else {
			this.text = text;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setMaxLength(int length) {
		maxLength = length;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void keyTyped(char key, int keyCode) {
		if(focused && enabled) {			
			switch(keyCode) {
				case Keyboard.KEY_LEFT:
					moveCursor(-1);
					break;
				
				case Keyboard.KEY_RIGHT:
					moveCursor(1);
					break;
					
				case Keyboard.KEY_BACK:
					removeAtCursor();
					break;
			}
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		setFocused(GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height));
		
		if(focused) {
			cursorPosition = text.length();
		}
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
		cursorCounter = 0;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public void setCursorPosition(int pos) {
		cursorPosition = pos;
	}
	
	public void draw() {
		drawRect(x - 1, y - 1, x + width + 1, y + height + 1, -6250336);
        drawRect(x, y, x + width, y + height, -16777216);
        
        int pos = 0;
        int currentLine = 0;
        while(pos < text.length()) {
        	String lineText = fontRenderer.trimStringToWidth(text.substring(pos), width - 2);

        	drawStringOnLine(lineText, currentLine);
        	
        	//if the cursor lies on this line
        	if((pos <= cursorPosition && pos + lineText.length() > cursorPosition)
        	   || pos + lineText.length() >= text.length()) {
        		drawCursor(pos, currentLine);
        	}

        	++currentLine;
        	pos += lineText.length();

        	if(currentLine >= lines) {
        		break;
        	}
        }
	}
	
	private void drawStringOnLine(String str, int line) {
    	fontRenderer.drawStringWithShadow(str, x + 1, y + line * fontRenderer.FONT_HEIGHT + 1, Integer.MAX_VALUE);
	}
	
	private void drawCursor(int lineStartPos, int line) {
		if(!focused) { return; }
		if(cursorPosition < lineStartPos) { return; }
		if((cursorCounter / 6) % 2 != 0) { return; }
				
		int cursorX = x + 1;
		if(cursorPosition > 0) {
			if(cursorPosition >= text.length()) {
				cursorX += fontRenderer.getStringWidth(text.substring(lineStartPos));
			} else {
				cursorX += fontRenderer.getStringWidth(text.substring(lineStartPos, cursorPosition));
			}
		}
		
		int cursorY = y + line * fontRenderer.FONT_HEIGHT + 2;
		
		fontRenderer.drawStringWithShadow("_", cursorX, cursorY, Integer.MAX_VALUE);
	}
	
	private void moveCursor(int direction) {
		if(direction < 0) {
			direction = -1;
		} else {
			direction = 1;
		}
		
		cursorPosition += direction;
		cursorPosition = Math.min(cursorPosition, text.length());
		cursorPosition = Math.max(cursorPosition, 0);
	}
	
	private void removeAtCursor() {
		if(cursorPosition == 0) { return; }
		
		String temp = text;
		text = temp.substring(0, cursorPosition - 1);
		
		if(cursorPosition + 1 < text.length()) {
			text += temp.substring(cursorPosition + 1);
		}
	}
}
