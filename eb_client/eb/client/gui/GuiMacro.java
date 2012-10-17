package eb.client.gui;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import eb.client.GhostBlockHandler;
import eb.client.macros.Macro;
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
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;
import net.minecraft.src.WorldClient;

@SideOnly(Side.CLIENT)
public class GuiMacro extends GuiScreen {
	private int guiLeft, guiTop, guiWidth, guiHeight;
	private GuiList files;
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
		populateList();
		
		macroName = new GuiTextField(fontRenderer, guiLeft + 7, guiTop + files.getHeight() + 8, guiWidth - 60, 11);
		macroName.setFocused(false);
		
		macroDesc = new GuiTextArea(fontRenderer, guiLeft + 7, guiTop + files.getHeight() + 22, guiWidth - 60, 32);
		macroDesc.setFocused(false);
		macroDesc.setMaxLength(65);
		
		saveButton = new GuiButton(0, guiLeft + guiWidth - 50, guiTop + files.getHeight() + 10, 45, 20, "Save");
		loadButton = new GuiButton(1, guiLeft + guiWidth - 50, guiTop + files.getHeight() + 32, 45, 20, "Load");
		
		controlList.add(saveButton);
		controlList.add(loadButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int texture = mc.renderEngine.getTexture(Constants.GUI_PATH + "background.png");
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
        files.draw();
        macroName.drawTextBox();
        macroDesc.draw();
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {		
		GuiListItem item = files.getSelected();
		if(item != null && !item.equals(selected)) {
			selected = item;
			GuiMacroItem macroItem = (GuiMacroItem)item;
			
			if(!macroItem.isLoaded()) {
				Macro macro = GhostBlockHandler.instance().requestMacro(macroItem.getName());
				
				if(macro != null) {
					macroItem.setDescription(macro.getDescription());
				}
			}
			
			macroName.setText(macroItem.getName());
			macroDesc.setText(macroItem.getDescription());
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
				saveMacro(macroName.getText(), macroDesc.getText());
			} else if(button.id == 1) { //Load button
				loadMacro(macroName.getText());
			}
		}
	}

	private void saveMacro(String name, String description) {
		if(name.length() > 0) {
			if(GhostBlockHandler.instance().saveMacro(name, description)) {
				System.out.println("Saved");
			}
		}
	}
	
	private void loadMacro(String name) {
		//GhostBlockHandler.instance()
	}
	
	private void populateList() {
		File dir = new File(Constants.MACROS_PATH);
		
		if(dir.exists()) {
			for(File file : dir.listFiles()) {
				files.addItem(new GuiMacroItem(getMacroName(file.getName()), ""));
			}
		}
	}
	
	private String getMacroName(String filename) {
		String name = filename.substring(0, filename.lastIndexOf('.'));
		name.replace("_", " ");
		
		return name;
	}
}
