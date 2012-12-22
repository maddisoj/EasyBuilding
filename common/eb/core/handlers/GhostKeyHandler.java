package eb.core.handlers;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eb.core.Constants;
import eb.core.Direction;
import eb.core.EBHelper;

/**
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

@SideOnly(Side.CLIENT)
public class GhostKeyHandler extends KeyHandler {
	
	//TODO: find a better way of doing this
	public static KeyBinding keyBindings[] = {
		new KeyBinding("Place Ghost", Keyboard.KEY_NUMPAD0),
		new KeyBinding("Ghost Forward", Keyboard.KEY_NUMPAD5),
		new KeyBinding("Ghost Backward", Keyboard.KEY_NUMPAD2),
		new KeyBinding("Ghost Left", Keyboard.KEY_NUMPAD1),
		new KeyBinding("Ghost Right", Keyboard.KEY_NUMPAD3),
		new KeyBinding("Ghost Up", Keyboard.KEY_NUMPAD4),
		new KeyBinding("Ghost Down", Keyboard.KEY_NUMPAD6),
		new KeyBinding("Place Block", Keyboard.KEY_RETURN),
		new KeyBinding("Toggle Recording", Keyboard.KEY_NUMPAD7),
		new KeyBinding("Play Macro", Keyboard.KEY_NUMPAD9),
		new KeyBinding("Open Macro UI", Keyboard.KEY_DECIMAL),
	};
	
	public static boolean repeats[] = {
		false, false, false, false, false, false, false, true, false, false, false, false, false
	};
	
	private static boolean controlEnabled = true;

	public GhostKeyHandler() {
		super(keyBindings, repeats);
	}

	@Override
	public String getLabel() {
		return Constants.MOD_NAME + ".KeyHandler";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if(!tickEnd && EBHelper.getWorld() != null) { return; }
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityClientPlayerMP player = mc.thePlayer;

		if(controlEnabled) {
			if(kb.keyDescription.equals("Place Ghost")) {
				GhostHandler.instance().place();
			} else if(kb.keyDescription.equals("Ghost Forward")) {
				GhostHandler.instance().move(Direction.FORWARD);
			} else if(kb.keyDescription.equals("Ghost Backward")) {
				GhostHandler.instance().move(Direction.BACKWARD);
			} else if(kb.keyDescription.equals("Ghost Left")) {
				GhostHandler.instance().move(Direction.LEFT);
			} else if(kb.keyDescription.equals("Ghost Right")) {
				GhostHandler.instance().move(Direction.RIGHT);
			} else if(kb.keyDescription .equals("Ghost Up")) {
				GhostHandler.instance().move(Direction.UP);
			} else if(kb.keyDescription.equals("Ghost Down")) {
				GhostHandler.instance().move(Direction.DOWN);
			} else if(kb.keyDescription.equals("Place Block")) {
				GhostHandler.instance().use();
			} else if(kb.keyDescription.equals("Toggle Recording")) {
				GhostHandler.instance().toggleRecording();
			}
		}
		
		if(kb.keyDescription.equals("Play Macro")) {
			GhostHandler.instance().playMacro();
		} else if(kb.keyDescription.equals("Open Macro UI")) {
			GhostHandler.instance().openMacroGui();
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}
	
	public static void setControl(boolean enabled) {
		controlEnabled = enabled;
	}
}
