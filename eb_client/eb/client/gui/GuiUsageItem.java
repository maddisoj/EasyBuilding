package eb.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

public class GuiUsageItem implements GuiListItem {
	private static RenderItem renderer = new RenderItem();
	private ItemStack stack;
	
	public GuiUsageItem(Item item, int count) {
		stack = new ItemStack(item);
		stack.stackSize = count;
	}
	
	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void draw(GuiList parent, int x, int y, int width) {
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0F);
		GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		renderer.zLevel = 200.0F;
		renderer.renderItemIntoGUI(parent.getFontRenderer(), parent.getRenderEngine(), stack, x, y);
		renderer.renderItemOverlayIntoGUI(parent.getFontRenderer(), parent.getRenderEngine(), stack, x, y);
		renderer.zLevel = 0.0F;
		
		GL11.glPopMatrix();
	}

	@Override
	public void setMouseOver(boolean mouseover) {}

	@Override
	public void setSelected(boolean selected) {}
}
