package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenSharePushedEvent extends PlayerScreenShareEvent {

	public PlayerScreenSharePushedEvent(Player suspect, ScreenShare screenShare) {
		super(suspect, screenShare);
	}
}