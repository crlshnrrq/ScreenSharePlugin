package com.github.crlshnrrq.screenshareplugin.configuration;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePermissions;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI.Edit;
import com.github.crlshnrrq.screenshareplugin.configuration.gui.ScreenShareConfigPermissionGUI;

public final class ScreenShareConfigListeners implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareConfigAPI.isEditing(player)) {
			Object modifier = ScreenShareConfigAPI.getEdit(player).getModifier();
			if (modifier instanceof ScreenShareMessages) {
				Edit<ScreenShareMessages> edit = (Edit<ScreenShareMessages>) ScreenShareConfigAPI.getEdit(player);

				if (edit.getModification() == ConfigModificationType.SET_MESSAGE) {
					List<String> messages = edit.getModifier().getMessages();
					messages.set(0, event.getMessage());
					edit.getModifier().setMessages(messages);
					ScreenShareConfigAPI.removeEdit(player);
					player.sendMessage("§aVocê definiu uma nova mensagem!");
				} else if (edit.getModification() == ConfigModificationType.ADD_MESSAGE) {
					List<String> messages = edit.getModifier().getMessages();
					messages.add(event.getMessage());
					edit.getModifier().setMessages(messages);
					ScreenShareConfigAPI.removeEdit(player);
					player.sendMessage("§aVocê adicionou uma nova mensagem!");
				} else if (edit.getModification() == ConfigModificationType.REMOVE_MESSAGE) {
					try {
						List<String> messages = edit.getModifier().getMessages();
						messages.remove(Integer.parseInt(event.getMessage()) - 1);
						edit.getModifier().setMessages(messages);
						ScreenShareConfigAPI.removeEdit(player);
						player.sendMessage("§aVocê removeu a linha " + event.getMessage() + "!");
					} catch (NumberFormatException ex) {
						player.sendMessage("§cA mensagem precisa ser um valor numérico!");
					}
				}
			} else if (modifier instanceof ScreenSharePermissions) {
				Edit<ScreenSharePermissions> edit = (Edit<ScreenSharePermissions>) ScreenShareConfigAPI.getEdit(player);

				if (edit.getModification() == ConfigModificationType.EDIT_PERMISSION_NAME) {
					ScreenSharePermissions permission = edit.getModifier();
					permission.setName(event.getMessage().split(" ")[0]);
					ScreenShareConfigPermissionGUI.openGUI(player, permission);
					player.sendMessage("§aVocê definiu um novo nome na Permissão!");
				} else if (edit.getModification() == ConfigModificationType.EDIT_PERMISSION_DESCRIPTION) {
					ScreenSharePermissions permission = edit.getModifier();
					permission.setDescription(event.getMessage());
					ScreenShareConfigPermissionGUI.openGUI(player, permission);
					player.sendMessage("§aVocê definiu uma nova descrição na Permissão!");
				}
			}

			event.setCancelled(true);
		}
	}
}