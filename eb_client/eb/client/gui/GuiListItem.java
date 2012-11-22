package eb.client.gui;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Interface for items in the list
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public interface GuiListItem {
	public int getHeight();
	public void draw(int x, int y, int width);
	public void setMouseOver(boolean mouseover);
	public void setSelected(boolean selected);
}
