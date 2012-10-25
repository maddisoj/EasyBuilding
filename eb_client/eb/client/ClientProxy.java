package eb.client;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import eb.common.CommonProxy;

public class ClientProxy extends CommonProxy {
	public void registerKeyBindings() {
		KeyBindingRegistry.registerKeyBinding(new GhostKeyHandler());
	}
	
	public void registerRenderInformation() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileGhostBlock.class, new GhostBlockRenderer());
	}
}
