package com.github.crlshnrrq.screenshareplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareHistoryGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenSharePlayerGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareSessionsGUI;

public final class ScreenShareCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(ScreenShareMessages.COMMAND_USE_PERMISSION.toString())) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("-sessions")) {
						ScreenShareSessionsGUI.openGUI(player);
						ScreenShareMessages.COMMAND_OPEN_SESSIONS.sendMessage(player);
					} else if (args[0].equalsIgnoreCase("-history")) {
						if (player.hasPermission(ScreenShareMessages.COMMAND_VIEW_HISTORY_PERMISSION.toString())) {
							ScreenShareHistoryGUI.openGUI(player);
							ScreenShareMessages.COMMAND_OPEN_HISTORY_SESSIONS.sendMessage(player);
						} else
							ScreenShareMessages.COMMAND_INSUFICIENT_PERMISSIONS.sendMessage(player);
					} else {
						ScreenSharePlayerGUI.openGUI(player, args[0]);
						ScreenShareMessages.COMMAND_OPEN_PLAYER_INFO.replace("<nickname>", args[0]).sendMessage(player);
					}
				} else
					ScreenShareMessages.COMMAND_USAGE.replace("<command>", label).sendMessage(player);
			} else
				ScreenShareMessages.COMMAND_INSUFICIENT_PERMISSIONS.sendMessage(player);
		} else
			ScreenShareMessages.COMMAND_ONLY_PLAYERS_USE.sendMessage(sender);
		return true;
	}
}