package eb.client;

import java.util.EnumSet;

import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.World;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import eb.common.Constants;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class GhostBlockRenderer implements ITickHandler {
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.RENDER)) {
			//GhostBlockHandler.instance().render();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return Constants.MOD_NAME + ".render";
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
