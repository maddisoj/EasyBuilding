package eb.core.handlers;

import java.util.HashSet;

public class PermissionHandler {
	private static final PermissionHandler INSTANCE = new PermissionHandler();
	private HashSet<String> permissions;
	private boolean singlePlayer;
	
	private PermissionHandler() {
		permissions = new HashSet<String>();
		singlePlayer = false;
	}
	
	public static PermissionHandler instance() {		
		return INSTANCE;
	}
	
	public void setSinglePlayer(boolean singlePlayer) {
		this.singlePlayer = singlePlayer;
	}
	
	public void add(String name) {
		permissions.add(name);
	}
	
	public void remove(String name) {
		permissions.remove(name);
	}
	
	public boolean hasPermission(String name) {
		return singlePlayer || permissions.contains(name);
	}
}
