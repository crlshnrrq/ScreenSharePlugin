package com.sudano.sdscreenshare;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.events.PlayerScreenShareCreateEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareFinalizeEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareJoinEvent;
import com.sudano.sdscreenshare.events.PlayerScreenSharePulledEvent;
import com.sudano.sdscreenshare.events.PlayerScreenSharePushedEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareQuitEvent;

public final class ScreenShareAPI {

	private static final HashMap<UUID, ScreenShare> screenShareMap = new HashMap<>();

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
		ScreenShare ss = new ScreenShare(author.getName(), suspect.getName());

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