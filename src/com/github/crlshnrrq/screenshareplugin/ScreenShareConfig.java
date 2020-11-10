package com.github.crlshnrrq.screenshareplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareHistoryGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenSharePlayerGUI;
import com.github.crlshnrrq.screenshareplugin.guis.config.ScreenShareConfigGUI;

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

		// +COMMAND
		if (!this.getMessages().contains("command.use-permission"))
			this.getMessages().set("command.use-permission", "screenshareplugin.use");
		if (!this.getMessages().contains("command.view-history-permission"))
			this.getMessages().set("command.view-history-permission", "screenshareplugin.viewhistory");
		if (!this.getMessages().contains("command.insuficient-permissions"))
			this.getMessages().set("command.insuficient-permissions",
					"&cVocê não possui Permissão para usar este Comando!");
		if (!this.getMessages().contains("command.usage"))
			this.getMessages().set("command.usage", "&cSintaxe incorreta, use: /<command> (Jogador)");
		if (!this.getMessages().contains("command.only-players-use"))
			this.getMessages().set("command.only-players-use", "&cApenas Jogadores podem usar este Comando!");
		if (!this.getMessages().contains("command.open-sessions"))
			this.getMessages().set("command.open-sessions", "&aVocê abriu a Lista de Sessões!");
		if (!this.getMessages().contains("command.open-history-sessions"))
			this.getMessages().set("command.open-history-sessions", "&aVocê abriu o Histórico de Sessões!");
		if (!this.getMessages().contains("command.open-config"))
			this.getMessages().set("command.open-config", "&eVocê abriu o Menu de Configurações.");
		if (!this.getMessages().contains("command.open-player-info"))
			this.getMessages().set("command.open-player-info", "&aVocê abriu as Informações de <nickname>!");
		// -COMMAND

		// +SESSION
		if (!this.getMessages().contains("session.spy-permission"))
			this.getMessages().set("session.spy-permission", "screenshareplugin.spy");
		if (!this.getMessages().contains("session.initiate-session"))
			this.getMessages().set("session.initiate-session", "&aVocê puxou <nickname> para uma ScreenShare!");
		if (!this.getMessages().contains("session.finalize-session"))
			this.getMessages().set("session.finalize-session", "&aVocê finalizou a ScreenShare de <nickname>!");
		if (!this.getMessages().contains("session.join-session"))
			this.getMessages().set("session.join-session", "&aVocê entrou na Sessão de <nickname>!");
		if (!this.getMessages().contains("session.leave-session"))
			this.getMessages().set("session.leave-session", "&cVocê saiu da Sessão de <nickname>!");
		if (!this.getMessages().contains("session.use-commands"))
			this.getMessages().set("session.use-commands", "&cVocê não pode executar Comandos na ScreenShare.");
		if (!this.getMessages().contains("session.start-session"))
			this.getMessages().set("session.start-session",
					"&aVocê foi puxado para a ScreenShare! §eBaixe o aplicativo AnyDesk, e siga todos os passos que o Staff falar!");
		if (!this.getMessages().contains("session.end-session"))
			this.getMessages().set("session.end-session",
					"&aSua ScreenShare foi finalizada! &7Obrigado por cooperar com o Servidor, pedimos desculpas pelo incomodo!");
		if (!this.getMessages().contains("session.logout-session"))
			this.getMessages().set("session.logout-during-session", "&c<nickname> deslogou durante a ScreenShare!");

		// +logs
		if (!this.getMessages().contains("session.logs.initiate-session"))
			this.getMessages().set("session.logs.initiate-session", "&7(<nickname> iniciou a Sessão #<id>)");
		if (!this.getMessages().contains("session.logs.finalize-session"))
			this.getMessages().set("session.logs.finalize-session", "&7(<nickname> finalizou a Sessão #<id>)");
		if (!this.getMessages().contains("session.logs.join-session"))
			this.getMessages().set("session.logs.join-session", "&7(<nickname> entrou na Sessão #<id>)");
		if (!this.getMessages().contains("session.logs.leave-session"))
			this.getMessages().set("session.logs.leave-session", "&7(<nickname> saiu da Sessão #<id>)");
		// -logs

		if (!this.getMessages().contains("session.chat-format"))
			this.getMessages().set("session.chat-format", "&7(SS #<id>) &r<nickname> &8» &r<message>");
		// -SESSION

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

	public String getCommand_Permission() {
		return this.getMessages().getString("command.use-permission");
	}

	public void setCommand_Permission(String permission) {
		this.getMessages().set("command.use-permission", permission);
		this.saveMessages();
	}

	public String getCommand_ViewHistoryPermission() {
		return this.getMessages().getString("command.view-history-permission");
	}

	public void setCommand_ViewHistoryPermission(String permission) {
		this.getMessages().set("command.view-history-permission", permission);
		this.saveMessages();
	}

	public String getCommand_InsuficientPermissions() {
		return ChatColor.translateAlternateColorCodes('&',
				this.getMessages().getString("command.insuficient-permissions"));
	}

	public void setCommand_InsuficientPermissions(String message) {
		this.getMessages().set("command.insuficient-permissions", message);
		this.saveMessages();
	}

	public String getCommand_Usage(String command) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("command.usage"))
				.replace("<command>", command);
	}

	public void setCommand_Usage(String message) {
		this.getMessages().set("command.usage", message);
		this.saveMessages();
	}

	public String getCommand_OnlyPlayersUse() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("command.only-players-use"));
	}

	public void setCommand_OnlyPlayersUse(String message) {
		this.getMessages().set("command.only-players-use", message.replace("§", "&"));
		this.saveMessages();
	}

	public String getCommand_OpenSessions() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("command.open-sessions"));
	}

	public void setCommand_OpenSessions(String message) {
		this.getMessages().set("command.open-sessions", message);
		this.saveMessages();
	}

	public String getCommand_OpenHistorySessions() {
		return ChatColor.translateAlternateColorCodes('&',
				this.getMessages().getString("command.open-history-sessions"));
	}

	public void setCommand_OpenHistorySessions(String message) {
		this.getMessages().set("command.open-history-sessions", message);
		this.saveMessages();
	}

	public String getCommand_OpenConfig() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("command.open-config"));
	}

	public void setCommand_OpenConfig(String message) {
		this.getMessages().set("command.open-config", message);
		this.saveMessages();
	}

	public String getCommand_OpenPlayerInfo(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("command.open-player-info"))
				.replace("<nickname>", nickname);
	}

	public void setCommand_OpenPlayerInfo(String message) {
		this.getMessages().set("command.open-player-info", message);
		this.saveMessages();
	}

	public String getSession_SpyPermission() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.spy-permission"));
	}

	public void setSession_SpyPermission(String message) {
		this.getMessages().set("session.spy-permission", message);
		this.saveMessages();
	}

	public String getSession_JoinSession(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.join-session"))
				.replace("<nickname>", nickname);
	}

	public void setSession_JoinSession(String message) {
		this.getMessages().set("session.join-session", message);
		this.saveMessages();
	}

	public String getSession_InitiateSession(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.initiate-session"))
				.replace("<nickname>", nickname);
	}

	public void setSession_InitiateSession(String message) {
		this.getMessages().set("session.initiate-session", message);
		this.saveMessages();
	}

	public String getSession_FinalizeSession(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.finalize-session"))
				.replace("<nickname>", nickname);
	}

	public void setSession_FinalizeSession(String message) {
		this.getMessages().set("session.finalize-session", message);
		this.saveMessages();
	}

	public String getSession_LeaveSession(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.leave-session"))
				.replace("<nickname>", nickname);
	}

	public void setSession_LeaveSession(String message) {
		this.getMessages().set("session.leave-session", message);
		this.saveMessages();
	}

	public String getSession_UseCommands() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.use-commands"));
	}

	public void setSession_UseCommands(String message) {
		this.getMessages().set("session.use-commands", message);
		this.saveMessages();
	}

	public String getSession_StartSession() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.start-session"));
	}

	public void setSession_StartSession(String message) {
		this.getMessages().set("session.start-session", message);
		this.saveMessages();
	}

	public String getSession_EndSession() {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.end-session"));
	}

	public void setSession_EndSession(String message) {
		this.getMessages().set("session.end-session", message);
		this.saveMessages();
	}

	public String getSession_LogoutSession(String nickname) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.logout-session"))
				.replace("<nickname>", nickname);
	}

	public void setSession_LogoutSession(String message) {
		this.getMessages().set("session.logout-sesion", message);
		this.saveMessages();
	}

	public String getSession_Logs_InitiateSession(String nickname, String id) {
		return ChatColor
				.translateAlternateColorCodes('&', this.getMessages().getString("session.logs.initiate-session"))
				.replace("<nickname>", nickname).replace("<id>", id);
	}

	public void setSession_Logs_InitiateSession(String message) {
		this.getMessages().set("session.logs.initiate-sesion", message);
		this.saveMessages();
	}

	public String getSession_Logs_FinalizeSession(String nickname, String id) {
		return ChatColor
				.translateAlternateColorCodes('&', this.getMessages().getString("session.logs.finalize-session"))
				.replace("<nickname>", nickname).replace("<id>", id);
	}

	public void setSession_Logs_FinalizeSession(String message) {
		this.getMessages().set("session.logs.finalize-sesion", message);
		this.saveMessages();
	}

	public String getSession_Logs_JoinSession(String nickname, String id) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.logs.join-session"))
				.replace("<nickname>", nickname).replace("<id>", id);
	}

	public void setSession_Logs_JoinSession(String message) {
		this.getMessages().set("session.logs.join-sesion", message);
		this.saveMessages();
	}

	public String getSession_Logs_LeaveSession(String nickname, String id) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.logs.leave-session"))
				.replace("<nickname>", nickname).replace("<id>", id);
	}

	public void setSession_Logs_LeaveSession(String message) {
		this.getMessages().set("session.logs.leave-sesion", message);
		this.saveMessages();
	}

	public String getSession_ChatFormat(String nickname, String id, String message) {
		return ChatColor.translateAlternateColorCodes('&', this.getMessages().getString("session.chat-format"))
				.replace("<nickname>", nickname).replace("<id>", id).replace("<message>", message);
	}

	public void setSession_ChatFormat(String message) {
		this.getMessages().set("session.chat-format", message);
		this.saveMessages();
	}
}