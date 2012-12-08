package eb.common.commands;

import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import eb.common.PermissionHandler;

public class CommandAdd extends CommandBase {
	@Override
	public String getCommandName() {
		return "ebadd";
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
				if(PermissionHandler.instance().hasPermission(target.username)) {
					sender.sendChatToPlayer("Player \"" + target.username + "\" is already permitted");
				} else {
					PermissionHandler.instance().add(target.username);
					sender.sendChatToPlayer("\"" + target.username + "\" now has permission");
					target.sendChatToPlayer("You now have permission to use EasyBuilding");
				}
			}
		} else {
			sender.sendChatToPlayer("Usage: " + getCommandUsage());
		}
	}
}
