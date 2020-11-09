package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenSharePulledEvent extends PlayerScreenShareEvent {

	public PlayerScreenSharePulledEvent(Player suspect, ScreenShare screenShare) {
		super(suspect, screenShare);
	}
}