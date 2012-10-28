package eb.common;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import eb.client.BlockGhost;
import eb.common.network.PacketEB;
import eb.common.network.PacketHandler;

/**
 * Main class for EasyBuilding
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@Mod(modid = "EasyBuilding", name = "Easy Building", version = Constants.VERSION)
@NetworkMod(channels = { "EasyBuilding" }, clientSideRequired = false, serverSideRequired = false, packetHandler = PacketHandler.class)
public class EasyBuilding {
	@Instance("EasyBuilding")
	public static EasyBuilding instance;	
	
	@SidedProxy(clientSide = "eb.client.ClientProxy", serverSide = "eb.common.CommonProxy")
	public static CommonProxy proxy;
	
	public static BlockGhost ghostBlock;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		Constants.GHOST_BLOCK_ID = config.getBlock("ghostBlock", 2008).getInt(2008);
		proxy.registerKeyBindings();
		
		config.save();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		ghostBlock = new BlockGhost(Constants.GHOST_BLOCK_ID);
		
		GameRegistry.registerBlock(ghostBlock);
		
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}
	
	public static void sendToPlayer(Player player, PacketEB packet) {
		proxy.sendToPlayer(player, packet);
	}
}
