package com.github.crlshnrrq.screenshareplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public enum ScreenShareMessages {
	COMMAND_INSUFICIENT_PERMISSIONS("command.insuficient-permissions",
			"&cVocê não possui Permissão para usar este Comando!"),
	COMMAND_USAGE("command.usage", "&cSintaxe incorreta, use: /<command> (Jogador)"),
	COMMAND_ONLY_PLAYERS_USE("command.only-players-use", "&cApenas Jogadores podem usar este Comando!"),
	COMMAND_OPEN_SESSIONS("command.open-sessions", "&aVocê abriu a Lista de Sessões!"),
	COMMAND_OPEN_HISTORY_SESSIONS("command.open-history-sessions", "&aVocê abriu o Histórico de Sessões!"),
	COMMAND_OPEN_CONFIG("command.open-config", "&eVocê abriu o Menu de Configurações."),
	COMMAND_OPEN_PLAYER_INFO("command.open-player-info", "&aVocê abriu as Informações de <nickname>!"),
	SESSION_INITIATE_SESSION("session.initiate-session", "&aVocê puxou <nickname> para uma ScreenShare!"),
	SESSION_FINALIZE_SESSION("session.finalize-session", "&aVocê finalizou a ScreenShare de <nickname>!"),
	SESSION_JOIN_SESSION("session.join-session", "&aVocê entrou na Sessão de <nickname>!"),
	SESSION_LEAVE_SESSION("session.leave-session", "&cVocê saiu da Sessão de <nickname>!"),
	SESSION_USE_COMMANDS("session.use-commands", "&cVocê não pode executar Comandos na ScreenShare."),
	SESSION_START_SESSION("session.start-session", "&aVocê foi puxado para a ScreenShare!",
			"&eBaixe o aplicativo AnyDesk, e siga todos os passos que o Staff falar."),
	SESSION_END_SESSION("session.end-session", "&aSua ScreenShare foi finalizada!",
			"&7Obrigado por cooperar com o Servidor, pedimos desculpas pelo incomodo!"),
	SESSION_LOGOUT_SESSION("session.logout-session", "&c<nickname> deslogou durante a ScreenShare!"),
	SESSION_LOGS_INITIATE_SESSION("session.logs.initiate-session", "&7(<nickname> iniciou a Sessão #<id>)"),
	SESSION_LOGS_FINALIZE_SESSION("session.logs.finalize-session", "&7(<nickname> finalizou a Sessão #<id>)"),
	SESSION_LOGS_JOIN_SESSION("session.logs.join-session", "&7(<nickname> entrou na Sessão #<id>)"),
	SESSION_LOGS_LEAVE_SESSION("session.logs.leave-session", "&7(<nickname> saiu da Sessão #<id>)"),
	SESSION_CHAT_FORMAT("session.chat-format", "&7(SS #<id>) &r<nickname> &8» &r<message>");

	private final String path;
	private final boolean isMultipleLine;
	private List<String> messages;
	private Map<String, String> replaceMap;

	private ScreenShareMessages(String path, String message) {
		this(path, false, Arrays.asList(message));
	}

	private ScreenShareMessages(String path, String... messages) {
		this(path, Arrays.asList(messages));
	}

	private ScreenShareMessages(String path, List<String> messages) {
		this(path, true, messages);
	}

	private ScreenShareMessages(String path, boolean isMultipleLine, List<String> messages) {
		this.path = path;
		this.isMultipleLine = isMultipleLine;
		this.messages = messages;
		this.replaceMap = new HashMap<>();
	}

	public String getPath() {
		return this.path;
	}

	public boolean isMultipleLine() {
		return this.isMultipleLine;
	}

	public List<String> getMessages() {
		return this.messages;
	}

	public void setMessages(List<String> messages) {
		Bukkit.getScheduler().runTask(ScreenSharePlugin.getPlugin(), () -> {
			this.messages = messages;
			ScreenSharePlugin.getConfig().getMessages().set(this.getPath(),
					this.isMultipleLine() ? messages.get(0) : messages);
			ScreenSharePlugin.getConfig().saveMessages();
		});
	}

	public ScreenShareMessages replace(String oldChar, String newChar) {
		this.replaceMap.put(oldChar, newChar);
		return this;
	}

	public void sendMessage(CommandSender sender) {
		for (String message : new ArrayList<>(this.getMessages())) {
			for (Entry<String, String> entry : this.replaceMap.entrySet())
				message = message.replace(entry.getKey(), entry.getValue());
			sender.sendMessage(message.replace("&", "§"));
		}
	}

	@Override
	public String toString() {
		return this.getMessages().get(0);
	}

	public static ScreenShareMessages getScreenShareMessageByPath(String path) {
		for (ScreenShareMessages message : values()) {
			if (message.getPath().equalsIgnoreCase(path))
				return message;
		}
		return null;
	}
}