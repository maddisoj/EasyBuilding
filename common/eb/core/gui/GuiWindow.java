package eb.core.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import eb.core.Constants;

public class GuiWindow extends GuiComponent {	
	private Minecraft mc;
	private ArrayList<GuiComponent> components;
	
	public GuiWindow(int width, int height) {
		this(0, 0, width, height);
		centerOnScreen();
	}
	
	public GuiWindow(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		mc = FMLClientHandler.instance().getClient();
		components = new ArrayList<GuiComponent>();
	}
	
	@Override
	public void draw() {
		if(!isVisible()) { return; }
		
		int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "window.png");
		mc.renderEngine.bindTexture(texture);
		
		int edgeWidth = getEdgeWidth();
		int edgeHeight = getEdgeHeight();
		
		GuiHelper.drawEdges(x, y, edgeWidth, edgeHeight);
		GuiHelper.drawCorner(x, y, GuiHelper.TOP_LEFT_CORNER);
		GuiHelper.drawCorner(x + edgeWidth + getCornerSize(), y, GuiHelper.TOP_RIGHT_CORNER);
		GuiHelper.drawCorner(x + edgeWidth + getCornerSize(), y + edgeHeight + getCornerSize(), GuiHelper.BOTTOM_RIGHT_CORNER);
		GuiHelper.drawCorner(x, y + edgeHeight + getCornerSize(), GuiHelper.BOTTOM_LEFT_CORNER);
		drawBackground();
		
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
		ScaledResolution res = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		
		x = (res.getScaledWidth() - width) / 2;
		y = (res.getScaledHeight() - height) / 2;
	}
	
	public void addComponent(GuiComponent component) {
		components.add(component);
	}
	
	private void drawBackground() {
		int colour = GuiHelper.RGBtoInt(GuiHelper.BACKGROUND_COLOUR[0],
										GuiHelper.BACKGROUND_COLOUR[1],
										GuiHelper.BACKGROUND_COLOUR[2]);
		
		drawRect(x + getCornerSize(), y + getCornerSize(),
				 x + getEdgeWidth() + getCornerSize(),  y + getEdgeHeight() + getCornerSize(), colour);
		
	}
	
	private void drawComponents() {
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
		
		for(GuiComponent component : components) {
			component.draw();
		}
		
		GL11.glPopMatrix();
	}
	
	private int getCornerSize() {
		return GuiHelper.BACKGROUND_TEXTURE_SIZE / 2;
	}
	
	private int getEdgeWidth() {
		return width - GuiHelper.BACKGROUND_TEXTURE_SIZE;
	}
	
	private int getEdgeHeight() {
		return height - GuiHelper.BACKGROUND_TEXTURE_SIZE;
	}
}
