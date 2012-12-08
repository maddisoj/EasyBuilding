package eb.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.Player;
import eb.common.commands.CommandAdd;
import eb.common.commands.CommandRemove;
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
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		proxy.registerKeyBindings();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		proxy.init(event);	
		proxy.registerTileEntities();
		proxy.registerRenderInformation();
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent event) {
		MinecraftServer server = event.getServer();
		
		addCommands((ServerCommandManager)server.getCommandManager());
		
		if(server.isSinglePlayer()) {
			PermissionHandler.instance().setSinglePlayer(true);
		} else {			
			ServerConfigurationManager configManager = server.getConfigurationManager();
			for(Object username : configManager.getOps()) {
				PermissionHandler.instance().add((String)username);
			}
		}
	}
	
	private void addCommands(ServerCommandManager manager) {
		manager.registerCommand(new CommandAdd());
		manager.registerCommand(new CommandRemove());
	}
}
