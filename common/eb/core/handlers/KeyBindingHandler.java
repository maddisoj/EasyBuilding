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
public class KeyBindingHandler extends KeyHandler {
	private static class EBKeyBinding {
		private KeyBinding keyBinding;
		private boolean repeats;
		
		public EBKeyBinding(String name, int key, boolean repeats) {
			this.keyBinding = new KeyBinding(name, key);
			this.repeats = repeats;
		}
		
		public boolean isNamed(String name) {
			return keyBinding.keyDescription.equals(name);
		}
		
		public KeyBinding getKeyBinding() {
			return keyBinding;
		}
		
		public boolean getRepeats() {
			return repeats;
		}
	}
	
	public static EBKeyBinding ebKeyBindings[] = {
		new EBKeyBinding("Place Ghost", Keyboard.KEY_NUMPAD0, false),
		new EBKeyBinding("Move Ghost Forward", Keyboard.KEY_NUMPAD5, false),
		new EBKeyBinding("Move Ghost Backward", Keyboard.KEY_NUMPAD2, false),
		new EBKeyBinding("Move Ghost Left", Keyboard.KEY_NUMPAD1, false),
		new EBKeyBinding("Move Ghost Right", Keyboard.KEY_NUMPAD3, false),
		new EBKeyBinding("Move Ghost Up", Keyboard.KEY_NUMPAD4, false),
		new EBKeyBinding("Move Ghost Down", Keyboard.KEY_NUMPAD6, false),
		new EBKeyBinding("Place Block", Keyboard.KEY_RETURN, true),
		new EBKeyBinding("Toggle Recording", Keyboard.KEY_HOME, false),
		new EBKeyBinding("Play Macro", Keyboard.KEY_INSERT, false),
		new EBKeyBinding("Open Menu", Keyboard.KEY_DECIMAL, false),
	};
	
	private static boolean controlEnabled = true;

	public KeyBindingHandler() {
		super(gatherKeyBindings(), gatherRepeats());
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
			} else if(kb.keyDescription.equals("Move Ghost Forward")) {
				GhostHandler.instance().move(Direction.FORWARD);
			} else if(kb.keyDescription.equals("Move Ghost Backward")) {
				GhostHandler.instance().move(Direction.BACKWARD);
			} else if(kb.keyDescription.equals("Move Ghost Left")) {
				GhostHandler.instance().move(Direction.LEFT);
			} else if(kb.keyDescription.equals("Move Ghost Right")) {
				GhostHandler.instance().move(Direction.RIGHT);
			} else if(kb.keyDescription .equals("Move Ghost Up")) {
				GhostHandler.instance().move(Direction.UP);
			} else if(kb.keyDescription.equals("Move Ghost Down")) {
				GhostHandler.instance().move(Direction.DOWN);
			} else if(kb.keyDescription.equals("Place Block")) {
				GhostHandler.instance().use();
			} else if(kb.keyDescription.equals("Toggle Recording")) {
				GhostHandler.instance().toggleRecording();
			}
		}
		
		if(kb.keyDescription.equals("Play Macro")) {
			GhostHandler.instance().playMacro();
		} else if(kb.keyDescription.equals("Open Menu")) {
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
	
	private static KeyBinding[] gatherKeyBindings() {
		KeyBinding[] bindings = new KeyBinding[ebKeyBindings.length];
		
		for(int i = 0; i < ebKeyBindings.length; ++i) {
			bindings[i] = ebKeyBindings[i].getKeyBinding();
		}
		
		return bindings;
	}
	
	private static boolean[] gatherRepeats() {
		boolean[] repeats = new boolean[ebKeyBindings.length];
		
		for(int i = 0; i < ebKeyBindings.length; ++i) {
			repeats[i] = ebKeyBindings[i].getRepeats();
		}
		
		return repeats;
	}
}
