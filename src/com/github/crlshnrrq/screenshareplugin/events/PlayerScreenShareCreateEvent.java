package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenShareCreateEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareCreateEvent(Player author, ScreenShare screenShare) {
		super(author, screenShare);
	}
}