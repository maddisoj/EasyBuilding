package eb.core.mode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GhostModeManager implements Iterable<GhostMode> {	
	private static GhostModeManager INSTANCE = new GhostModeManager();
	
	private Map<Class, GhostMode> modes;
	
	private GhostModeManager() {
		modes = new LinkedHashMap<Class, GhostMode>();
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
	
	public GhostMode getMode(String name) {
		for(GhostMode mode : this) {
			if(mode.toString().equals(name)) {
				return mode;
			}
		}
		
		return null;
	}

	@Override
	public Iterator<GhostMode> iterator() {
		return modes.values().iterator();
	}
}
