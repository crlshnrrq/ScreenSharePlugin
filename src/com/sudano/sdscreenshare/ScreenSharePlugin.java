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

	private final JavaPlugin plugin;

	public ScreenSharePlugin(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public JavaPlugin getPlugin() {
		return this.plugin;
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
		pm.registerEvents(new ScreenShareSessionsGUI(), this.getPlugin());
		pm.registerEvents(new ScreenShareInfoGUI(), this.getPlugin());

		pm.registerEvents(new ScreenShareListeners(), this.getPlugin());

		this.getPlugin().getCommand("screenshare").setExecutor(new ScreenShareCommand());

		Bukkit.getScheduler().runTaskTimer(this.getPlugin(),
				() -> Bukkit.getPluginManager().callEvent(new TimeSecondEvent()), 20L, 20L);
	}

	public void onDisable() {
		HandlerList.unregisterAll(this.getPlugin());
		Bukkit.getScheduler().cancelTasks(this.getPlugin());
	}
}