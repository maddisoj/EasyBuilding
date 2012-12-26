package eb.core.mode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GhostModeManager implements Iterable<GhostMode> {	
	private static GhostModeManager INSTANCE = new GhostModeManager();
	
	private HashMap<Class, GhostMode> modes;
	
	private GhostModeManager() {
		modes = new HashMap<Class, GhostMode>();
	}
	
	public static GhostModeManager instance() {
		return INSTANCE;
	}
	
	public void addMode(GhostMode mode) {
		modes.put(mode.getClass(), mode);
	}
	
	public GhostMode getMode(Class<? extends GhostMode> ghostModeClass) {
		return modes.get(ghostModeClass);
	}

	@Override
	public Iterator<GhostMode> iterator() {
		return modes.values().iterator();
	}
}
