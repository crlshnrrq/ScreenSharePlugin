package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenSharePulledEvent extends PlayerScreenShareEvent {

	public PlayerScreenSharePulledEvent(Player suspect, ScreenShare screenShare) {
		super(suspect, screenShare);
	}
}