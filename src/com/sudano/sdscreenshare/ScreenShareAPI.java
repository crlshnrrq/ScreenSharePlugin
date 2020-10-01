package com.sudano.sdscreenshare;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public final class ScreenShareAPI {

	private static final HashMap<UUID, ScreenShare> screenShareMap = new HashMap<>();

	public static boolean hasScreenShare(Player player) {
		return screenShareMap.containsKey(player.getUniqueId());
	}

	public static ScreenShare getScreenShare(Player player) {
		return screenShareMap.getOrDefault(player.getUniqueId(), null);
	}

	public static void setScreenShare(Player player, ScreenShare screenshare) {
		screenShareMap.put(player.getUniqueId(), screenshare);
	}

	public static void removeScreenShare(Player player) {
		screenShareMap.remove(player.getUniqueId());
	}
}