package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenShareJoinEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareJoinEvent(Player player, ScreenShare screenShare) {
		super(player, screenShare);
	}
}