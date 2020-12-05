package com.github.crlshnrrq.screenshareplugin.configuration;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;

public final class ScreenShareConfigAPI {

	private static final HashMap<UUID, Edit<?>> editMap = new HashMap<>();

	public static boolean isEditing(Player player) {
		return editMap.containsKey(player.getUniqueId());
	}

	public static Edit<?> getEdit(Player player) {
		return editMap.get(player.getUniqueId());
	}

	public static void setEdit(Player player, Edit<?> edit) {
		editMap.put(player.getUniqueId(), edit);
	}

	public static Edit<?> removeEdit(Player player) {
		return editMap.remove(player.getUniqueId());
	}

	public static class Edit<E> {

		private final E modifier;
		private final ConfigModificationType modification;

		public Edit(E modifier, ConfigModificationType modification) {
			this.modifier = modifier;
			this.modification = modification;
		}

		public E getModifier() {
			return this.modifier;
		}

		public ConfigModificationType getModification() {
			return this.modification;
		}
	}
}