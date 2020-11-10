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
						config.getCommand_OpenSessions().forEach(message -> player.sendMessage(message));
					} else if (args[0].equalsIgnoreCase("-history")) {
						if (player.hasPermission(config.getCommand_ViewHistoryPermission())) {
							ScreenShareHistoryGUI.openGUI(player);
							config.getCommand_OpenHistorySessions().forEach(message -> player.sendMessage(message));
						} else
							config.getCommand_InsuficientPermissions().forEach(message -> player.sendMessage(message));
					} else if (args[0].equalsIgnoreCase("-config")) {
						ScreenShareConfigGUI.openGUI(player);
						config.getCommand_OpenConfig().forEach(message -> player.sendMessage(message));
					} else {
						ScreenSharePlayerGUI.openGUI(player, args[0]);
						config.getCommand_OpenPlayerInfo(args[0]).forEach(message -> player.sendMessage(message));
					}
				} else
					config.getCommand_Usage(label).forEach(message -> player.sendMessage(message));
			} else
				config.getCommand_InsuficientPermissions().forEach(message -> player.sendMessage(message));
		} else
			config.getCommand_OnlyPlayersUse().forEach(message -> sender.sendMessage(message));
		return true;
	}
}