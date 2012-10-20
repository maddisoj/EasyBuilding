package eb.client.gui;

import net.minecraft.src.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

public class GuiUsageItem implements GuiListItem {
	private ItemStack item;
	private int count;
	
	public GuiUsageItem(ItemStack item, int count) {
		this.item = item;
		this.count = count;
	}
	
	@Override
	public int getHeight() {
		return 20;
	}

	@Override
	public void draw(GuiList parent, int x, int y, int width) {
		IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.INVENTORY);
		
		renderer.renderItem(ItemRenderType.INVENTORY, item, (Object[])null);
	}

	@Override
	public void setMouseOver(boolean mouseover) {}

	@Override
	public void setSelected(boolean selected) {}
}
