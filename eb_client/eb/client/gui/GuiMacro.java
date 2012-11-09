package eb.client.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import eb.client.GhostBlockHandler;
import eb.client.macros.Macro;
import eb.client.macros.MacroIO;
import eb.common.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiShareToLan;
import net.minecraft.src.GuiStats;
import net.minecraft.src.GuiTextField;
import net.minecraft.src.GuiYesNo;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;
import net.minecraft.src.WorldClient;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiMacro extends GuiScreen {
	private int guiLeft, guiTop, guiWidth, guiHeight;
	private GuiList files, usageList;
	private GuiTextField macroName;
	private GuiTextArea macroDesc;
	private GuiButton saveButton, loadButton;
	private GuiListItem selected;
	
	@Override
	public void initGui() {
		guiWidth = 176;
		guiHeight = 166;
		guiLeft = (width - guiWidth) / 2;
		guiTop = (height - guiHeight) / 2;
		
		files = new GuiList(mc, this, guiLeft + 6, guiTop + 6, guiWidth - 12, guiHeight - 60);
		files.setPadding(2);
		populateFilesList();
		
		usageList = new GuiList(mc, this, guiLeft + guiWidth + 5, guiTop + 6, 48, guiHeight);
		usageList.setPadding(2);
		usageList.setDrawBackground(false);
		populateUsageList();
		
		macroName = new GuiTextField(fontRenderer, guiLeft + 7, guiTop + files.getHeight() + 8, guiWidth - 65, 11);
		macroName.setFocused(false);
		
		macroDesc = new GuiTextArea(fontRenderer, guiLeft + 7, guiTop + files.getHeight() + 22, guiWidth - 65, 32);
		macroDesc.setFocused(false);
		macroDesc.setMaxLength(65);
		
		saveButton = new GuiButton(0, guiLeft + guiWidth - 55, guiTop + files.getHeight() + 10, 50, 20, "Save");
		loadButton = new GuiButton(1, guiLeft + guiWidth - 55, guiTop + files.getHeight() + 32, 50, 20, "Load");
		
		controlList.add(saveButton);
		controlList.add(loadButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "background.png");
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
        files.draw();
        macroName.drawTextBox();
        macroDesc.draw();
        usageList.draw();
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {		
		GuiListItem item = files.getSelected();
		if(item != null && !item.equals(selected)) {
			selected = item;
			GuiMacroItem macroItem = (GuiMacroItem)item;
			
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
    protected void mouseMovedOrUp(int x, int y, int event) {
        if(event != 0) {
            files.mouseMoved(x, y);
        }
        
        super.mouseMovedOrUp(x, y, event);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 0) { //Save button
				if(button.displayString.equals("Overwrite")) {
					((GuiMacroItem)selected).setLoaded(false);
				}
				
				saveMacro(macroName.getText().trim(), macroDesc.getText().trim());
			} else if(button.id == 1) { //Load button
				loadMacro(macroName.getText().trim());
			}
		}
	}

	private void saveMacro(String name, String description) {
		if(name.length() > 0) {
			if(GhostBlockHandler.instance().saveMacro(name, description)) {
				populateFilesList();
			}
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
				files.addItem(new GuiMacroItem(getMacroName(file.getName()), ""));
			}
		}
	}
	
	private void populateUsageList() {
		if(selected == null) {
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
		}
	}
	
	private Macro getSelectedMacro() {
		GuiMacroItem item = (GuiMacroItem)selected;
		return MacroIO.load(item.getName());
	}
	
	private String getMacroName(String filename) {
		String name = filename.substring(0, filename.lastIndexOf('.'));
		name = name.replace("_", " ");
		
		return name;
	}
}
