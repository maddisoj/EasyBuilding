package eb.client;

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
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.Side;
import eb.common.Direction;
import eb.common.Helper;

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
		new KeyBinding("Toggle Autoplace", Keyboard.KEY_NUMPAD8),
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
		return "GhostKeyHandler";
	}	

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if(!tickEnd) { return; }
		
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityClientPlayerMP player = mc.thePlayer;

		if(controlEnabled) {
			if(kb.keyDescription.equals("Place Ghost") && controlEnabled) {
				MovingObjectPosition target = mc.objectMouseOver;
				if(target == null) { return; }
				
				int[] pos = Helper.getPosition(target.blockX, target.blockY, target.blockZ, target.sideHit);
				GhostBlockHandler.instance().place(pos[0], pos[1], pos[2]);
			} else if(kb.keyDescription.equals("Ghost Forward")) {
				GhostBlockHandler.instance().move(Direction.FORWARD);
			} else if(kb.keyDescription.equals("Ghost Backward")) {
				GhostBlockHandler.instance().move(Direction.BACKWARD);
			} else if(kb.keyDescription.equals("Ghost Left")) {
				GhostBlockHandler.instance().move(Direction.LEFT);
			} else if(kb.keyDescription.equals("Ghost Right")) {
				GhostBlockHandler.instance().move(Direction.RIGHT);
			} else if(kb.keyDescription .equals("Ghost Up")) {
				GhostBlockHandler.instance().move(Direction.UP);
			} else if(kb.keyDescription.equals("Ghost Down")) {
				GhostBlockHandler.instance().move(Direction.DOWN);
			} else if(kb.keyDescription.equals("Place Block")) {
				GhostBlockHandler.instance().placeBlock();
			} else if(kb.keyDescription.equals("Toggle Recording")) {
				GhostBlockHandler.instance().toggleRecording();
			}  else if (kb.keyDescription.equals("Toggle Autoplace")) {
				GhostBlockHandler.instance().toggleAutoplace();
			}
		}
		
		if(kb.keyDescription.equals("Play Macro")) {
				GhostBlockHandler.instance().playMacro();
		} else if(kb.keyDescription.equals("Open Macro UI")) {
			GhostBlockHandler.instance().openMacroGui();
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
