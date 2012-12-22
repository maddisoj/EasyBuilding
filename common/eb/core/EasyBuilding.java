package eb.core;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import eb.command.CommandAdd;
import eb.command.CommandRemove;
import eb.core.handlers.PermissionHandler;
import eb.core.proxy.CommonProxy;
import eb.network.PacketHandler;

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
	
	@SidedProxy(clientSide = "eb.core.proxy.ClientProxy", serverSide = "eb.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		proxy.registerKeyHandler();
	}
	
	@Init
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderEvent();
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
