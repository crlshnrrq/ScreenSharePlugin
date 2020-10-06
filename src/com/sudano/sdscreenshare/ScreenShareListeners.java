package com.sudano.sdscreenshare;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

import com.sudano.sdscreenshare.events.PlayerScreenShareCreateEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareFinalizeEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareJoinEvent;
import com.sudano.sdscreenshare.events.PlayerScreenSharePulledEvent;
import com.sudano.sdscreenshare.events.PlayerScreenSharePushedEvent;
import com.sudano.sdscreenshare.events.PlayerScreenShareQuitEvent;
import com.sudano.sdscreenshare.events.TimeSecondEvent;

public class ScreenShareListeners implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onTimeSecond(TimeSecondEvent event) {
		Bukkit.getOnlinePlayers().stream().filter(player -> ScreenShareAPI.hasScreenShare(player))
				.forEach(player -> Bukkit.getOnlinePlayers().forEach(players -> {
					if (ScreenShareAPI.getScreenShare(player).getAllPlayersInScreenShare().contains(players.getName()))
						player.showPlayer(players);
					else
						player.hidePlayer(players);
				}));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenShareCreate(PlayerScreenShareCreateEvent event) {
		ScreenShare ss = event.getScreenShare();
		Player player = event.getPlayer();
		ss.announceMessage("§7(" + player.getName() + " criou a ScreenShare)");
		player.sendMessage("§aVocê puxou §l" + ss.getSuspect() + " §apara uma §lSCREENSHARE§a!");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenSharePulled(PlayerScreenSharePulledEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(" ");
		player.sendMessage("§cVocê foi puxado para a §lSCREENSHARE§c!");
		player.sendMessage("§cCaso você desconecte do Servidor, será §lAUTOMATICAMENTE §cbanido!");
		player.sendMessage("§cBaixe o aplicativo AnyDesk, e siga todos os passos que o Staff falar!");
		player.sendMessage(" ");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenShareJoin(PlayerScreenShareJoinEvent event) {
		ScreenShare ss = event.getScreenShare();
		Player player = event.getPlayer();
		ss.announceMessage("§7(" + player.getName() + " entrou na ScreenShare)");
		player.sendMessage("§aVocê entrou na ScreenShare de " + ss.getSuspect() + "!");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenShareFinalize(PlayerScreenShareFinalizeEvent event) {
		ScreenShare ss = event.getScreenShare();
		Player player = event.getPlayer();
		player.sendMessage("§aVocê finalizou a §lSCREENSHARE §ade §l" + event.getScreenShare().getSuspect() + "§a!");
		ss.announceMessage("§7(" + player.getName() + " finalizou a ScreenShare)");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenSharePushed(PlayerScreenSharePushedEvent event) {
		Player player = event.getPlayer();
		player.sendMessage(" ");
		player.sendMessage("§cVocê foi retirado da §lSCREENSHARE§c!");
		player.sendMessage("§cObrigado por cooperar com o Servidor, pedimos desculpas pelo incomodo!");
		player.sendMessage(" ");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerScreenShareJoin(PlayerScreenShareQuitEvent event) {
		ScreenShare ss = event.getScreenShare();
		Player player = event.getPlayer();
		ss.announceMessage("§7(" + player.getName() + " saiu da ScreenShare)");
		player.sendMessage("§cVocê saiu da ScreenShare de " + ss.getSuspect() + "!");
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		Player player = (Player) event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player)) {
			ScreenShare ss = ScreenShareAPI.getScreenShare(player);
			event.setCancelled(true);
			ss.getAllPlayersInScreenShare().forEach(nickname -> {
				Player players = Bukkit.getPlayer(nickname);
				if (players != null)
					players.sendMessage("§7(SS) §r" + player.getDisplayName() + " §8» §r" + event.getMessage());
			});
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerCommandPreproccess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player) && !player.hasPermission("sdscreenshare.use")) {
			event.setCancelled(true);
			player.sendMessage("§cVocê não pode executar Comandos na ScreenShare.");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		Bukkit.getOnlinePlayers().stream().filter(players -> ScreenShareAPI.hasScreenShare(players))
				.forEach(players -> players.hidePlayer(player));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setRespawnLocation(ScreenSharePlugin.getScreenShareLocation());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player)) {
			ScreenShare ss = ScreenShareAPI.getScreenShare(player);
			ScreenShareAPI.finalizeScreenShare(player, ss);
			ss.announceMessage("§c" + player.getName() + " deslogou durante a ScreenShare!");
		}
	}

	// +BLOCKS
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}
	// -BLOCKS

	// +ENTITY
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onEntityCombust(EntityCombustEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setCancelled(true);
		}
	}
	// -ENTITY

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setCancelled(true);
		}
	}

	// +PLAYER
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (ScreenShareAPI.hasScreenShare(player))
			event.setCancelled(true);
	}
	// -PLAYER

	// +VEHICLE

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onVehicleDamage(VehicleDamageEvent event) {
		if (event.getAttacker() instanceof Player) {
			Player player = (Player) event.getAttacker();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	private void onVehicleDestroy(VehicleDestroyEvent event) {
		if (event.getAttacker() instanceof Player) {
			Player player = (Player) event.getAttacker();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setCancelled(true);
		}
	}
	// -VEHICLE

	@EventHandler
	private void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (ScreenShareAPI.hasScreenShare(player))
				event.setFoodLevel(20);
		}
	}
}