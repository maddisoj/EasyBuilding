package eb.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eb.core.gui.GuiHelper;
import eb.core.gui.GuiListItem;

/**
 * A list item representing a macro
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GuiFileItem extends Gui implements GuiListItem {
	private static final int MAX_STRING_LENGTH = 30;
	private String name, desc;
	private boolean mouseover, selected;
	private boolean loaded;
	
	public GuiFileItem(String name, String desc) {
		this.name = name;
		this.desc = desc;
		this.loaded = false;
	}
	
	@Override
	public int getHeight() {
		return 11;
	}

	@Override
	public void draw(int x, int y, int width) {
		if(mouseover || selected) {
			drawRect(x, y - 1, x + width, y + getHeight() - 2, Integer.MIN_VALUE);
		}
		
		FontRenderer font = GuiHelper.getFontRenderer();
		
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

	public String getDescription() {
		return desc;
	}

	public void setDescription(String description) {
		this.desc = description;
	}
	
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
}
