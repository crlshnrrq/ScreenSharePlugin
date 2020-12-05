package com.github.crlshnrrq.screenshareplugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public enum ScreenSharePermissions {
	USE_COMMAND("screenshareplugin.use", "Permissão para usar o Comando Principal do Plugin de ScreenShare.",
			PermissionDefault.OP),
	VIEW_SESSIONS("screenshareplugin.viewsessions", "Permissão para ver as Sessões de ScreenShare em Andamento.",
			PermissionDefault.OP),
	VIEW_HISTORY("screenshareplugin.viewhistory", "Permissão para ver o Histórico de Sessões de ScreenShare's.",
			PermissionDefault.OP),
	OPEN_CONFIG("screenshareplugin.config", "Permissão para abrir o Menu de Configurações do Plugin de ScreenShare.",
			PermissionDefault.OP),
	SPY_SESSIONS("screenshareplugin.spy", "Permissão para espionar os Registros das Sessões em Andamento.",
			PermissionDefault.OP),
	SCREENSHARE_BYPASS("screenshareplugin.bypass", "Permissão para burlar proibições do Plugin.", PermissionDefault.OP);

	private Permission permission;

	private ScreenSharePermissions(String name, String description, PermissionDefault defaultValue) {
		Bukkit.getPluginManager().addPermission(this.permission = new Permission(name, description, defaultValue));
	}

	@Override
	public String toString() {
		return this.permission.getName();
	}

	public Permission toPermission() {
		return this.permission;
	}

	public String getName() {
		return this.permission.getName();
	}

	public void setName(String name) {
		Bukkit.getScheduler().runTask(ScreenSharePlugin.getPlugin(), () -> {
			Bukkit.getPluginManager().removePermission(this.toPermission());
			Bukkit.getPluginManager()
					.addPermission(this.permission = new Permission(name, this.getDescription(), this.getDefault()));
			ScreenSharePlugin.getConfig().getPermissions().set(this.name().toLowerCase().replace("_", "-") + ".name",
					name);
			ScreenSharePlugin.getConfig().savePermissions();
		});
	}

	public String getDescription() {
		return this.permission.getDescription();
	}

	public void setDescription(String description) {
		Bukkit.getScheduler().runTask(ScreenSharePlugin.getPlugin(), () -> {
			this.permission.setDescription(description);
			ScreenSharePlugin.getConfig().getPermissions()
					.set(this.name().toLowerCase().replace("_", "-") + ".description", description);
			ScreenSharePlugin.getConfig().savePermissions();
		});
	}

	public PermissionDefault getDefault() {
		return this.permission.getDefault();
	}

	public void setDefault(PermissionDefault permissionDefault) {
		Bukkit.getScheduler().runTask(ScreenSharePlugin.getPlugin(), () -> {
			this.permission.setDefault(permissionDefault);
			ScreenSharePlugin.getConfig().getPermissions().set(this.name().toLowerCase().replace("_", "-") + ".default",
					permissionDefault.name());
			ScreenSharePlugin.getConfig().savePermissions();
		});
	}

	public static ScreenSharePermissions getPermissionByName(String name) {
		for (ScreenSharePermissions permissions : values()) {
			if (permissions.getName().equalsIgnoreCase(name))
				return permissions;
		}
		return null;
	}
}