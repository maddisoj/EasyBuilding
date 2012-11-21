package eb.common;

import net.minecraft.src.ICommandManager;
import net.minecraft.src.ServerCommandManager;
import net.minecraftforge.common.Configuration;
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
		addCommands((ServerCommandManager)event.getServer().getCommandManager());
	}
	
	public static void sendToPlayer(Player player, PacketEB packet) {
		proxy.sendToPlayer(player, packet);
	}
	
	private void addCommands(ServerCommandManager manager) {
		manager.registerCommand(new CommandAllow());
		manager.registerCommand(new CommandRemove());
	}
}
