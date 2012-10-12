package eb.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiAchievements;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiShareToLan;
import net.minecraft.src.GuiStats;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;
import net.minecraft.src.WorldClient;

@SideOnly(Side.CLIENT)
public class GuiMacro extends GuiScreen {
	private static final String MACRO_PATH = "/macros/";
	private int guiLeft, guiTop, guiWidth, guiHeight;
	private GuiList files;
	
	@Override
	public void initGui() {
		guiWidth = 176;
		guiHeight = 166;
		guiLeft = (width - guiWidth) / 2;
		guiTop = (height - guiHeight) / 2;
		
		files = new GuiList(mc, this, guiLeft + 6, guiTop + 6, guiWidth - 12, guiHeight - 24);
		files.setPadding(2);
		files.addItem(new GuiMacroItem("Test", "This is a test"));
		files.addItem(new GuiMacroItem("Test2", "This is a also test that is very long"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int texture = mc.renderEngine.getTexture("/eb/gui/background.png");
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
        files.draw();
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int button) {
		files.mouseClicked(x, y, button);
    }

	@Override
    protected void mouseMovedOrUp(int x, int y, int event) {
        if(event != 0) {
            files.mouseMoved(x, y);
        }
    }

}
