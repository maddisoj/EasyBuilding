package eb.client.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Gui;
import net.minecraft.src.Tessellator;

public class GuiScrollbar extends Gui {
	private Minecraft mc;
	private int x, y, width, height;
	private int amountScrolled;
	private int containedHeight;
	private int scrollbarHeight;
	private boolean hover, selected;
	private int lastMouseY;
	
	public GuiScrollbar(Minecraft mc, int x, int y, int width, int height) {
		this.mc = mc;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.containedHeight = height;
		this.amountScrolled = 0;
		this.scrollbarHeight = 0;
		this.hover = false;
		this.selected = false;
		this.lastMouseY = -1;
	}
	
	public void draw() {
		Tessellator tess = Tessellator.instance;
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		int left = x;
		int right = x + width;
		int top = y + amountScrolled;
		int bottom = y + amountScrolled + scrollbarHeight;
		
		if(selected || hover) {
			GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		} else {
			GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
		}
		
        tess.startDrawingQuads();
        tess.addVertex(right, top, 0.0);
        tess.addVertex(left, top, 0.0);
        tess.addVertex(left, bottom, 0.0);
        tess.addVertex(right, bottom, 0.0);
        tess.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void scroll(int amount) { 
		amountScrolled += amount;
		
		amountScrolled = Math.max(0, amountScrolled);
		amountScrolled = Math.min(height - scrollbarHeight, amountScrolled);
	}

	public void setContainedHeight(int containedHeight) {
		containedHeight = Math.max(height, containedHeight);
		this.containedHeight = containedHeight;
		
		scrollbarHeight = containedHeight;
		if(containedHeight != 0) {
			scrollbarHeight = (int)Math.floor(((float)height / (float)containedHeight) * (float)height);
		}
	}

	public int getWidth() {
		return width;
	}
	
	public void mouseMoved(int mouseX, int mouseY) {
		if(GuiHelper.pointInRect(mouseX, mouseY, x, y + amountScrolled, width, scrollbarHeight)) {
			hover = true;
			selected = Mouse.isButtonDown(0);
			
			if(selected) {
				scroll(mouseY - lastMouseY);
			}
		} else {
			hover = false;
			selected = false;
		}
		
		lastMouseY = mouseY;
	}

	public float getScroll() {
		return (float)amountScrolled / (float)height;
	}

	public float getLength() {
		return (float)scrollbarHeight / (float)height;
	}
}
