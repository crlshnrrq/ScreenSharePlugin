package com.sudano.sdscreenshare;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sudano.sdscreenshare.events.TimeSecondEvent;
import com.sudano.sdscreenshare.guis.ScreenShareInfoGUI;
import com.sudano.sdscreenshare.guis.ScreenShareSessionsGUI;

public final class ScreenSharePlugin {

	private static JavaPlugin plugin;

	public ScreenSharePlugin(JavaPlugin instance) {
		plugin = instance;
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	private static final ArrayList<ScreenShare> screenshares = new ArrayList<>();

	public static ArrayList<ScreenShare> getScreenshares() {
		return screenshares;
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

	public void onLoad() {
		new WorldCreator("screenshare_world").createWorld();
	}

	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ScreenShareSessionsGUI(), getPlugin());
		pm.registerEvents(new ScreenShareInfoGUI(), getPlugin());

		pm.registerEvents(new ScreenShareListeners(), getPlugin());

		getPlugin().getCommand("screenshare").setExecutor(new ScreenShareCommand());

		Bukkit.getScheduler().runTaskTimer(getPlugin(),
				() -> Bukkit.getPluginManager().callEvent(new TimeSecondEvent()), 20L, 20L);
	}

	public void onDisable() {
		HandlerList.unregisterAll(getPlugin());
		Bukkit.getScheduler().cancelTasks(getPlugin());
	}
}