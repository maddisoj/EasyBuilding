package eb.client;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.registry.TickRegistry;
import eb.common.CommonProxy;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class ClientProxy extends CommonProxy {
	@Override
	public void registerKeyHandler() {
		KeyBindingRegistry.registerKeyBinding(new GhostKeyHandler());
		//TickRegistry.registerTickHandler(new GhostBlockRenderer(), Side.CLIENT);
	}
	
	@Override
	public void registerRenderEvent() {
		MinecraftForge.EVENT_BUS.register(GhostBlockHandler.instance());
	}
}
