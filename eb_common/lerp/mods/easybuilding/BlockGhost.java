package lerp.mods.easybuilding;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
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
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
        return true;
    }
}
