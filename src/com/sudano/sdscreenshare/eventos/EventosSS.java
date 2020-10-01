package com.sudano.sdscreenshare.eventos;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.sudano.sdscreenshare.Main;
import com.sudano.sdscreenshare.ScreenShareAPI;

public class EventosSS implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (ScreenShareAPI.hasScreenShare(p)) {
			for (Player todos : Bukkit.getOnlinePlayers()) {
				for (String s : Main.plugin.getConfig().getStringList("DesconectouSS")) {
					todos.sendMessage(s.replace("&", "§").replace("{player}", p.getName()));
				}
			}
			if (Boolean.valueOf(Main.banirquit) == true) {
				Main.comandoban = Main.getPlugin().getConfig().getString("ComandoBan").replace("/", "")
						.replace("{player}", p.getName());
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), Main.comandoban);
			}
			ScreenShareAPI.removeScreenShare(p);
		}
	}

	@EventHandler
	public void onTryEscape(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (ScreenShareAPI.hasScreenShare(p)) {
			String cmd = e.getMessage().toLowerCase();
			if (cmd.startsWith("/")) {
				e.setCancelled(true);
				p.sendMessage("§cVocê não pode executar comandos na ScreenShare!");
			}

		}
	}
}