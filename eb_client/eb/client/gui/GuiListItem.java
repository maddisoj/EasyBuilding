package eb.client.gui;

public interface GuiListItem {
	public int getHeight();
	public void draw(GuiList parent, int x, int y, int width);
	public void setMouseOver(boolean mouseover);
	public void setSelected(boolean selected);
}
