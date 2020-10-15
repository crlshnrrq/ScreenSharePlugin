package com.sudano.sdscreenshare;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

public final class Config {

	private final File file;
	private final YamlConfiguration config;

	public Config() {
		this.file = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "config.yml");
		if (!this.getFile().exists())
			this.getFile().getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.getFile());
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
}