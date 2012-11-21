package eb.client.gui;

import net.minecraft.src.Gui;

public class GuiProgressBar extends GuiComponent {
	private float progress;
	
	public GuiProgressBar(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}

	@Override
	public void draw() {
		if(!isVisible()) { return; }
		
		drawRect(x, y, x + width, y + height, GuiHelper.RGBtoInt(150, 150, 150));
		drawRect(x, y, (int)(x + (width * progress)), y + height, GuiHelper.RGBtoInt(50, 255, 50));
	}
}
