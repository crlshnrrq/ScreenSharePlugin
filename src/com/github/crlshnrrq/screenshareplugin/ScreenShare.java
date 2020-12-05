package com.github.crlshnrrq.screenshareplugin;

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
	private final ArrayList<String> spectators;

	private final File file;
	private final YamlConfiguration config;

	public ScreenShare(String id) {
		this.id = id;
		this.spectators = new ArrayList<>();

		this.file = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare/screenshare-logs",
				"screenshare-" + id + ".yml");
		if (!this.getFile().exists())
			this.getFile().getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.getFile());
		if (!this.getConfig().contains("logs")) {
			this.getConfig().set("logs", new ArrayList<>());
			this.saveConfig();
		}
	}

	public String getID() {
		return this.id;
	}

	protected YamlConfiguration getConfig() {
		return this.config;
	}

	protected File getFile() {
		return this.file;
	}

	protected void saveConfig() {
		try {
			this.getConfig().save(this.getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public String getAuthor() {
		return this.getConfig().getString("author");
	}

	public void setAuthor(String author) {
		this.getConfig().set("author", author);
		this.saveConfig();
	}

	public String getSuspect() {
		return this.getConfig().getString("suspect");
	}

	public void setSuspect(String suspect) {
		this.getConfig().set("suspect", suspect);
		this.saveConfig();
	}

	public String getIniciado() {
		return this.getConfig().getString("iniciado");
	}

	public void setIniciado(String iniciado) {
		this.getConfig().set("iniciado", iniciado);
		this.saveConfig();
	}

	public String getFinalizado() {
		return this.getConfig().getString("finalizado");
	}

	public void setFinalizado(String finalizado) {
		try {
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

	public List<String> getLogs() {
		return this.config.getStringList("logs");
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

	public void messages(List<String> messages) {
		messages.forEach(message -> this.message(message));
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