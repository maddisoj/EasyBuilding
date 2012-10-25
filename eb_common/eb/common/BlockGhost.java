package eb.common;

import java.util.Random;

import eb.client.GhostBlockRenderer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockGhost extends BlockContainer {
	public BlockGhost(int id) {		
		super(id, Material.glass);

		enableStats = false;
		setBlockName("Ghost");
		setCreativeTab(CreativeTabs.tabTools);
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        TileGhostBlock ghostBlock = Helper.getGhostBlock(world, x, y, z);
        if(ghostBlock == null) {
        	return null;
        }
        
        return ghostBlock.getContainedBoundingBox();
    }
	
	@Override
	public boolean isOpaqueCube() {
		return true;
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
	
	@Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return 15;
    }
}
