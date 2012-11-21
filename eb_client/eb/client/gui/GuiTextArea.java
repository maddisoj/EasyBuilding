package eb.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * A multi-line text area
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiTextArea extends GuiComponent {
	private static final int TEXT_COLOUR = GuiHelper.RGBtoInt(255, 255, 255);
	
	private FontRenderer fontRenderer;
	private String text;
	private int lines;
	private boolean focused, enabled;
	private int cursorCounter, cursorPosition;
	private int maxLength;
	
	public GuiTextArea(FontRenderer fontRenderer, int x, int y, int width, int height) {
		this.fontRenderer = fontRenderer;
		this.text = "";
		this.focused = false;
		this.enabled = true;
		this.cursorCounter = 0;
		this.cursorPosition = 0;
		this.maxLength = 100;
		
		setDimensions(x, y, width, height);
		
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
					removeAt(cursorPosition);
					break;
					
				case Keyboard.KEY_DELETE:
					removeAt(cursorPosition + 1);
					break;
					
				default:
					if(ChatAllowedCharacters.isAllowedCharacter(key)) {
						String strKey = Character.toString(key);
						if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
							strKey = strKey.toUpperCase();
						}
						
						insertCharacter(cursorPosition, strKey);
					}
					
					break;
			}
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int button) {
		setFocused(GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height));
		
		if(focused) {
			setCursorPosition(text.length());
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
		
		cursorPosition = Math.min(cursorPosition, text.length());
		cursorPosition = Math.max(cursorPosition, 0);
	}
	
	public void draw() {
		if(!isVisible()) { return; }
		
		drawRect(x - 1, y - 1, x + width + 1, y + height + 1, -6250336);
        drawRect(x, y, x + width, y + height, -16777216);
        
        if(text.length() == 0) {
        	drawCursor(0, 0);
        } else {
	        int pos = 0;
	        int currentLine = 0;
	        while(pos < text.length()) {
	        	String lineText = fontRenderer.trimStringToWidth(text.substring(pos), width - 5);
	
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
	}
	
	private void drawStringOnLine(String str, int line) {
    	fontRenderer.drawStringWithShadow(str, x + 4, y + line * fontRenderer.FONT_HEIGHT + 1, TEXT_COLOUR);
	}
	
	private void drawCursor(int lineStartPos, int line) {
		if(!focused || cursorPosition < lineStartPos || (cursorCounter / 6) % 2 != 0) { return; }
				
		int cursorX = x + 4;
		if(cursorPosition > 0 && text.length() > 0) {
			if(cursorPosition >= text.length()) {
				cursorX += fontRenderer.getStringWidth(text.substring(lineStartPos));
			} else {
				cursorX += fontRenderer.getStringWidth(text.substring(lineStartPos, cursorPosition));
			}
		}
		
		int cursorY = y + line * fontRenderer.FONT_HEIGHT + 2;
		
		fontRenderer.drawStringWithShadow("_", cursorX, cursorY, TEXT_COLOUR);
	}
	
	private void moveCursor(int direction) {
		if(direction < 0) {
			direction = -1;
		} else {
			direction = 1;
		}
		
		setCursorPosition(cursorPosition + direction);
	}
	
	private void removeAt(int position) {
		if(position < 1 || position > text.length()) { return; }
		
		String temp = text;
		text = temp.substring(0, position - 1);
		
		if(position < temp.length()) {
			text += temp.substring(position);
			setCursorPosition(cursorPosition - 1);
		} else {
			setCursorPosition(cursorPosition);
		}
	}
	
	private void insertCharacter(int position, String character) {
		if(text.length() != maxLength && position >= 0) {
			if(position > text.length()) {
				position = text.length();
			}
			
			String temp = text;
			text = temp.substring(0, position);
			text += character;
			text += temp.substring(position);
			
			setCursorPosition(cursorPosition + 1);
		}
	}
}
