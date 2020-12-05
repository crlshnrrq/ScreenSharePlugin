package com.github.crlshnrrq.screenshareplugin.guis;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;
import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePermissions;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePlugin;

public final class ScreenShareSessionsGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Sessões de ScreenShare's #")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			int page = Integer.parseInt(event.getInventory().getName().replace("Sessões de ScreenShare's #", ""));
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("§aPágina Anterior"))
				openGUI(player, page - 1);
			if (display.equals("§aPágina Posterior"))
				openGUI(player, page + 1);
			if (display.equals("§aHistórico de Sessões"))
				ScreenShareHistoryGUI.openGUI(player);
			if (display.startsWith("§6Informações da Sessão #§7")) {
				ScreenShare ss = ScreenSharePlugin
						.getScreenShareById(display.replace("§6Informações da Sessão #§7", ""), false);
				if (ss != null)
					ScreenSharePlayerGUI.openGUI(player, ss.getSuspect());
			}
		}
	}

	public static void openGUI(Player player) {
		openGUI(player, 1);
	}

	public static void openGUI(Player player, int page) {
		Inventory inv = Bukkit.createInventory(null, 54, "Sessões de ScreenShare's #" + (page < 10 ? "0" : "") + page);

		if (page > 1) {
			ItemStack voltar = new ItemStack(Material.ARROW);
			ItemMeta mVoltar = voltar.getItemMeta();
			mVoltar.setDisplayName("§aPágina Anterior");
			voltar.setItemMeta(mVoltar);
			inv.setItem(45, voltar);
		} else {
			ItemStack voltar = new ItemStack(Material.ARROW);
			ItemMeta mVoltar = voltar.getItemMeta();
			mVoltar.setDisplayName("§7Página Anterior");
			voltar.setItemMeta(mVoltar);
			inv.setItem(45, voltar);
		}

		ArrayList<ScreenShare> screenshares = ScreenSharePlugin.getScreenshares();
		for (int index = ((page - 1) * 45) + 1; index <= page * 45; index++) {
			if (screenshares.size() >= index) {
				ScreenShare ss = screenshares.get(index - 1);
				if (ss != null) {
					ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					SkullMeta mItem = (SkullMeta) item.getItemMeta();
					mItem.setOwner(ss.getSuspect());
					mItem.setDisplayName("§6Informações da Sessão #§7" + ss.getID());
					ArrayList<String> lore = new ArrayList<>();
					lore.add(" ");
					lore.add(" §8» §fSuspeito: §7" + ss.getSuspect());
					lore.add(" §8» §fAutor: §7" + ss.getAuthor());
					lore.add(" §8» §fIniciado em: §7" + ss.getIniciado());
					lore.add(" §8» §fFinalizado em: §7" + ss.getFinalizado());
					lore.add(" ");
					mItem.setLore(lore);
					item.setItemMeta(mItem);
					inv.setItem(inv.firstEmpty(), item);
				}
			}
		}

		if (player.hasPermission(ScreenSharePermissions.VIEW_HISTORY.toPermission())) {
			ItemStack historico = new ItemStack(Material.BOOK);
			ItemMeta mHistorico = historico.getItemMeta();
			mHistorico.setDisplayName("§aHistórico de Sessões");
			historico.setItemMeta(mHistorico);
			inv.setItem(49, historico);
		} else {
			ItemStack historico = new ItemStack(Material.BOOK);
			ItemMeta mHistorico = historico.getItemMeta();
			mHistorico.setDisplayName("§7Histórico de Sessões");
			historico.setItemMeta(mHistorico);
			inv.setItem(49, historico);
		}

		double pages = Math.ceil((double) ScreenSharePlugin.getScreenshares().size() / 45);
		if (pages > page) {
			ItemStack avancar = new ItemStack(Material.ARROW);
			ItemMeta mAvancar = avancar.getItemMeta();
			mAvancar.setDisplayName("§aPágina Posterior");
			avancar.setItemMeta(mAvancar);
			inv.setItem(53, avancar);
		} else {
			ItemStack avancar = new ItemStack(Material.ARROW);
			ItemMeta mAvancar = avancar.getItemMeta();
			mAvancar.setDisplayName("§7Página Posterior");
			avancar.setItemMeta(mAvancar);
			inv.setItem(53, avancar);
		}

		player.openInventory(inv);
	}
}