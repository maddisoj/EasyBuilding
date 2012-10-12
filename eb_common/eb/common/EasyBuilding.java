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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import eb.common.network.PacketEB;
import eb.common.network.PacketHandler;

@Mod(modid = "EasyBuilding", name = "Easy Building", version = "1.0")
@NetworkMod(channels = { "EasyBuilding" }, clientSideRequired = true, serverSideRequired = false, packetHandler = PacketHandler.class)
public class EasyBuilding {
	@Instance("EasyBuilding")
	public static EasyBuilding instance;
	
	public static BlockGhost ghostBlock;	
	
	@SidedProxy(clientSide = "eb.client.ClientProxy", serverSide = "eb.common.CommonProxy")
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
