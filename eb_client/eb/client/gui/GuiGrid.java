package eb.client.gui;

import java.util.ArrayList;

public class GuiGrid<T extends GuiGridItem> extends GuiComponent {
	private ArrayList<T> items;
	private int columns;
	private int padding;
	
	public GuiGrid(int columns, int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.items = new ArrayList<T>();
		this.columns = columns;
		this.padding = 0;
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void addItem(T item) {
		items.add(item);
	}

	@Override
	public void draw() {
		int currentY = y + padding;
		
		for(int i = 0; i < items.size(); ++i) {
			int rowHeight = 0;
			int currentX = x + padding;
			
			for(int column = 0; i < items.size() && column < columns; ++i, ++column) {
				GuiGridItem item = items.get(i);
				
				item.draw(currentX, currentY);
				
				currentX += item.getWidth();
				rowHeight = Math.max(rowHeight, item.getHeight());
			}
			
			currentY += rowHeight;
			
			if(currentY > y + height) {
				break;
			}
		}
	}
}
