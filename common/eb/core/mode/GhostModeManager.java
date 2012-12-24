package eb.core.mode;

import java.util.ArrayList;
import java.util.HashMap;


public class GhostModeManager {	
	private static HashMap<Class, GhostMode> modes = new HashMap<Class, GhostMode>();
	
	private GhostModeManager() {}
	
	public static void addMode(GhostMode mode) {
		modes.put(mode.getClass(), mode);
	}
	
	public static GhostMode getMode(Class<? extends GhostMode> ghostModeClass) {
		return modes.get(ghostModeClass);
	}
}
