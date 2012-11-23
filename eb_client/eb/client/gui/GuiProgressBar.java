package eb.client.gui;

import org.lwjgl.opengl.GL11;

public class GuiProgressBar extends GuiComponent {
	private float progress;
	private GuiLabel label;
	
	public GuiProgressBar(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		label = new GuiLabel("", width / 2, height / 2);
		label.setCentered(true);
		label.setY(label.getY() - label.getHeight() / 2);
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public void draw() {
		if(!isVisible()) { return; }
		
		drawRect(x, y, x + width, y + height, GuiHelper.RGBtoInt(150, 150, 150));
		drawRect(x, y, (int)(x + (width * progress)), y + height, GuiHelper.RGBtoInt(0, 76, 13));
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0f);
		label.draw();
		GL11.glPopMatrix();
	}
}
