package com.sudano.sdscreenshare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class ScreenShare {

	private final String id;
	private final String suspect, author, iniciado;
	private String finalizado;
	private final ArrayList<String> spectators;

	private final File file;
	private final YamlConfiguration config;

	public ScreenShare(String id, String author, String suspect, String iniciado) {
		this.id = id;
		this.suspect = suspect;
		this.author = author;
		this.iniciado = iniciado;
		this.spectators = new ArrayList<>();

		this.file = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare-logs",
				"screenshare-" + id + ".yml");
		if (!this.file.exists())
			this.file.getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.file);
		try {
			this.config.set("author", author);
			this.config.set("suspect", suspect);
			this.config.set("iniciado", iniciado);
			this.config.set("finalizado", "Indefinido");
			this.config.set("logs", new ArrayList<>());
			this.config.save(this.file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getID() {
		return this.id;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getSuspect() {
		return this.suspect;
	}

	public String getIniciado() {
		return this.iniciado;
	}

	public String getFinalizado() {
		return this.finalizado;
	}

	public void setFinalizado(String finalizado) {
		try {
			this.finalizado = finalizado;
			this.config.set("finalizado", finalizado);
			this.config.save(this.file);
		} catch (IOException ex) {
		}
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

	public void log(String log) {
		try {
			List<String> logs = this.config.getStringList("logs");
			logs.add("[" + ScreenShareAPI.getCurrentTime() + "] " + ChatColor.stripColor(log));
			this.config.set("logs", logs);
			this.config.save(this.file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void message(String message) {
		this.message(message, message);
	}

	public void message(String logMessage, String message) {
		this.log(logMessage);
		this.getAllPlayersInScreenShare().forEach(nickname -> {
			Player player = Bukkit.getPlayer(nickname);
			if (player != null)
				player.sendMessage(message);
		});
	}
}