package eb.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import eb.core.gui.GuiHelper;
import eb.core.gui.GuiListItem;

/**
 * A list item representing the amount of items used by a macro
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GuiUsageItem implements GuiListItem {
	private static RenderItem renderer = new RenderItem();
	private ItemStack stack;
	
	public GuiUsageItem(ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void draw(int x, int y, int width) {
		RenderHelper.enableGUIStandardItemLighting();
		
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0F);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		renderer.zLevel = 200.0F;
		renderer.renderItemIntoGUI(GuiHelper.getFontRenderer(), GuiHelper.getRenderEngine(), stack, x, y);
		renderer.renderItemOverlayIntoGUI(GuiHelper.getFontRenderer(), GuiHelper.getRenderEngine(), stack, x, y);
		renderer.zLevel = 0.0F;
		
		GL11.glPopMatrix();
		
		RenderHelper.disableStandardItemLighting();
	}

	@Override
	public void setMouseOver(boolean mouseover) {}

	@Override
	public void setSelected(boolean selected) {}
}
