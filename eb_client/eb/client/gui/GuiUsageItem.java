package eb.client.gui;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderItem;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

public class GuiUsageItem implements GuiListItem {
	private static RenderItem renderer = new RenderItem();
	private ItemStack stack;
	private int count;
	
	public GuiUsageItem(Item item, int count) {
		this.stack = new ItemStack(item);
		this.count = count;
	}
	
	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void draw(GuiList parent, int x, int y, int width) {
		renderer.drawItemIntoGui(parent.getFontRenderer(), parent.getRenderEngine(), stack.itemID, 0, stack.getIconIndex(), x, y);
		parent.getFontRenderer().drawStringWithShadow(Integer.toString(count), x + 20, y + 4, Integer.MAX_VALUE);
	}

	@Override
	public void setMouseOver(boolean mouseover) {}

	@Override
	public void setSelected(boolean selected) {}
}
