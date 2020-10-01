package com.sudano.sdscreenshare.comandos;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sudano.sdscreenshare.Main;
import com.sudano.sdscreenshare.ScreenShare;
import com.sudano.sdscreenshare.ScreenShareAPI;

public class ScreenShareCmd implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cEsse comando só pode ser utilizado dentro de jogo!");
			return false;
		}

		Player p = (Player) sender;

		if (cmd.getLabel().equalsIgnoreCase("ss") || cmd.getLabel().equalsIgnoreCase("screenshare")) {
			if (p.hasPermission("sdscreenshare.usar")) {
				if (args.length == 0) {
					p.sendMessage("§cComando incorreto, utilize /" + cmd.getLabel() + " <Jogador>");
					return false;
				} else {

					Player mencionado = Bukkit.getPlayer(args[0]);

					if (mencionado == null) {
						p.sendMessage("§cJogador \"" + args[0] + "\" não encontrado!");
						return false;
					} else {

						if (mencionado.getName().equals(p.getName())) {
							p.sendMessage("§cVocê não pode por a si mesmo na ScreenShare!");
						} else {
							if (ScreenShareAPI.hasScreenShare(mencionado)) {
								ScreenShare ss = ScreenShareAPI.getScreenShare(mencionado);
								ss.getPlayers().forEach(players -> players
										.teleport(Bukkit.getWorld(Main.mundo_nome).getSpawnLocation()));
								mencionado.sendMessage(Main.retirado_ss);
								p.sendMessage(
										"§cVocê removeu o jogador \"" + mencionado.getName() + "\" da ScreenShare!");

								for (String s : Main.plugin.getConfig().getStringList("SaiuSS")) {
									mencionado.sendMessage(s.replace("&", "§"));
								}
							} else {
								ScreenShare screenshare = new ScreenShare(Main.getScreenshares().size() + 1,
										mencionado.getName());
								ScreenShareAPI.setScreenShare(mencionado, screenshare);
								screenshare.addPlayer(mencionado);
								mencionado.sendMessage(Main.colocado_ss);
								for (String s : Main.plugin.getConfig().getStringList("EntrouSS"))
									mencionado.sendMessage(s.replace("&", "§"));

								screenshare.addPlayer(p);
								screenshare.addStaff(p.getName());
								p.sendMessage(
										"§cVocê colocou o jogador \"" + mencionado.getName() + "\" na ScreenShare!");

								screenshare.getPlayers().forEach(players -> mencionado
										.teleport(Bukkit.getWorld(Main.mundoss_nome).getSpawnLocation()));
							}
						}
					}
				}
			}
		}
		return false;
	}
}