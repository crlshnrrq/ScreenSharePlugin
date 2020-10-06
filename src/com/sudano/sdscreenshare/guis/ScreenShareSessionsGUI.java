package com.sudano.sdscreenshare.guis;

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

import com.sudano.sdscreenshare.ScreenSharePlugin;
import com.sudano.sdscreenshare.ScreenShare;

public final class ScreenShareSessionsGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Sess�es de ScreenShare's #")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			int page = Integer.parseInt(event.getInventory().getName().replace("Sess�es de ScreenShare's #", ""));
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("�aP�gina Anterior"))
				openGUI(player, page - 1);
			if (display.equals("�aP�gina Posterior"))
				openGUI(player, page + 1);
			if (display.startsWith("�6ScreenShare #")) {
				if (event.getCurrentItem().getItemMeta().hasLore()) {
					event.getCurrentItem().getItemMeta().getLore().stream()
							.filter(lines -> lines.startsWith("�7Suspeito: �c")).forEach(line -> {
								ScreenShare ss = ScreenSharePlugin
										.getScreenShareBySuspect(line.replace("�7Suspeito: �c", ""));
								if (ss != null)
									ScreenShareInfoGUI.openGUI(player, ss);
							});
				}
			}
		}
	}

	public static void openGUI(Player player) {
		openGUI(player, 1);
	}

	public static void openGUI(Player player, int page) {
		Inventory inv = Bukkit.createInventory(null, 54, "Sess�es de ScreenShare's #" + (page < 10 ? "0" : "") + page);

		if (page > 1) {
			ItemStack voltar = new ItemStack(Material.ARROW);
			ItemMeta mVoltar = voltar.getItemMeta();
			mVoltar.setDisplayName("�aP�gina Anterior");
			voltar.setItemMeta(mVoltar);
			inv.setItem(45, voltar);
		}

		for (int index = ((page - 1) * 45) + 1; index <= page * 45; index++) {
			ScreenShare ss = ScreenSharePlugin.getScreenshares().get(index);
			if (ss != null) {
				ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
				SkullMeta mItem = (SkullMeta) item.getItemMeta();
				mItem.setOwner(ss.getSuspect());
				mItem.setDisplayName("�6ScreenShare #" + (index < 10 ? "0" : "") + index);
				ArrayList<String> lore = new ArrayList<>();
				lore.add(" ");
				lore.add("�7Suspeito: �c" + ss.getSuspect());
				lore.add("�7Autor: �a" + ss.getAuthor());
				lore.add(" ");
				mItem.setLore(lore);
				item.setItemMeta(mItem);
				inv.setItem(inv.firstEmpty(), item);
			}
		}

		double pages = Math.ceil((double) ScreenSharePlugin.getScreenshares().size() / 45);
		if (pages > page) {
			ItemStack avancar = new ItemStack(Material.ARROW);
			ItemMeta mAvancar = avancar.getItemMeta();
			mAvancar.setDisplayName("�aP�gina Posterior");
			avancar.setItemMeta(mAvancar);
			inv.setItem(53, avancar);
		}

		player.openInventory(inv);
	}
}