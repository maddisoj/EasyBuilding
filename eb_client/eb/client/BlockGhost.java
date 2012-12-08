package eb.client;

import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import eb.common.EBHelper;

/**
 * The block class for the Ghost cursor
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class BlockGhost extends BlockContainer {
	public BlockGhost(int id) {		
		super(id, Material.glass);

		enableStats = false;
		setBlockName("Ghost");
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        TileGhostBlock ghostBlock = EBHelper.getGhostBlock(world, x, y, z);
        if(ghostBlock == null) {
        	return null;
        }
        
        return ghostBlock.getContainedBoundingBox();
    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return GhostBlockRenderer.RENDER_ID;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileGhostBlock();
	}
	
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}
	
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
        return true;
    }
	
	/*@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 0;
    }*/
}
