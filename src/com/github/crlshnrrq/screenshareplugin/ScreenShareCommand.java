package com.github.crlshnrrq.screenshareplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareHistoryGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenSharePlayerGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareSessionsGUI;
import com.github.crlshnrrq.screenshareplugin.guis.config.ScreenShareConfigGUI;

public final class ScreenShareCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ScreenShareConfig config = ScreenSharePlugin.getConfig();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission(config.getCommand_Permission())) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("-sessions")) {
						ScreenShareSessionsGUI.openGUI(player);
						player.sendMessage(config.getCommand_OpenSessions());
					} else if (args[0].equalsIgnoreCase("-history")) {
						if (player.hasPermission(config.getCommand_ViewHistoryPermission())) {
							ScreenShareHistoryGUI.openGUI(player);
							player.sendMessage(config.getCommand_OpenHistorySessions());
						} else
							player.sendMessage(config.getCommand_InsuficientPermissions());
					} else if (args[0].equalsIgnoreCase("-config")) {
						ScreenShareConfigGUI.openGUI(player);
						player.sendMessage(config.getCommand_OpenConfig());
					} else {
						ScreenSharePlayerGUI.openGUI(player, args[0]);
						player.sendMessage(config.getCommand_OpenPlayerInfo(args[0]));
					}
				} else
					player.sendMessage(config.getCommand_Usage(label));
			} else
				player.sendMessage(config.getCommand_InsuficientPermissions());
		} else
			sender.sendMessage(config.getCommand_OnlyPlayersUse());
		return true;
	}
}