package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenShareFinalizeEvent extends PlayerScreenShareEvent {

	public PlayerScreenShareFinalizeEvent(Player author, ScreenShare screenShare) {
		super(author, screenShare);
	}
}