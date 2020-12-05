package com.github.crlshnrrq.screenshareplugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenShareCreateEvent;
import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenShareFinalizeEvent;
import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenShareJoinEvent;
import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenSharePulledEvent;
import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenSharePushedEvent;
import com.github.crlshnrrq.screenshareplugin.events.PlayerScreenShareQuitEvent;

public final class ScreenShareAPI {

	private static final HashMap<UUID, ScreenShare> screenShareMap = new HashMap<>();

	public static String getCurrentTime() {
		String[] split = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(GregorianCalendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo")).getTime()).split(" ");
		return split[0] + " às " + split[1];
	}

	public static String getRandomID() {
		char[] values = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

		String id = new String();
		for (int i = 0; i < 5; i++)
			id += values[new Random().nextInt(values.length)];

		return id;
	}

	public static boolean hasScreenShare(Player player) {
		return screenShareMap.containsKey(player.getUniqueId());
	}

	public static ScreenShare getScreenShare(Player player) {
		return screenShareMap.getOrDefault(player.getUniqueId(), null);
	}

	public static void setScreenShare(Player player, ScreenShare screenShare) {
		if (hasScreenShare(player))
			quitScreenShare(player, screenShare);

		screenShareMap.put(player.getUniqueId(), screenShare);
		player.teleport(ScreenSharePlugin.getScreenShareLocation());
	}

	public static void removeScreenShare(Player player) {
		player.teleport(ScreenSharePlugin.getDefaultWorldLocation());
		screenShareMap.remove(player.getUniqueId());
	}

	public static void createScreenShare(Player author, Player suspect) {
		String id = getRandomID();
		while (ScreenSharePlugin.getConfig().existsScreenShare(id))
			id = getRandomID();

		ScreenShare ss = new ScreenShare(id);
		ss.setAuthor(author.getName());
		ss.setSuspect(suspect.getName());
		ss.setIniciado(getCurrentTime());
		ss.setFinalizado("em andamento");

		Bukkit.getPluginManager().callEvent(new PlayerScreenShareCreateEvent(author, ss));

		Bukkit.getPluginManager().callEvent(new PlayerScreenSharePulledEvent(suspect, ss));

		setScreenShare(author, ss);
		setScreenShare(suspect, ss);

		ScreenSharePlugin.getScreenshares().add(ss);
	}

	public static void finalizeScreenShare(Player author, ScreenShare screenShare) {
		screenShare.getAllPlayersInScreenShare().forEach(nickname -> {
			Player players = Bukkit.getPlayer(nickname);
			if (players != null)
				leaveScreenShare(players, screenShare);
		});

		Player suspect = Bukkit.getPlayer(screenShare.getSuspect());
		if (suspect != null)
			Bukkit.getPluginManager().callEvent(new PlayerScreenSharePushedEvent(suspect, screenShare));

		Bukkit.getPluginManager().callEvent(new PlayerScreenShareFinalizeEvent(author, screenShare));

		screenShare.setFinalizado(getCurrentTime());
		ScreenSharePlugin.getConfig().addScreenShareHistory(screenShare.getID());
		ScreenSharePlugin.getScreenshares().remove(screenShare);
	}

	public static void joinScreenShare(Player player, ScreenShare screenShare) {
		setScreenShare(player, screenShare);
		screenShare.addSpectator(player.getName());

		Bukkit.getPluginManager().callEvent(new PlayerScreenShareJoinEvent(player, screenShare));
	}

	public static void leaveScreenShare(Player player, ScreenShare screenShare) {
		screenShare.removeSpectator(player.getName());
		removeScreenShare(player);
	}

	public static void quitScreenShare(Player player, ScreenShare screenShare) {
		leaveScreenShare(player, screenShare);
		Bukkit.getPluginManager().callEvent(new PlayerScreenShareQuitEvent(player, screenShare));
	}
}