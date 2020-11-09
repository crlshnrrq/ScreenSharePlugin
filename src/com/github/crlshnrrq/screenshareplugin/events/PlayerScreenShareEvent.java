package com.github.crlshnrrq.screenshareplugin.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;

public class PlayerScreenShareEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private ScreenShare screenShare;
	private boolean cancelled;

	public PlayerScreenShareEvent(Player player, ScreenShare screenShare) {
		super(player);
		this.screenShare = screenShare;
		this.cancelled = false;
	}

	public ScreenShare getScreenShare() {
		return this.screenShare;
	}

	public void setScreenShare(ScreenShare screenShare) {
		this.screenShare = screenShare;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}