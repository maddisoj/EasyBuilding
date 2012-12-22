package eb.client.gui;

import java.util.Observable;
import java.util.Observer;

import eb.core.gui.GuiLabel;
import eb.core.gui.GuiProgressBar;
import eb.core.gui.GuiWindow;

public class GuiProcessDialog implements Observer {
	private GuiWindow window;
	private GuiLabel label;
	private GuiProgressBar progress;
	
	public GuiProcessDialog(String text) {
		window = new GuiWindow(176, 50);
		
		final int padding = 6;
		final int centerY = window.getY() + window.getHeight() / 2;
		
		label = new GuiLabel(text, window.getX() + window.getWidth() / 2,
								   centerY - 10);
		label.setCentered(true);
		progress = new GuiProgressBar(window.getX() + padding, centerY, window.getWidth() - 2 * padding, 10);
	}
	
	public void setLabelText(String text) {
		label.setText(text);
	}
	
	public void setProgressText(String text) {
		progress.setText(text);
	}
	
	public void setProgress(float progress) {
		this.progress.setProgress(progress);
	}
	
	public void draw() {
		window.draw();
		label.draw();
		progress.draw();
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg instanceof Float) {
			setProgress((Float)arg);
		}
	}
}
