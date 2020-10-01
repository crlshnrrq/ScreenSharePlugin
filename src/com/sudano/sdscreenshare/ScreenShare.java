package com.sudano.sdscreenshare;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public final class ScreenShare {

	private final int id;
	private final String suspect;
	private final ArrayList<String> staffers;
	private final ArrayList<Player> players;

	public ScreenShare(int id, String suspect) {
		this.id = id;
		this.suspect = suspect;
		this.staffers = new ArrayList<>();
		this.players = new ArrayList<>();
	}

	public int getID() {
		return this.id;
	}

	public String getSuspect() {
		return this.suspect;
	}

	public ArrayList<String> getStaffers() {
		return this.staffers;
	}

	public boolean hasStaff(String nickname) {
		return this.getStaffers().contains(nickname);
	}

	public void addStaff(String nickname) {
		if (!hasStaff(nickname))
			this.getStaffers().add(nickname);
	}

	public void removeStaff(String nickname) {
		this.getStaffers().remove(nickname);
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public boolean hasPlayer(Player player) {
		return this.getPlayers().contains(player);
	}

	public void addPlayer(Player player) {
		if (!hasPlayer(player))
			this.getPlayers().add(player);
	}

	public void removePlayer(Player player) {
		this.getPlayers().remove(player);
	}
}