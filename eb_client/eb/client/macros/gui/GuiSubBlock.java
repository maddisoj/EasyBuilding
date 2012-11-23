package eb.client.macros.gui;

import java.util.ArrayList;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import eb.client.GhostBlockHandler;
import eb.client.gui.GuiGrid;
import eb.client.gui.GuiWindow;
import eb.client.macros.Macro;

public class GuiSubBlock extends GuiScreen {
	private GuiWindow window;
	private GuiGrid<GuiBlockItem> blocks;
	
	public GuiSubBlock() {
		window = new GuiWindow(176, 166);
	}
	
	@Override
	public void initGui() {
		final int padding = 6;
		
		blocks = new GuiGrid<GuiBlockItem>(5, padding, padding, window.getWidth() - 2 * padding, window.getHeight() - 2 * padding);
		blocks.setPadding(2);
		populateBlocksGrid();
		
		window.addComponent(blocks);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		window.draw();
	}
	
	private void populateBlocksGrid() {
		Macro macro = GhostBlockHandler.instance().getMacro();
		
		if(macro == null) {
			return;
		}
		
		ArrayList<ItemStack> usage = macro.getBlockUsage();
		for(ItemStack stack : usage) {
			blocks.addItem(new GuiBlockItem(stack));
		}
	}
}
