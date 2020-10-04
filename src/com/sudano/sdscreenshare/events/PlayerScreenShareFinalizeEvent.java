package com.sudano.sdscreenshare.events;

import org.bukkit.entity.Player;

import com.sudano.sdscreenshare.ScreenShare;

public class PlayerScreenShareFinalizeEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareFinalizeEvent(Player author, ScreenShare screenShare) {
		super(author, screenShare);
	}
}