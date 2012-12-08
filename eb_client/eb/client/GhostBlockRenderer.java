package eb.client;

import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GhostBlockRenderer extends TileEntitySpecialRenderer {
	public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
	private RenderBlocks renderer;
	
	public GhostBlockRenderer() {
		renderer = new RenderBlocks();
	}
	
	@Override
	public void onWorldChange(World world) {
		renderer.blockAccess = world;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
		render((TileGhostBlock)te, x, y, z, partialTick);
	}
	
	public void render(TileGhostBlock entity, double x, double y, double z, float partialTick) {		
		entity.render(x, y, z);
	}
	
	private void renderTopFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 1.0f, 0.0f);
		t.addVertex(min[0], max[1], min[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], max[1], min[2]);
	}
	
	private void renderBottomFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, -1.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], min[1], min[2]);
	}
	
	private void renderFrontFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, 1.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
		t.addVertex(min[0], max[1], min[2]);
	}
	
	private void renderBackFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(0.0f, 0.0f, -1.0f);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(max[0], max[1], min[2]);
	}
	
	private void renderLeftFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], max[2]);
		t.addVertex(max[0], min[1], max[2]);
		t.addVertex(max[0], max[1], max[2]);
		t.addVertex(min[0], max[1], max[2]);
	}
	
	private void renderRightFace(double[] min, double[] max) {
		Tessellator t = Tessellator.instance;
		
		t.setNormal(-1.0f, 0.0f, 0.0f);
		t.addVertex(min[0], min[1], min[2]);
		t.addVertex(max[0], min[1], min[2]);
		t.addVertex(max[0], max[1], min[2]);
		t.addVertex(min[0], max[1], min[2]);
	}
}
