package eb.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;

public abstract class GuiComponent extends Gui {
	protected int x, y, width, height;
	protected boolean visible;
	
	public GuiComponent() {
		this(0, 0, 0, 0);
	}
	
	public GuiComponent(int x, int y, int width, int height) {
		setDimensions(x, y, width, height);
		visible = true;
	}
	
	public void setDimensions(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void mouseMoved(int mouseX, int mouseY) {}
	
	public abstract void draw();
}
