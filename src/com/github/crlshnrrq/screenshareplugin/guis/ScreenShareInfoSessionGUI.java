package com.github.crlshnrrq.screenshareplugin.guis;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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
import org.jpaste.exceptions.PasteException;
import org.jpaste.pastebin.Pastebin;

import com.github.crlshnrrq.screenshareplugin.ScreenShare;
import com.github.crlshnrrq.screenshareplugin.ScreenShareAPI;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePlugin;

public final class ScreenShareInfoSessionGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Informa��es da Sess�o #")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			String id = event.getInventory().getName().replace("Informa��es da Sess�o #", "");
			ScreenShare ss = ScreenSharePlugin.getScreenShareById(id, true);

			if (display.equals("�aRegistros da Sess�o")) {
				try {
					String content = new String();
					for (String line : ss.getLogs()) {
						if (content.isEmpty())
							content += line;
						else
							content += "\n" + line;
					}

					URL url = Pastebin.pastePaste("6a32a05be10baa5380d618fff13ab968", content,
							"Registros da Sess�o #" + id);

					player.sendMessage("�aURL do Registro: " + url);
					player.closeInventory();
				} catch (PasteException ex) {
					ex.printStackTrace();
				}
			} else if (display.equals("�cExcluir a Sess�o")) {
				ScreenSharePlugin.getConfig().deleteScreenShare(id);
				player.sendMessage("�aVoc� excluiu a ScreenShare #" + id);
				player.closeInventory();
			}
		}
	}

	public static void openGUI(Player player, String id) {
		ScreenShare ss = ScreenSharePlugin.getScreenShareById(id, true);
		Inventory inv = Bukkit.createInventory(null, 54, "Informa��es da Sess�o #" + ss.getID());

		ItemStack glass = new ItemStack(Material.THIN_GLASS);
		for (int i = 0; i < 54; i++) {
			if (i >= 38 && i <= 42)
				continue;
			inv.setItem(i, glass);
		}

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta mHead = (SkullMeta) head.getItemMeta();
		mHead.setOwner(ss.getSuspect());
		mHead.setDisplayName("�6" + ss.getSuspect());
		head.setItemMeta(mHead);
		inv.setItem(13, head);

		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta mInfo = info.getItemMeta();
		mInfo.setDisplayName("�aInforma��es da Sess�o");
		ArrayList<String> infoLore = new ArrayList<>();
		infoLore.add(" ");
		infoLore.add(" �8� �fID da Sess�o: �7" + ss.getID());
		infoLore.add(" �8� �fIniciado em: �7" + ss.getIniciado());
		infoLore.add(" �8� �fFinalizado em: �7" + ss.getFinalizado());
		infoLore.add(" �8� �fEspectadores: �7" + ss.getSpectators().size());
		infoLore.add(" ");
		mInfo.setLore(infoLore);
		info.setItemMeta(mInfo);
		inv.setItem(inv.firstEmpty(), info);

		ItemStack logs = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta mLogs = logs.getItemMeta();
		mLogs.setDisplayName("�aRegistros da Sess�o");
		mLogs.setLore(Arrays.asList("�7Clique para receber um URL com os registros."));
		logs.setItemMeta(mLogs);
		inv.setItem(inv.firstEmpty(), logs);

		ItemStack delete = new ItemStack(Material.INK_SACK, 1, (short) 4);
		ItemMeta mDelete = delete.getItemMeta();
		mDelete.setDisplayName("�cExcluir a Sess�o");
		mDelete.setLore(Arrays.asList("�7Clique para apagar a Sess�o de forma permanente."));
		delete.setItemMeta(mDelete);
		inv.setItem(inv.firstEmpty(), delete);

		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta mVidro = vidro.getItemMeta();
		mVidro.setDisplayName(" ");
		vidro.setItemMeta(mVidro);
		while (inv.firstEmpty() != -1)
			inv.setItem(inv.firstEmpty(), vidro);

		inv.remove(glass);
		player.openInventory(inv);
	}
}