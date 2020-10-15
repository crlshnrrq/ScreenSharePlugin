package com.sudano.sdscreenshare.guis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

import com.sudano.sdscreenshare.ScreenShare;
import com.sudano.sdscreenshare.ScreenSharePlugin;

public final class ScreenShareHistoryGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Hist�rico de Sess�es #") && event.getCurrentItem() != null
				&& event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			int page = Integer.parseInt(event.getInventory().getName().replace("Hist�rico de Sess�es #", ""));
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("�aP�gina Anterior"))
				openGUI(player, page - 1);
			if (display.equals("�aP�gina Posterior"))
				openGUI(player, page + 1);
			if (display.equals("�aSess�es em Andamento"))
				ScreenShareSessionsGUI.openGUI(player);
		}
	}

	public static void openGUI(Player player) {
		openGUI(player, 1);
	}

	public static void openGUI(Player player, int page) {
		Inventory inv = Bukkit.createInventory(null, 54, "Hist�rico de Sess�es #" + (page < 10 ? "0" : "") + page);

		if (page > 1) {
			ItemStack voltar = new ItemStack(Material.ARROW);
			ItemMeta mVoltar = voltar.getItemMeta();
			mVoltar.setDisplayName("�aP�gina Anterior");
			voltar.setItemMeta(mVoltar);
			inv.setItem(45, voltar);
		} else {
			ItemStack voltar = new ItemStack(Material.ARROW);
			ItemMeta mVoltar = voltar.getItemMeta();
			mVoltar.setDisplayName("�7P�gina Anterior");
			voltar.setItemMeta(mVoltar);
			inv.setItem(45, voltar);
		}

		List<String> history = ScreenSharePlugin.getConfig().getScreenShareHistory();
		Collections.reverse(history);

		List<ScreenShare> screenshares = new ArrayList<>();
		history.forEach(id -> screenshares.add(new ScreenShare(id)));

		for (int index = ((page - 1) * 45) + 1; index <= page * 45; index++) {
			if (screenshares.size() >= index) {
				ScreenShare ss = screenshares.get(index - 1);
				if (ss != null) {
					ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
					SkullMeta mItem = (SkullMeta) item.getItemMeta();
					mItem.setOwner(ss.getSuspect());
					mItem.setDisplayName("�6Informa��es da Sess�o #�7" + ss.getID());
					ArrayList<String> lore = new ArrayList<>();
					lore.add(" ");
					lore.add(" �8� �fSuspeito: �7" + ss.getSuspect());
					lore.add(" �8� �fAutor: �7" + ss.getAuthor());
					lore.add(" �8� �fIniciado em: �7" + ss.getIniciado());
					lore.add(" �8� �fFinalizado em: �7" + ss.getFinalizado());
					lore.add(" ");
					mItem.setLore(lore);
					item.setItemMeta(mItem);
					inv.setItem(inv.firstEmpty(), item);
				}
			}
		}

		ItemStack sessoes = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta mSessoes = sessoes.getItemMeta();
		mSessoes.setDisplayName("�aSess�es em Andamento");
		sessoes.setItemMeta(mSessoes);
		inv.setItem(49, sessoes);

		double pages = Math.ceil((double) ScreenSharePlugin.getScreenshares().size() / 45);
		if (pages > page) {
			ItemStack avancar = new ItemStack(Material.ARROW);
			ItemMeta mAvancar = avancar.getItemMeta();
			mAvancar.setDisplayName("�aP�gina Posterior");
			avancar.setItemMeta(mAvancar);
			inv.setItem(53, avancar);
		} else {
			ItemStack avancar = new ItemStack(Material.ARROW);
			ItemMeta mAvancar = avancar.getItemMeta();
			mAvancar.setDisplayName("�7P�gina Posterior");
			avancar.setItemMeta(mAvancar);
			inv.setItem(53, avancar);
		}

		player.openInventory(inv);
	}
}