package com.sudano.sdscreenshare;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.guis.ScreenSharePlayerGUI;
import com.sudano.sdscreenshare.guis.ScreenShareSessionsGUI;

public final class ScreenShareCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("sdscreenshare.use")) {
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("-sessions")) {
						ScreenShareSessionsGUI.openGUI(player);
						player.sendMessage("§aVocê abriu a Lista de ScreenShare's!");
					} else if (args[0].equalsIgnoreCase("-spectate")) {
						if (args.length > 1) {
							Player target = Bukkit.getPlayer(args[1]);
							if (target != null) {
								if (ScreenShareAPI.hasScreenShare(target)) {
									ScreenShare ss = ScreenShareAPI.getScreenShare(target);
									if (!ss.getAuthor().equalsIgnoreCase(player.getName())) {
										if (!ScreenShareAPI.hasScreenShare(player) || ScreenShareAPI
												.getScreenShare(player).getSuspect() != target.getName())
											ScreenShareAPI.joinScreenShare(player, ss);
										else
											ScreenShareAPI.quitScreenShare(player, ss);
									} else
										player.sendMessage("§cO Autor da ScreenShare não pode sair!");
								} else
									player.sendMessage("§c" + target.getName() + " não está em uma ScreenShare!");
							} else
								player.sendMessage("§c" + args[1] + " não foi encontrado!");
						} else
							player.sendMessage("§cSintaxe incorreta, use: /" + label + " -spectate (Jogador)");
					} else {
						ScreenSharePlayerGUI.openGUI(player, args[0]);
						player.sendMessage("§aVocê abriu as Informações de " + args[0] + "!");
					}
				} else
					player.sendMessage("§cSintaxe incorreta, use: /" + label + " (Jogador)");
			} else
				player.sendMessage("§cVocê não possui Permissão para usar este Comando!");
		} else
			sender.sendMessage("§cEste Comando só pode ser utilizado por um Jogador!");
		return true;
	}
}