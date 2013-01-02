package eb.core.gui;

import net.minecraft.client.renderer.RenderEngine;
import eb.core.Constants;

public class GuiWindowDrawer {
	public static final int BACKGROUND_TEXTURE_SIZE = 16;
	public static final int BACKGROUND_COLOUR = GuiHelper.RGBtoInt(198, 198, 198);
	public static final int CORNER_SIZE = BACKGROUND_TEXTURE_SIZE / 2;
	
	public enum WindowPart {
		TOP_LEFT_CORNER(new int[][] { { 0, 8 }, { 0, 8 }, }),
		TOP_EDGE(new int[][] { { 4, 5 }, { 0, 8 }, }),
		TOP_RIGHT_CORNER(new int[][] { { 8, 16 }, { 0, 8 }, }),
		LEFT_EDGE(new int[][] { { 0, 8 }, { 4, 5 }, }),
		BACKGROUND(new int[][] { { 4, 4 }, { 12, 12 }, }),
		RIGHT_EDGE(new int[][] { { 8, 16 }, { 4, 5 }, }),
		BOTTOM_LEFT_CORNER(new int[][] { { 0, 8 }, { 8, 16 }, }),
		BOTTOM_EDGE(new int[][] { { 4, 5 }, { 8, 16 }, }),
		BOTTOM_RIGHT_CORNER(new int[][] { { 8, 16 }, { 8, 16 }, });
		
		private int[][] uv;
		
		private WindowPart(int[][] uv) {
			this.uv = uv;
		}
		
		public int[][] getUVCoords() {
			return uv;
		}
	}
	
	private int x, y, width, height;
	private boolean[] drawParts;
	
	public GuiWindowDrawer(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.drawParts = new boolean[WindowPart.values().length];
		
		for(int i = 0; i < drawParts.length; ++i) {
			drawParts[i] = true;
		}
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void togglePartEnabled(WindowPart part) {
		drawParts[part.ordinal()] = !drawParts[part.ordinal()];
	}
	
	public boolean isPartEnabled(WindowPart part) {
		return drawParts[part.ordinal()];
	}
	
	public void draw() {	
		RenderEngine renderEngine = GuiHelper.getRenderEngine();		
		renderEngine.bindTexture(renderEngine.getTexture(Constants.BACKGROUND_PATH));
		
		drawCorners();
		drawEdges();
		drawBackground();
	}
	
	private void drawCorner(int cornerX, int cornerY, WindowPart part) {
		int[][] uv = part.getUVCoords();
		
		GuiHelper.drawTexturedRect(cornerX, cornerY, CORNER_SIZE, CORNER_SIZE, BACKGROUND_TEXTURE_SIZE, uv[0], uv[1]);
	}
	
	private void drawEdge(int edgeX, int edgeY, int edgeWidth, int edgeHeight, WindowPart part) {	
		int[][] uv = part.getUVCoords();
		
		GuiHelper.drawTexturedRect(edgeX, edgeY, edgeWidth, edgeHeight, BACKGROUND_TEXTURE_SIZE, uv[0], uv[1]);
	}
	
	private void drawBackground(int x, int y, int width, int height) {
		int[][] uv = WindowPart.BACKGROUND.getUVCoords();
		
		GuiHelper.drawTexturedRect(x, y, width, height, BACKGROUND_TEXTURE_SIZE, uv[0], uv[1]);
	}
	
	private void drawEdges() {
		int edgeWidth = width - 2 * CORNER_SIZE;
		int edgeHeight = height - 2 * CORNER_SIZE;
		
		if(isPartEnabled(WindowPart.TOP_EDGE)) {
			drawEdge(x + CORNER_SIZE, y, edgeWidth, CORNER_SIZE, WindowPart.TOP_EDGE);
		}
		
		if(isPartEnabled(WindowPart.LEFT_EDGE)) {
			int edgeY = y + CORNER_SIZE;
			int leftEdgeHeight = edgeHeight;
			
			if(!isPartEnabled(WindowPart.TOP_LEFT_CORNER)) {
				edgeY -= CORNER_SIZE;
				leftEdgeHeight += CORNER_SIZE;
			}
			
			if(!isPartEnabled(WindowPart.BOTTOM_LEFT_CORNER)) {
				leftEdgeHeight += CORNER_SIZE;
			}
			
			drawEdge(x, edgeY, CORNER_SIZE, leftEdgeHeight, WindowPart.LEFT_EDGE);
		}
		
		if(isPartEnabled(WindowPart.RIGHT_EDGE)) {
			int edgeY = y + CORNER_SIZE;
			int rightEdgeHeight = edgeHeight;

			if(!isPartEnabled(WindowPart.TOP_RIGHT_CORNER)) {
				edgeY -= CORNER_SIZE;
				rightEdgeHeight += CORNER_SIZE;
			}

			if(!isPartEnabled(WindowPart.BOTTOM_RIGHT_CORNER)) {
				rightEdgeHeight += CORNER_SIZE;
			}
			
			drawEdge(x + width - CORNER_SIZE, edgeY, CORNER_SIZE, rightEdgeHeight, WindowPart.RIGHT_EDGE);
		}
		
		if(isPartEnabled(WindowPart.BOTTOM_EDGE)) {
			drawEdge(x + CORNER_SIZE, y + height - CORNER_SIZE, edgeWidth, CORNER_SIZE, WindowPart.BOTTOM_EDGE);
		}
	}
	
	private void drawCorners() {
		int edgeWidth = width - 2 * CORNER_SIZE;
		int edgeHeight = height - 2 * CORNER_SIZE;
		
		if(isPartEnabled(WindowPart.TOP_LEFT_CORNER)) {
			drawCorner(x, y, WindowPart.TOP_LEFT_CORNER);
		}
		
		if(isPartEnabled(WindowPart.TOP_RIGHT_CORNER)) {
			drawCorner(x + edgeWidth + CORNER_SIZE, y, WindowPart.TOP_RIGHT_CORNER);
		}
		
		if(isPartEnabled(WindowPart.BOTTOM_RIGHT_CORNER)) {
			drawCorner(x + edgeWidth + CORNER_SIZE, y + edgeHeight + CORNER_SIZE, WindowPart.BOTTOM_RIGHT_CORNER);
		}
		
		if(isPartEnabled(WindowPart.BOTTOM_LEFT_CORNER)) {
			drawCorner(x, y + edgeHeight + CORNER_SIZE, WindowPart.BOTTOM_LEFT_CORNER);
		}
	}
	
	private void drawBackground() {
		int bgX = x + CORNER_SIZE;
		int bgY = y + CORNER_SIZE;
		int bgWidth = width - 2 * CORNER_SIZE;
		int bgHeight = height - 2 * CORNER_SIZE;
		
		if(!isPartEnabled(WindowPart.TOP_EDGE)) {
			bgY -= CORNER_SIZE;
			bgHeight += CORNER_SIZE;
		}
		
		if(!isPartEnabled(WindowPart.LEFT_EDGE)) {
			bgX -= CORNER_SIZE;
			bgWidth += CORNER_SIZE;
		}
		
		if(!isPartEnabled(WindowPart.RIGHT_EDGE)) {
			bgWidth += CORNER_SIZE;
		}
		
		if(!isPartEnabled(WindowPart.BOTTOM_EDGE)) {
			bgHeight += CORNER_SIZE;
		}
		
		drawBackground(bgX, bgY, bgWidth, bgHeight);
	}
}
