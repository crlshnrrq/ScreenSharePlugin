package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenShareJoinEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareJoinEvent(Player player, ScreenShare screenShare) {
		super(player, screenShare);
	}
}