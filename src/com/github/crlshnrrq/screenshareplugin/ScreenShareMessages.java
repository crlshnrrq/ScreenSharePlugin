package com.github.crlshnrrq.screenshareplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum ScreenShareMessages {
	COMMAND_USE_PERMISSION("command.use-permission", false, "screenshareplugin.use"),
	COMMAND_CONFIG_PERMISSION("command.config-permission", false, "screenshareplugin.config"),
	COMMAND_VIEW_HISTORY_PERMISSION("command.view-history-permission", false, "screenshareplugin.viewhistory"),
	COMMAND_INSUFICIENT_PERMISSIONS("command.insuficient-permissions", true,
			"&cVocê não possui Permissão para usar este Comando!"),
	COMMAND_USAGE("command.usage", true, "&cSintaxe incorreta, use: /<command> (Jogador)"),
	COMMAND_ONLY_PLAYERS_USE("command.only-players-use", true, "&cApenas Jogadores podem usar este Comando!"),
	COMMAND_OPEN_SESSIONS("command.open-sessions", true, "&aVocê abriu a Lista de Sessões!"),
	COMMAND_OPEN_HISTORY_SESSIONS("command.open-history-sessions", true, "&aVocê abriu o Histórico de Sessões!"),
	COMMAND_OPEN_CONFIG("command.open-config", true, "&eVocê abriu o Menu de Configurações."),
	COMMAND_OPEN_PLAYER_INFO("command.open-player-info", true, "&aVocê abriu as Informações de <nickname>!"),
	SESSION_SPY_PERMISSION("session.spy-permission", false, "screenshareplugin.spy"),
	SESSION_INITIATE_SESSION("session.initiate-session", true, "&aVocê puxou <nickname> para uma ScreenShare!"),
	SESSION_FINALIZE_SESSION("session.finalize-session", true, "&aVocê finalizou a ScreenShare de <nickname>!"),
	SESSION_JOIN_SESSION("session.join-session", true, "&aVocê entrou na Sessão de <nickname>!"),
	SESSION_LEAVE_SESSION("session.leave-session", true, "&cVocê saiu da Sessão de <nickname>!"),
	SESSION_USE_COMMANDS("session.use-commands", true, "&cVocê não pode executar Comandos na ScreenShare."),
	SESSION_START_SESSION("session.start-session", true,
			Arrays.asList("&aVocê foi puxado para a ScreenShare!",
					"&eBaixe o aplicativo AnyDesk, e siga todos os passos que o Staff falar.")),
	SESSION_END_SESSION("session.end-session", true,
			Arrays.asList("&aSua ScreenShare foi finalizada!",
					"&7Obrigado por cooperar com o Servidor, pedimos desculpas pelo incomodo!")),
	SESSION_LOGOUT_SESSION("session.logout-session", true, "&c<nickname> deslogou durante a ScreenShare!"),
	SESSION_LOGS_INITIATE_SESSION("session.logs.initiate-session", true, "&7(<nickname> iniciou a Sessão #<id>)"),
	SESSION_LOGS_FINALIZE_SESSION("session.logs.finalize-session", true, "&7(<nickname> finalizou a Sessão #<id>)"),
	SESSION_LOGS_JOIN_SESSION("session.logs.join-session", true, "&7(<nickname> entrou na Sessão #<id>)"),
	SESSION_LOGS_LEAVE_SESSION("session.logs.leave-session", true, "&7(<nickname> saiu da Sessão #<id>)"),
	SESSION_CHAT_FORMAT("session.chat-format", false, "&7(SS #<id>) &r<nickname> &8» &r<message>");

	private final String path;
	private final boolean isMultipleLine;
	private List<String> defaultMessages, messages;

	private ScreenShareMessages(String path, boolean isMultipleLine, String defaultMessage) {
		this(path, isMultipleLine, Arrays.asList(defaultMessage));
	}

	private ScreenShareMessages(String path, boolean isMultipleLine, List<String> defaultMessages) {
		this.path = path;
		this.isMultipleLine = isMultipleLine;
		this.defaultMessages = defaultMessages;
		this.messages = new ArrayList<>();
	}

	public String getPath() {
		return this.path;
	}

	public boolean isMultipleLine() {
		return this.isMultipleLine;
	}

	public List<String> getDefaultMessages() {
		return this.defaultMessages;
	}

	public List<String> getMessages() {
		YamlConfiguration config = ScreenSharePlugin.getConfig().getMessages();
		if (config.isString(this.getPath()))
			return Arrays.asList(ChatColor.translateAlternateColorCodes('&', config.getString(this.getPath())));

		this.messages = new ArrayList<>();
		config.getStringList(this.getPath())
				.forEach(line -> this.messages.add(ChatColor.translateAlternateColorCodes('&', line)));
		return this.messages;
	}

	public void setMessage(List<String> messages) {
		ScreenSharePlugin.getConfig().getMessages().set(this.getPath(), messages);
		ScreenSharePlugin.getConfig().saveMessages();
	}

	@Override
	public String toString() {
		return this.getMessages().get(0);
	}

	public ScreenShareMessages replace(String oldChar, String newChar) {
		for (int index = 0; index < this.getMessages().size(); index++)
			this.getMessages().set(index, this.getMessages().get(index).replace(oldChar, newChar));
		return this;
	}

	public void sendMessage(CommandSender sender) {
		this.getMessages().forEach(messages -> sender.sendMessage(messages));
	}
}