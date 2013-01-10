package eb.core.proxy;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import eb.core.handlers.GhostHandler;
import eb.core.handlers.KeyBindingHandler;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class ClientProxy extends CommonProxy {
	@Override
	public void registerKeyHandler() {
		KeyBindingRegistry.registerKeyBinding(GhostHandler.instance().getKeyHandler());
	}
	
	@Override
	public void registerRenderEvent() {
		MinecraftForge.EVENT_BUS.register(GhostHandler.instance());
	}
}
