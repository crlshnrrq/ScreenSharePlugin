package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenShareQuitEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareQuitEvent(Player player, ScreenShare screenShare) {
		super(player, screenShare);
	}
}