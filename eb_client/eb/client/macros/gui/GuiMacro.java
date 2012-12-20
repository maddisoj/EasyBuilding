package eb.client.macros.gui;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.ItemStack;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import eb.client.GhostBlockHandler;
import eb.client.gui.GuiList;
import eb.client.gui.GuiListItem;
import eb.client.gui.GuiScrollbar;
import eb.client.gui.GuiTextArea;
import eb.client.gui.GuiWindow;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.common.Constants;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiMacro extends GuiScreen {
	private GuiWindow window;
	private GuiList<GuiFileItem> files;
	private GuiList<GuiUsageItem> usageList;
	private GuiTextField macroName;
	private GuiTextArea macroDesc;
	private GuiButton saveButton, loadButton;
	private GuiListItem selected;
	private GuiScrollbar scrollbar;
	
	@Override
	public void initGui() {
		final int padding = 6;
		window = new GuiWindow(176, 166);
		
		files = new GuiList<GuiFileItem>(padding, padding,
										 window.getWidth() - 2 * padding, window.getHeight() - 60);
		files.setPadding(2);
		populateFilesList();
		window.addComponent(files);
		
		usageList = new GuiList<GuiUsageItem>(window.getWidth() + padding, padding,
											  48, window.getHeight() - padding);
		usageList.setPadding(2);
		usageList.setDrawBackground(false);
		usageList.setScrollbarMode(GuiList.OVERFLOW);
		populateUsageList();
		window.addComponent(usageList);
		
		macroName = new GuiTextField(fontRenderer, window.getX() + padding + 1,
												   window.getY() + files.getHeight() + 8,
												   window.getWidth() - 65, 11);
		macroName.setFocused(false);
		
		macroDesc = new GuiTextArea(fontRenderer, padding + 1, files.getHeight() + 22,
												  window.getWidth() - 65, 33);
		macroDesc.setFocused(false);
		macroDesc.setMaxLength(65);
		window.addComponent(macroDesc);
		
		saveButton = new GuiButton(0, window.getX() + macroDesc.getWidth() + (int)(1.5 * padding), window.getY() + files.getHeight() + 10,
									  50, 20, "Save");
		loadButton = new GuiButton(1, saveButton.xPosition, saveButton.yPosition + 22, 50, 20, "Load");
		
		controlList.add(saveButton);
		controlList.add(loadButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.mouseMoved(mouseX, mouseY);

		window.draw();
        macroName.drawTextBox();
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {
		GuiListItem item = files.getSelected();
		
		if(item != null && !item.equals(selected)) {
			selected = item;
			GuiFileItem macroItem = (GuiFileItem)item;
			
			if(!macroItem.isLoaded()) {
				Macro macro = getSelectedMacro();
				
				if(macro != null) {
					macroItem.setDescription(macro.getDescription());
					macroItem.setLoaded(true);
				}
			}
			
			macroName.setText(macroItem.getName());
			macroDesc.setText(macroItem.getDescription());
			
			populateUsageList();
		}
		
		if(MacroIO.macroExists(macroName.getText())) {
			saveButton.displayString = "Overwrite";
		} else {
			saveButton.displayString = "Save";
		}

		macroName.updateCursorCounter();
		macroDesc.updateCursorCounter();
		
		super.updateScreen();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void keyTyped(char key, int keyCode) {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			return;
		}
		
		if(macroName.isFocused()) {
			macroName.textboxKeyTyped(key, keyCode);
		} else if(macroDesc.isFocused()) {
			macroDesc.keyTyped(key, keyCode);
		}
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		files.mouseClicked(x, y, button);
		macroName.mouseClicked(x, y, button);
		macroDesc.mouseClicked(x, y, button);
		
		super.mouseClicked(x, y, button);
    }
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) { //Save button
				if(button.displayString.equals("Overwrite")) {
					((GuiFileItem)selected).setLoaded(false);
				}
				
				saveMacro(macroName.getText().trim(), macroDesc.getText().trim());
			} else if(button.id == 1) { //Load button
				loadMacro(macroName.getText().trim());
			}
		}
	}

	private void saveMacro(String name, String description) {
		if(name.length() > 0) {
			Macro macro = GhostBlockHandler.instance().getMacro();
			
			if(macro != null) {
				macro.setName(name);
				macro.setDescription(description);
				MacroIO.save(macro);
			}
			
			populateFilesList();
		}
	}
	
	private void loadMacro(String name) {
		GhostBlockHandler.instance().setMacro(macroName.getText());
	}
	
	private void populateFilesList() {
		File dir = new File(Constants.MACROS_PATH);
		
		if(dir.exists()) {
			files.clear();
			
			for(File file : dir.listFiles()) {
				files.addItem(new GuiFileItem(getMacroName(file.getName()), ""));
			}
		}
	}
	
	private void populateUsageList() {
		/*if(selected == null) {
			return;
		}
		
		Macro selectedMacro = getSelectedMacro();
		if(selectedMacro != null) {
			ArrayList<ItemStack> usage = selectedMacro.getBlockUsage();
			usageList.clear();
			
			for(ItemStack stack : usage) {
				usageList.addItem(new GuiUsageItem(stack));
			}
		} else {
			usageList.clear();
		}*/
	}
	
	private Macro getSelectedMacro() {
		GuiFileItem item = (GuiFileItem)selected;
		return MacroIO.load(item.getName());
	}
	
	private String getMacroName(String filename) {
		String name = filename.substring(0, filename.lastIndexOf('.'));
		name = name.replace("_", " ");
		
		return name;
	}
}
