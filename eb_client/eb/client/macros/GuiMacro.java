package eb.client.macros;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
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
	
	public GuiMacro() {
		guiWidth = 250;
		guiHeight = 200;
		guiLeft = (width - guiWidth) / 2;
		guiTop = (height - guiHeight) / 2;
	}
	
	//I don't know what these arguments are for
	@Override
	public void drawScreen(int a, int b, float c) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int texture = mc.renderEngine.getTexture("/eb/gui/background.png");
        mc.renderEngine.bindTexture(texture);
        
        drawTexturedModalRect(0, 0, 0, 0, guiWidth, guiHeight);
		
		super.drawScreen(a, b, c);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
