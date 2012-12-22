package eb.core.mode;

import java.util.ArrayList;


public class GhostModeManager {
	private static GhostModeManager INSTANCE = new GhostModeManager();
	
	private ArrayList<GhostMode> modes;
	
	private GhostModeManager() {
		modes = new ArrayList<GhostMode>();
	}
	
	public static GhostModeManager instance() {
		return INSTANCE;
	}
	
	public void addMode(GhostMode mode) {
		modes.add(mode);
	}
	
	public GhostMode getMode(Class<? extends GhostMode> ghostModeClass) {
		for(GhostMode mode : modes) {
			if(mode.getClass() == ghostModeClass) {
				return mode;
			}
		}
		
		return null;
	}
}
