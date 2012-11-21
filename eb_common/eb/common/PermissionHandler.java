package eb.common;

import java.util.HashSet;

public class PermissionHandler {
	private static final PermissionHandler INSTANCE = new PermissionHandler();
	private HashSet<String> permissions;
	
	private PermissionHandler() {
		permissions = new HashSet<String>();
	}
	
	public static PermissionHandler instance() {
		return INSTANCE;
	}
	
	public void add(String name) {
		permissions.add(name);
	}
	
	public void remove(String name) {
		permissions.remove(name);
	}
	
	public boolean hasPermission(String name) {
		return permissions.contains(name);
	}
}
