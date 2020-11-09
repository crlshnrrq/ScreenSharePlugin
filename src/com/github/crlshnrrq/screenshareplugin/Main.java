package com.github.crlshnrrq.screenshareplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

	private static ScreenSharePlugin screenSharePlugin;

	public static ScreenSharePlugin getScreenSharePlugin() {
		return screenSharePlugin;
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		screenSharePlugin = new ScreenSharePlugin(this);
		getScreenSharePlugin().onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		getScreenSharePlugin().onDisable();
	}
}