package eb.client.macros.gui;

import java.io.File;

import eb.client.GhostBlockHandler;
import eb.client.gui.GuiLabel;
import eb.client.gui.GuiList;
import eb.client.gui.GuiWindow;
import eb.client.macros.MacroIO;
import eb.client.macros.SchematicImporter;
import eb.client.macros.SchematicImporter.State;
import eb.common.Constants;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiSchematic extends GuiScreen {
	private static final long SIZE_THRESHOLD = 7168L;
	
	private GuiWindow window;
	private GuiList files;
	private GuiButton importButton;
	private GuiLabel schematicName, stateLabel, sizeWarning;
	private SchematicImporter importer;
	private long time;
	private boolean importing;
	private String importingName;
	
	public GuiSchematic() {
		importer = new SchematicImporter();
		time = 0;
		importing = false;
	}
	
	@Override
	public void initGui() {
		final int guiWidth = 176;
		final int guiHeight = 166;
		final int guiLeft = (width - guiWidth) / 2;
		final int guiTop = (height - guiHeight) / 2;
		final int padding = 6;
		
		window = new GuiWindow(mc, guiLeft, guiTop, guiWidth, guiHeight);
		files = new GuiList(mc, this, guiLeft + padding, guiTop + padding, guiWidth - 2 * padding, guiHeight - (23 + 2 * padding));
		files.setPadding(2);
		populateFilesList();
		
		final int buttonWidth = 100;
		final int buttonHeight = 20;
		importButton = new GuiButton(0, guiLeft + guiWidth / 2 - buttonWidth / 2,
										guiTop + guiHeight - (buttonHeight + padding),
										buttonWidth,
										buttonHeight,
										"Import");
		
		controlList.add(importButton);
		
		schematicName = new GuiLabel(fontRenderer, "", guiLeft + guiWidth / 2, guiTop + padding);
		schematicName.setCentered(true);
		stateLabel = new GuiLabel(fontRenderer, "", guiLeft + guiWidth / 2, guiTop + padding + 10);
		stateLabel.setCentered(true);
		sizeWarning = new GuiLabel(fontRenderer, "Warning: Large schematics can take a long time to import", guiLeft + guiWidth / 2, guiTop + guiHeight + 3);
		sizeWarning.setCentered(true);
		sizeWarning.setVisible(false);
		sizeWarning.setColour(255, 0, 0);
		
		if(importing) {
			setImporting(true);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		files.mouseMoved(mouseX, mouseY);
		
		window.draw();
		files.draw();
		schematicName.draw();
		stateLabel.draw();
		sizeWarning.draw();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {
		if(importing) {
			stateLabel.setText(getProgressString());
		}
		
		if(files.getSelected() != null) {
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
			importButton.drawButton = false;
			files.setVisible(false);
			schematicName.setText(importingName);
			schematicName.setVisible(true);
			stateLabel.setVisible(true);
			sizeWarning.setVisible(false);
			window.setHeight(50);
			this.importing = true;
		} else {
			importButton.drawButton = true;
			files.setVisible(true);
			schematicName.setVisible(false);
			stateLabel.setVisible(false);
			sizeWarning.setVisible(false);
			window.setHeight(166);
			this.importing = false;
		}
		
		window.setLeft((width - window.getWidth()) / 2);
		window.setTop((height - window.getHeight()) / 2);
		schematicName.setY(window.getTop() + window.getHeight() / 2 - 10);
		stateLabel.setY(window.getTop() + window.getHeight() / 2);
	}
	
	private void importSchematic(String name) {
		final String path = Constants.SCHEMATICS_PATH + name + ".schematic";
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				GhostBlockHandler.instance().setMacro(importer.importSchematic(path));
				setImporting(false);
				GhostBlockHandler.instance().sendMessage("Successfully imported " + importingName);
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
}
