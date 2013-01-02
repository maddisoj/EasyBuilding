package eb.core.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import eb.core.Constants;
import eb.core.gui.GuiWindowDrawer.WindowPart;

public class GuiWindow extends GuiComponent {	
	private Minecraft mc;
	private ArrayList<GuiComponent> components;
	private GuiWindowDrawer windowDrawer;
	
	public GuiWindow(int width, int height) {
		this(0, 0, width, height);
		centerOnScreen();
	}
	
	public GuiWindow(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		mc = Minecraft.getMinecraft();
		components = new ArrayList<GuiComponent>();
		windowDrawer = new GuiWindowDrawer(x, y, width, height);
	}	

	public void setX(int x) {
		super.setX(x);
		windowDrawer.setX(x);
	}
	
	public void setY(int y) {
		super.setY(y);
		windowDrawer.setY(y);
	}
	
	public void setWidth(int width) {
		super.setWidth(width);
		windowDrawer.setWidth(width);
	}
	
	public void setHeight(int height) {
		super.setHeight(height);
		windowDrawer.setHeight(height);
	}
	
	public void togglePartEnabled(WindowPart part) {
		windowDrawer.togglePartEnabled(part);
	}
	
	@Override
	public void draw() {
		if(!isVisible()) { return; }
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		windowDrawer.draw();		
		drawComponents();
	}
	
	@Override
	public void mouseMoved(int mouseX, int mouseY) {
		for(GuiComponent component : components) {
			component.mouseMoved(mouseX - x, mouseY - y);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button){ 
		for(GuiComponent component : components) {
			component.mouseClicked(mouseX - x, mouseY - y, button);
		}
	}
	
	public void centerOnScreen() {
		ScaledResolution res = GuiHelper.getScaledResolution(mc.displayWidth, mc.displayHeight);
		
		setX((res.getScaledWidth() - width) / 2);
		setY((res.getScaledHeight() - height) / 2);
	}
	
	public void addComponent(GuiComponent component) {
		components.add(component);
	}
	
	private void drawComponents() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
		
		for(GuiComponent component : components) {
			component.draw();
		}
		
		GL11.glPopMatrix();
	}
}
