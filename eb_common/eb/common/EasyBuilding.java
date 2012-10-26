package eb.common;

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

@Mod(modid = "EasyBuilding", name = "Easy Building", version = Constants.VERSION)
@NetworkMod(channels = { "EasyBuilding" }, clientSideRequired = false, serverSideRequired = false, packetHandler = PacketHandler.class)
public class EasyBuilding {
	@Instance("EasyBuilding")
	public static EasyBuilding instance;
	
	public static BlockGhost ghostBlock;	
	
	@SidedProxy(clientSide = "eb.client.ClientProxy", serverSide = "eb.common.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.registerKeyBindings();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		ghostBlock = new BlockGhost(Constants.GHOST_BLOCK_ID);
		
		GameRegistry.registerBlock(ghostBlock);
		LanguageRegistry.addName(ghostBlock, "Ghost Block");
		
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}
	
	public static void sendToPlayer(Player player, PacketEB packet) {
		proxy.sendToPlayer(player, packet);
	}
}
