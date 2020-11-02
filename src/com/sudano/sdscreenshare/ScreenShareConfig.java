package com.sudano.sdscreenshare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public final class ScreenShareConfig {

	private final File file;
	private final YamlConfiguration config;

	public ScreenShareConfig() {
		this.file = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "config.yml");
		if (!this.getFile().exists())
			this.getFile().getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.getFile());

		if (!this.getConfig().contains("screenshare-history")) {
			this.getConfig().set("screenshare-history", new ArrayList<>());
			this.saveConfig();
		}
	}

	protected File getFile() {
		return this.file;
	}

	protected YamlConfiguration getConfig() {
		return this.config;
	}

	public void saveConfig() {
		try {
			this.getConfig().save(this.getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public List<String> getScreenShareHistory() {
		return this.getConfig().getStringList("screenshare-history");
	}

	public void addScreenShareHistory(String id) {
		List<String> history = this.getScreenShareHistory();
		history.add(id);
		this.getConfig().set("screenshare-history", history);
		this.saveConfig();
	}

	public void removeScreenShareHistory(String id) {
		List<String> history = this.getScreenShareHistory();
		history.remove(id);
		this.getConfig().set("screenshare-history", history);
		this.saveConfig();
	}
}