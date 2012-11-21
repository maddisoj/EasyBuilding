package eb.common;

import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;

public class CommandAllow extends CommandBase {
	@Override
	public String getCommandName() {
		return "eballow";
	}
	
	public String getCommandUsage() {
		return "/" + getCommandName() + " <player>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params) {
		if(params.length >= 1) {
			EntityPlayerMP target = getPlayerFromName(sender, params[0]);
			
			if(target != null) {
				if(PermissionHandler.instance().hasPermission(target.username)) {
					sender.sendChatToPlayer("Player \"" + target.username + "\" is already permitted");
				} else {
					PermissionHandler.instance().add(target.username);
					sender.sendChatToPlayer("Player \"" + target.username + "\" now has permission");
				}
			}
		}
	}
}
