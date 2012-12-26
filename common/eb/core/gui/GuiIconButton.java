package eb.core.gui;

import java.awt.event.ActionListener;

public class GuiIconButton extends GuiComponent {
	private String texture;
	private ActionListener onClick;

	@Override
	public void draw() {
		drawRect(x, y, x + width, y + height, GuiHelper.RGBtoInt(0, 0, 0));
		drawRect(x + 1, y + 1, x + width - 1, y + height - 1, GuiHelper.RGBtoInt(100, 100, 100));
	}
	
	public void setActionListener(ActionListener actionListener) {
		onClick = actionListener;
	}
}
