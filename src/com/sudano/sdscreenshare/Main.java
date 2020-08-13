package com.sudano.sdscreenshare;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sudano.sdscreenshare.comandos.ScreenShare;
import com.sudano.sdscreenshare.eventos.EventosSS;

public class Main extends JavaPlugin {

	public static Plugin plugin;
	public static Main instance;

	public static String mundo_nome;
	public static String mundoss_nome;
	
	public static String retirado_ss;
	public static String colocado_ss;
	
	public static String comandoban;
	public static String banirquit;

	public static Main getInstace() {
		return instance;
	}

	public void onEnable() {
		plugin = this;
		instance = this;
		try {
			this.saveDefaultConfig();
		} catch (Exception var1_1) {
		}

		mundo_nome = this.getConfig().getString("Mundo");
		mundoss_nome = this.getConfig().getString("MundoSS");
		banirquit = this.getConfig().getString("BanirDesconectar");
		
		Bukkit.getConsoleSender().sendMessage("[sdScreenShare] - Plugin §ahabilitado§f.");

		World w_mundoss = Bukkit.getWorld(mundoss_nome);
		if (w_mundoss == null) {
			Bukkit.getConsoleSender().sendMessage(
					"[sdScreenShare] - §c§lERRO! §cNao foi possivel encontrar o mundo §f\"" + mundoss_nome + "§c\"");
			Bukkit.getConsoleSender()
					.sendMessage("[sdScreenShare] - §c§lERRO! §cArrume o nome dos mundos no arquivo §lplugin.yml§c!");
		}

		World w_mundo = Bukkit.getWorld(mundo_nome);
		if (w_mundo == null) {
			Bukkit.getConsoleSender().sendMessage(
					"[sdScreenShare] - §c§lERRO! §cNao foi possivel encontrar o mundo §f\"" + mundo_nome + "§c\"");
			Bukkit.getConsoleSender()
					.sendMessage("[sdScreenShare] - §c§lERRO! §cArrume o nome dos mundos no arquivo §lplugin.yml§c!");
		}
		
		getCommand("ss").setExecutor(new ScreenShare());
		getCommand("screenshare").setExecutor(new ScreenShare());
		
		Bukkit.getPluginManager().registerEvents(new EventosSS(), this);
	}
	

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("[sdScreenShare] - Plugin §cdesabilitado§f.");
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static Plugin getInstance() {
		return plugin;
	}

	public static Plugin instance() {
		return plugin;
	}

}
