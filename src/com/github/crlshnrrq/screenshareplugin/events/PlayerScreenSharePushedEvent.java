package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenSharePushedEvent extends PlayerScreenShareEvent {

	public PlayerScreenSharePushedEvent(Player suspect, ScreenShare screenShare) {
		super(suspect, screenShare);
	}
}