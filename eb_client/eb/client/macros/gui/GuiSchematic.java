package eb.client.macros.gui;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import eb.client.GhostBlockHandler;
import eb.client.gui.GuiLabel;
import eb.client.gui.GuiList;
import eb.client.gui.GuiProgressBar;
import eb.client.gui.GuiWindow;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.client.macros.SchematicImporter;
import eb.client.macros.SchematicImporter.State;
import eb.common.Constants;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiSchematic extends GuiScreen implements Observer {
	private static final long SIZE_THRESHOLD = 7168L;
	
	private GuiWindow window;
	private GuiList files;
	private GuiButton importButton;
	private GuiLabel schematicName, sizeWarning;
	private GuiProgressBar progress;
	private GuiProcessDialog processDialog;
	private SchematicImporter importer;
	private long time;
	private boolean importing;
	private String importingName;
	private int windowPadding, componentPadding;
	
	public GuiSchematic() {
		importer = new SchematicImporter();
		time = 0;
		importing = false;
		windowPadding = 6;
		componentPadding = 2;
	}
	
	@Override
	public void initGui() {
		window = new GuiWindow(176, 166);
		
		final int x = window.getX();
		final int y = window.getY();
		final int w = window.getWidth();
		final int h = window.getHeight();

		final int buttonWidth = 100;
		final int buttonHeight = 20;
		
		files = new GuiList(mc, this, x + windowPadding, y + windowPadding,
									  w - 2 * windowPadding, h - (buttonHeight + componentPadding + 2 * windowPadding));
		files.setPadding(2);
		populateFilesList();	

		importButton = new GuiButton(0, x + (w / 2) - (buttonWidth / 2),
										files.getY() + files.getHeight() + componentPadding,
										buttonWidth, buttonHeight,
										"Import");
		controlList.add(importButton);
		
		sizeWarning = new GuiLabel("Warning: Large schematics can take a long time to import",
								   x + w / 2, y + h + componentPadding);
		sizeWarning.setCentered(true);
		sizeWarning.setVisible(false);
		sizeWarning.setColour(255, 0, 0);
		
		processDialog = new GuiProcessDialog("");
		
		if(importing) {
			setImporting(true);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		files.mouseMoved(mouseX, mouseY);
		
		if(importing) {
			processDialog.draw();
		} else {
			window.draw();
			files.draw();
			sizeWarning.draw();
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void updateScreen() {
		if(importing) {
			processDialog.setProgressText(getProgressString());
		}
		
		if(!importing && files.getSelected() != null) {
			GuiLoadableItem selected = getSelectedItem();
			long schematicSize = getSchematicSize(selected.getName());
			
			if(schematicSize > SIZE_THRESHOLD) {
				sizeWarning.setVisible(true);
			} else {
				sizeWarning.setVisible(false);
			}
		}
		
		super.updateScreen();
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		files.mouseClicked(x, y, button);		
		super.mouseClicked(x, y, button);
    }
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) {
				if(files.getSelected() != null) {
					GuiLoadableItem selected = getSelectedItem();
					importingName = selected.getName();
					importSchematic(selected.getName());
				}
			}
		}
	}
	
	private void setImporting(boolean importing) {
		if(importing) {
			this.importing = true;
			processDialog.setLabelText(importingName);
			importer.getMacro().addObserver(this);
		} else {
			this.importing = false;
			importer.getMacro().deleteObserver(this);
		}
	}
	
	private void importSchematic(String name) {
		final String path = Constants.SCHEMATICS_PATH + name + ".schematic";
				
		new Thread(new Runnable() {
			@Override
			public void run() {
				importer.importSchematic(path);
				setImporting(false);
				GhostBlockHandler.instance().sendMessage("Successfully imported " + importingName);
				GhostBlockHandler.instance().setMacro(importer.getMacro());
			}
		}).start();
		
		setImporting(true);
		time = System.currentTimeMillis();
	}

	private void populateFilesList() {
		File dir = new File(Constants.SCHEMATICS_PATH);
		
		if(dir.exists()) {
			files.clear();
			
			for(File file : dir.listFiles()) {
				if(getFileExtension(file.getName()).equals("schematic")) {
					files.addItem(new GuiLoadableItem(getFileName(file.getName()), ""));
				}
			}
		}
	}
	
	private String getFileName(String path) {
		int start = path.lastIndexOf('/');
		int end = path.lastIndexOf('.');
		
		if(start == -1) {
			start = 0;
		}
		
		if(end == -1) {
			end = path.length() - 1;
		}
		
		return path.substring(start, end);
	}
	
	private String getFileExtension(String path) {
		int start = path.lastIndexOf('.');
		
		if(start == -1) {
			return "";
		} else {
			return path.substring(start + 1);
		}
	}
	
	private String getProgressString() {
		String progress = "";
		
		if(importer.getState() == State.IMPORTING) {
			progress = "Importing";
		} else if(importer.getState() == State.OPTIMZING) {
			progress = "Optimizing";
		}
		
		long dt = System.currentTimeMillis() - time;
		
		if(dt > 333) {
			progress += ".";
		}
		
		if(dt > 666) {
			progress += ".";
		}
		
		if(dt > 999) {
			progress += ".";
		}
		
		if(dt > 1332) {
			time = System.currentTimeMillis();
		}
		
		return progress;
	}
	
	private long getSchematicSize(String name) {
		final String path = Constants.SCHEMATICS_PATH + name + ".schematic";
		
		return (new File(path)).length();
	}
	
	private GuiLoadableItem getSelectedItem() {
		return (GuiLoadableItem)files.getSelected();
	}

	@Override
	public void update(Observable observable, Object arg) {
		processDialog.setProgress((Float)arg);
	}
}
