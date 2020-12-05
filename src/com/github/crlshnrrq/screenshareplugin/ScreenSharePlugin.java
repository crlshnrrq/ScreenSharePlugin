package com.github.crlshnrrq.screenshareplugin;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigListeners;
import com.github.crlshnrrq.screenshareplugin.configuration.gui.ScreenShareConfigGUI;
import com.github.crlshnrrq.screenshareplugin.configuration.gui.ScreenShareConfigMessagesGUI;
import com.github.crlshnrrq.screenshareplugin.configuration.gui.ScreenShareConfigPermissionGUI;
import com.github.crlshnrrq.screenshareplugin.configuration.gui.ScreenShareConfigPermissionsGUI;
import com.github.crlshnrrq.screenshareplugin.events.TimeSecondEvent;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareHistoryGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareInfoSessionGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenSharePlayerGUI;
import com.github.crlshnrrq.screenshareplugin.guis.ScreenShareSessionsGUI;

public final class ScreenSharePlugin {

	private static JavaPlugin plugin;
	private static ScreenShareConfig config;

	public ScreenSharePlugin(JavaPlugin instance) {
		plugin = instance;
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	public static ScreenShareConfig getConfig() {
		return config;
	}

	private static final ArrayList<ScreenShare> screenshares = new ArrayList<>();

	public static ArrayList<ScreenShare> getScreenshares() {
		return screenshares;
	}

	public static ScreenShare getScreenShareById(String id, boolean filterHistory) {
		return getScreenshares().stream().filter(ss -> ss.getID().equals(id)).findFirst()
				.orElse(filterHistory ? new ScreenShare(id) : null);
	}

	public static ScreenShare getScreenShareBySuspect(String nickname) {
		return getScreenshares().stream().filter(ss -> ss.getSuspect().equalsIgnoreCase(nickname)).findFirst()
				.orElse(null);
	}

	public static Location getScreenShareLocation() {
		return Bukkit.getWorld("screenshare_world").getSpawnLocation();
	}

	public static Location getDefaultWorldLocation() {
		return Bukkit.getWorld("world").getSpawnLocation();
	}

	public void onEnable() {
		config = new ScreenShareConfig();

		Bukkit.getServer().createWorld(new WorldCreator("screenshare_world"));

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ScreenShareConfigGUI(), getPlugin());
		pm.registerEvents(new ScreenShareConfigMessagesGUI(), getPlugin());
		pm.registerEvents(new ScreenShareConfigPermissionGUI(), getPlugin());
		pm.registerEvents(new ScreenShareConfigPermissionsGUI(), getPlugin());
		pm.registerEvents(new ScreenShareHistoryGUI(), getPlugin());
		pm.registerEvents(new ScreenShareInfoSessionGUI(), getPlugin());
		pm.registerEvents(new ScreenShareSessionsGUI(), getPlugin());
		pm.registerEvents(new ScreenSharePlayerGUI(), getPlugin());

		pm.registerEvents(new ScreenShareListeners(), getPlugin());
		pm.registerEvents(new ScreenShareConfigListeners(), getPlugin());

		getPlugin().getCommand("screenshare").setExecutor(new ScreenShareCommand());

		Bukkit.getScheduler().runTaskTimer(getPlugin(),
				() -> Bukkit.getPluginManager().callEvent(new TimeSecondEvent()), 20L, 20L);
	}

	public void onDisable() {
		HandlerList.unregisterAll(getPlugin());
		Bukkit.getScheduler().cancelTasks(getPlugin());
	}
}