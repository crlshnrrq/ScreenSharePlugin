package com.sudano.sdscreenshare;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ScreenShare {

	private final int id;
	private final String suspect, author;
	private final ArrayList<String> spectators;

	public ScreenShare(int id, String author, String suspect) {
		this.id = id;
		this.suspect = suspect;
		this.author = author;
		this.spectators = new ArrayList<>();
	}

	public int getID() {
		return this.id;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getSuspect() {
		return this.suspect;
	}

	public ArrayList<String> getSpectators() {
		return this.spectators;
	}

	public boolean hasSpectator(String spectator) {
		return this.getSpectators().contains(spectator);
	}

	public void addSpectators(String... spectators) {
		for (String spectator : spectators)
			this.addSpectator(spectator);
	}

	public void addSpectator(String spectator) {
		if (!hasSpectator(spectator))
			this.getSpectators().add(spectator);
	}

	public void removeSpectators(String... spectators) {
		for (String spectator : spectators)
			this.removeSpectator(spectator);
	}

	public void removeSpectator(String spectator) {
		this.getSpectators().remove(spectator);
	}

	public ArrayList<String> getAllPlayersInScreenShare() {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(this.getSpectators());
		list.add(this.getSuspect());
		list.add(this.getAuthor());
		return list;
	}

	public void announceMessage(String message) {
		this.getAllPlayersInScreenShare().forEach(nickname -> {
			Player player = Bukkit.getPlayer(nickname);
			if (player != null)
				player.sendMessage(message);
		});
	}
}