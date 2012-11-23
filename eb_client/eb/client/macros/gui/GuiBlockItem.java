package eb.client.macros.gui;

import net.minecraft.src.ItemStack;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;

import org.lwjgl.opengl.GL11;

import eb.client.gui.GuiGridItem;
import eb.client.gui.GuiHelper;

public class GuiBlockItem implements GuiGridItem {
	private static RenderItem renderer = new RenderItem();
	private ItemStack stack;
	
	public GuiBlockItem(ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public int getWidth() {
		return 20;
	}

	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void draw(int x, int y) {
		RenderHelper.enableGUIStandardItemLighting();
		
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0F);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		renderer.zLevel = 200.0F;
		renderer.renderItemIntoGUI(GuiHelper.getFontRenderer(), GuiHelper.getRenderEngine(), stack, x, y);
		renderer.zLevel = 0.0F;
		
		GL11.glPopMatrix();
		
		RenderHelper.disableStandardItemLighting();
	}
	
}
