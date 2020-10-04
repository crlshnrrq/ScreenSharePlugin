package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenShareQuitEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareQuitEvent(Player player, ScreenShare screenShare) {
		super(player, screenShare);
	}
}