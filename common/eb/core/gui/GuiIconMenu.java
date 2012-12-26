package eb.core.gui;

import static org.lwjgl.opengl.GL11.glColor3f;
import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

public class GuiIconMenu extends GuiComponent {	
	private ArrayList<GuiIconButton> buttons;
	private GuiIconButton current;
	private boolean expanded;
	
	public GuiIconMenu(int x, int y, int width, int height) {
		super(x, y, width, height);
		buttons = new ArrayList<GuiIconButton>();
		current = null;
		expanded = false;
	}
	
	public void addIcon(GuiIconButton button) {
		buttons.add(button);
		
		if(current == null) {
			current = button;
		}
	}
	
	@Override
	public void draw() {
		int cornerSize = getCornerSize();
		int edgeWidth = getEdgeWidth();	
		
		GL11.glPushMatrix();
		if(expanded) {
			int tabOffset = 0;
			
			for(GuiIconButton button : buttons) {
				button.setX(x + tabOffset);
				button.setY(y);
				button.setHeight(height);
				button.draw();
				
				tabOffset += button.getWidth() - 1;
			}
			
			GL11.glTranslatef(tabOffset, 0.0f, 0.0f);
		}

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GuiHelper.drawEdge(x, y, edgeWidth, cornerSize, GuiHelper.TOP_EDGE);
		GuiHelper.drawEdge(x + width - cornerSize,  y + cornerSize, cornerSize, height - 2 * cornerSize, GuiHelper.RIGHT_EDGE);
		GuiHelper.drawEdge(x, y + height - cornerSize, edgeWidth, cornerSize, GuiHelper.BOTTOM_EDGE);
		GuiHelper.drawCorner(x + width - cornerSize, y, GuiHelper.TOP_RIGHT_CORNER);
		GuiHelper.drawCorner(x + width - cornerSize, y + height - cornerSize, GuiHelper.BOTTOM_RIGHT_CORNER);
		drawBackground();
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(button == 0) {			
			if(GuiHelper.pointInRect(mouseX, mouseY, x, y, width, height)) {
				expanded = !expanded;
			}
		}
	}
	
	private void drawBackground() {
		int colour = GuiHelper.RGBtoInt(GuiHelper.BACKGROUND_COLOUR[0],
										GuiHelper.BACKGROUND_COLOUR[1],
										GuiHelper.BACKGROUND_COLOUR[2]);
		
		drawRect(x, y + getCornerSize(), x + getEdgeWidth(),  y + getEdgeHeight() + getCornerSize(), colour);
		
	}
	
	private int getCornerSize() {
		return GuiHelper.BACKGROUND_TEXTURE_SIZE / 2;
	}
	
	private int getEdgeWidth() {
		return width - getCornerSize();
	}
	
	private int getEdgeHeight() {
		return height - getCornerSize() * 2;
	}
}
