package eb.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import eb.core.handlers.PermissionHandler;

public class CommandRemove extends CommandBase {
	@Override
	public String getCommandName() {
		return "ebremove";
	}
	
	public String getCommandUsage() {
		return "/" + getCommandName() + " <player>";
	}
	
    public int getRequiredPermissionLevel() {
        return 2;
    }

	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		if(params.length >= 1) {
			EntityPlayerMP target = func_82359_c(sender, params[0]);
			
			if(target != null) {
				if(!PermissionHandler.instance().hasPermission(target.username)) {
					sender.sendChatToPlayer("Player \"" + target.username + "\" is already not permitted");
				} else {
					PermissionHandler.instance().remove(target.username);
					sender.sendChatToPlayer("\"" + target.username + "\" no longer has permission");
					target.sendChatToPlayer("You no longer have permission to use EasyBuilding");
				}
			}
		} else {
			sender.sendChatToPlayer("Usage: " + getCommandUsage());
		}
	}
}
