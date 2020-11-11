package com.github.crlshnrrq.screenshareplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public final class ScreenShareConfig {

	private final File file;
	private final YamlConfiguration config;

	private final File messagesFile;
	private final YamlConfiguration messages;

	public ScreenShareConfig() {
		this.file = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "config.yml");
		if (!this.getFile().exists())
			this.getFile().getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.getFile());

		this.messagesFile = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "messages.yml");
		if (!this.getMessagesFile().exists())
			this.getMessagesFile().getParentFile().mkdirs();
		this.messages = YamlConfiguration.loadConfiguration(this.getMessagesFile());

		if (!this.getConfig().contains("screenshare-history"))
			this.getConfig().set("screenshare-history", new ArrayList<>());
		this.saveConfig();

		for (ScreenShareMessages message : ScreenShareMessages.values())
			if (!this.getMessages().contains(message.getPath()))
				this.getMessages().set(message.getPath(),
						message.getDefaultMessages().size() == 1 ? message.getDefaultMessages().get(0)
								: message.getDefaultMessages());
		this.saveMessages();
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

	protected File getMessagesFile() {
		return this.messagesFile;
	}

	protected YamlConfiguration getMessages() {
		return this.messages;
	}

	public void saveMessages() {
		try {
			this.getMessages().save(this.getMessagesFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public boolean existsScreenShare(String id) {
		return new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare/screenshare-logs",
				"screenshare-" + id + ".yml").exists();
	}

	public ScreenShare getScreenShare(String id) {
		return this.existsScreenShare(id) ? new ScreenShare(id) : null;
	}

	public void deleteScreenShare(String id) {
		ScreenShare screenShare = this.getScreenShare(id);
		if (screenShare != null && screenShare.getFile().delete())
			this.removeScreenShareHistory(id);
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