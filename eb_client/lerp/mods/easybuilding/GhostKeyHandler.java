package lerp.mods.easybuilding;

import java.util.ArrayList;
import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet15Place;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class GhostKeyHandler extends KeyHandler {
	
	public static KeyBinding keyBindings[] = {
		new KeyBinding("PlaceGhost", Keyboard.KEY_NUMPAD3),
		new KeyBinding("GhostForward", Keyboard.KEY_NUMPAD8),
		new KeyBinding("GhostBackward", Keyboard.KEY_NUMPAD5),
		new KeyBinding("GhostLeft", Keyboard.KEY_NUMPAD4),
		new KeyBinding("GhostRight", Keyboard.KEY_NUMPAD6),
		new KeyBinding("GhostUp", Keyboard.KEY_NUMPAD9),
		new KeyBinding("GhostDown", Keyboard.KEY_NUMPAD7),
		new KeyBinding("PlaceBlock", Keyboard.KEY_ADD),
	};
	
	public static boolean repeats[] = {
		false, false, false, false, false, false, false, true
	};

	public GhostKeyHandler() {		
		super(keyBindings, repeats);
	}

	@Override
	public String getLabel() {
		return "GhostKeyHandler";
	}	

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if(!tickEnd) { return; }
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityClientPlayerMP player = mc.thePlayer;

		if(kb.keyDescription.equals("PlaceGhost")) {
			MovingObjectPosition target = mc.objectMouseOver;
			if(target == null) { return; }
			
			int[] pos = Helper.getPosition(target.blockX, target.blockY, target.blockZ, target.sideHit);
			GhostBlockHandler.instance().place(pos[0], pos[1], pos[2]);
		} else if(kb.keyDescription.equals("GhostForward")) {
			GhostBlockHandler.instance().move(Direction.FORWARD);
		} else if(kb.keyDescription.equals("GhostBackward")) {
			GhostBlockHandler.instance().move(Direction.BACKWARD);
		} else if(kb.keyDescription.equals("GhostLeft")) {
			GhostBlockHandler.instance().move(Direction.LEFT);
		} else if(kb.keyDescription.equals("GhostRight")) {
			GhostBlockHandler.instance().move(Direction.RIGHT);
		} else if(kb.keyDescription .equals("GhostUp")) {
			GhostBlockHandler.instance().move(Direction.UP);
		} else if(kb.keyDescription.equals("GhostDown")) {
			GhostBlockHandler.instance().move(Direction.DOWN);
		} else if(kb.keyDescription.equals("PlaceBlock")) {
			GhostBlockHandler.instance().placeBlock();
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
