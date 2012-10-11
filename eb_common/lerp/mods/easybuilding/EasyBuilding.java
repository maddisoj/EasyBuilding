package lerp.mods.easybuilding;

import lerp.mods.easybuilding.network.PacketEB;
import lerp.mods.easybuilding.network.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "EasyBuilding", name = "Easy Building", version = "1.0")
@NetworkMod(channels = { "EasyBuilding" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class EasyBuilding {
	@Instance("EasyBuilding")
	public static EasyBuilding instance;
	
	public static BlockGhost ghostBlock;	
	
	@SidedProxy(clientSide = "lerp.mods.easybuilding.ClientProxy", serverSide = "lerp.mods.easybuilding.CommonProxy")
	public static CommonProxy proxy;
	
	private int ghostBlockId;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		ghostBlockId = BlockIDs.ghostBlockID;
		
		proxy.registerKeyBindings();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		ghostBlock = new BlockGhost(ghostBlockId);
		
		GameRegistry.registerBlock(ghostBlock);
		LanguageRegistry.addName(ghostBlock, "Ghost Block");
		
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}
	
	public static void sendToAllPlayers(PacketEB packet) {
		proxy.sendToAllPlayers(packet);
	}
}
