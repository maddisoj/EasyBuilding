package eb.client;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import eb.common.CommonProxy;
import eb.common.Constants;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class ClientProxy extends CommonProxy {
	public static BlockGhost ghostBlock;
	
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		Constants.GHOST_BLOCK_ID = config.getBlock("ghostBlock", 2008).getInt(2008);
		
		config.save();
	}
	
	public void init(FMLInitializationEvent event) {
		ghostBlock = new BlockGhost(Constants.GHOST_BLOCK_ID);
		GameRegistry.registerBlock(ghostBlock);
	}
	
	public void registerKeyBindings() {
		KeyBindingRegistry.registerKeyBinding(new GhostKeyHandler());
	}
	
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileGhostBlock.class, new GhostBlockRenderer());
	}
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TileGhostBlock.class, "tileGhostBlock");
	}
}
