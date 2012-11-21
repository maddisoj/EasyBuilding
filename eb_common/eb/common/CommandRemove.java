package eb.common;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

public class CommandRemove extends CommandBase {
	@Override
	public String getCommandName() {
		return "ebremove";
	}
	
	public String getCommandUsage() {
		return "/" + getCommandName() + " <player>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		if(params.length >= 1) {
			EntityPlayerMP target = getPlayerFromName(sender, params[0]);
			
			if(target != null) {
				if(!PermissionHandler.instance().hasPermission(target.username)) {
					sender.sendChatToPlayer("Player \"" + target.username + "\" is already not permitted");
				} else {
					PermissionHandler.instance().remove(target.username);
					sender.sendChatToPlayer("Player \"" + target.username + "\" no longer has permission");
				}
			}
		}
	}
}
