package eb.client.gui;

import java.io.File;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import eb.core.Constants;
import eb.core.EBHelper;
import eb.core.gui.GuiLabel;
import eb.core.gui.GuiList;
import eb.core.gui.GuiWindow;
import eb.core.handlers.GhostHandler;
import eb.macro.SchematicImporter;

public class GuiSchematic extends GuiScreen {
	private static final long SIZE_THRESHOLD = 7168L;
	
	private GuiWindow window;
	private GuiList files;
	private GuiButton importButton;
	private GuiLabel schematicName, sizeWarning;
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
		
		files = new GuiList(x + windowPadding, y + windowPadding,
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
			GuiFileItem selected = getSelectedItem();
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
					GuiFileItem selected = getSelectedItem();
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
			importer.getMacro().addObserver(processDialog);
		} else {
			this.importing = false;
			importer.getMacro().deleteObserver(processDialog);
		}
	}
	
	private void importSchematic(String name) {
		final String path = Constants.SCHEMATICS_PATH + name + ".schematic";
				
		new Thread(new Runnable() {
			@Override
			public void run() {
				importer.importSchematic(path);
				setImporting(true);
				importer.getMacro().optimize();
				setImporting(false);
				
				EBHelper.printMessage("Successfully imported " + importingName);
				GhostHandler.instance().setMacro(importer.getMacro());
			}
		}).start();
		
		time = System.currentTimeMillis();
	}

	private void populateFilesList() {
		File dir = new File(Constants.SCHEMATICS_PATH);
		
		if(dir.exists()) {
			files.clear();
			
			for(File file : dir.listFiles()) {
				if(getFileExtension(file.getName()).equals("schematic")) {
					files.addItem(new GuiFileItem(getFileName(file.getName()), ""));
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
		String progress = "Optimizing";
		
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
	
	private GuiFileItem getSelectedItem() {
		return (GuiFileItem)files.getSelected();
	}
}
