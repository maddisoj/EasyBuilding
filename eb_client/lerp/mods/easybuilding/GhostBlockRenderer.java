package lerp.mods.easybuilding;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

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
		Tessellator t = Tessellator.instance;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		double[] min = new double[3];
		double[] max = new double[3];
		//made 0.01 units smaller to stop faces fighting
		min[0] = x + 0.01;
		min[1] = y + 0.01;
		min[2] = z + 0.01;
		max[0] = x + 1.0 - 0.01;
		max[1] = y + 1.0 - 0.01;
		max[2] = z + 1.0 - 0.01;
		t.startDrawingQuads();
		
		if(entity.getBlockId() == 0) {			
			t.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.5f);
		} else {
			t.setColorRGBA_F(1.0f, 0.0f, 0.0f, 0.9f);
		}
		
		renderTopFace(min, max);
		renderBottomFace(min, max);
		renderFrontFace(min, max);
		renderBackFace(min, max);
		renderLeftFace(min, max);
		renderRightFace(min, max);
		
		t.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
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
