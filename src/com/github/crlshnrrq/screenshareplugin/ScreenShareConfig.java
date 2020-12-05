package com.github.crlshnrrq.screenshareplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.PermissionDefault;

public final class ScreenShareConfig {

	private final File configFile;
	private final YamlConfiguration config;

	private final File permissionsFile;
	private final YamlConfiguration permissions;

	private final File messagesFile;
	private final YamlConfiguration messages;

	public ScreenShareConfig() {
		this.configFile = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "config.yml");
		if (!this.getFile().exists())
			this.getFile().getParentFile().mkdirs();
		this.config = YamlConfiguration.loadConfiguration(this.getFile());

		if (!this.getConfig().contains("pastebin-developer-api-key"))
			this.getConfig().set("pastebin-developer-api-key", "insert dev api key here");
		if (!this.getConfig().contains("screenshare-history"))
			this.getConfig().set("screenshare-history", new ArrayList<>());
		this.saveConfig();

		this.messagesFile = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare", "messages.yml");
		if (!this.getMessagesFile().exists())
			this.getMessagesFile().getParentFile().mkdirs();
		this.messages = YamlConfiguration.loadConfiguration(this.getMessagesFile());

		for (ScreenShareMessages message : ScreenShareMessages.values()) {
			if (!this.getMessages().contains(message.getPath()))
				this.getMessages().set(message.getPath(),
						message.isMultipleLine() ? message.getMessages() : message.toString());
			else
				message.setMessages(this.getMessages().isString(message.getPath())
						? Arrays.asList(this.getMessages().getString(message.getPath()))
						: this.getMessages().getStringList(message.getPath()));
		}
		this.saveMessages();

		this.permissionsFile = new File(ScreenSharePlugin.getPlugin().getDataFolder() + "/screenshare",
				"permissions.yml");
		if (!this.getPermissionsFile().exists())
			this.getPermissionsFile().getParentFile().mkdirs();
		this.permissions = YamlConfiguration.loadConfiguration(this.getPermissionsFile());

		for (ScreenSharePermissions permission : ScreenSharePermissions.values()) {
			String permissionPath = permission.name().toLowerCase().replace("_", "-");
			if (!this.getPermissions().contains(permissionPath)) {
				if (!this.getPermissions().contains(permissionPath + ".name"))
					this.getPermissions().set(permissionPath + ".name", permission.getName());
				if (!this.getPermissions().contains(permissionPath + ".default"))
					this.getPermissions().set(permissionPath + ".default", permission.getDefault().name());
				if (!this.getPermissions().contains(permissionPath + ".description"))
					this.getPermissions().set(permissionPath + ".description", permission.getDescription());
			} else {
				permission.setName(this.getPermissions().getString(permissionPath + ".name"));
				permission.setDefault(
						PermissionDefault.valueOf(this.getPermissions().getString(permissionPath + ".default")));
				permission.setDescription(this.getPermissions().getString(permissionPath + ".description"));
			}
		}
		this.savePermissions();
	}

	protected File getFile() {
		return this.configFile;
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

	public String getPastebinDeveloperAPIKey() {
		return this.getConfig().getString("pastebin-developer-api-key");
	}

	public void setPastebinDeveloperAPIKey(String apiKey) {
		this.getConfig().set("pastebin-developer-api-key", apiKey);
		this.saveConfig();
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

	protected File getPermissionsFile() {
		return this.permissionsFile;
	}

	protected YamlConfiguration getPermissions() {
		return this.permissions;
	}

	public void savePermissions() {
		try {
			this.getPermissions().save(this.getPermissionsFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}