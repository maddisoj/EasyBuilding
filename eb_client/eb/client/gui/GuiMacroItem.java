package eb.client.gui;

import net.minecraft.src.FontRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMacroItem implements GuiListItem {
	private static final int MAX_STRING_LENGTH = 30;
	private String name, desc;
	private boolean mouseover, selected;
	
	public GuiMacroItem(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	@Override
	public int getHeight() {
		return 11;
	}

	@Override
	public void draw(GuiList parent, int x, int y, int width) {
		if(mouseover || selected) {
			parent.drawRect(x, y - 1, x + width, y + getHeight() - 2, Integer.MIN_VALUE);
		}
		
		FontRenderer font = parent.getFontRenderer();
		
		String renderedName = name;
		if(font.getStringWidth(renderedName) > width) {
			renderedName = renderedName.substring(0, MAX_STRING_LENGTH - 3);
			renderedName += "...";
		}
		
		font.drawStringWithShadow(renderedName, x, y, Integer.MAX_VALUE);
	}

	@Override
	public void setMouseOver(boolean mouseover) {
		this.mouseover = mouseover;
	}
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}
}
