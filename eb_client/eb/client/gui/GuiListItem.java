package eb.client.gui;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public interface GuiListItem {
	public int getHeight();
	public void draw(GuiList parent, int x, int y, int width);
	public void setMouseOver(boolean mouseover);
	public void setSelected(boolean selected);
}
