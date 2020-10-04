package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenShareCreateEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareCreateEvent(Player author, ScreenShare screenShare) {
		super(author, screenShare);
	}
}