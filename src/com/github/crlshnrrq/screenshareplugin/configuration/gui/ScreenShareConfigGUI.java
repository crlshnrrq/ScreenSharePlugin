package com.github.crlshnrrq.screenshareplugin.configuration.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ScreenShareConfigGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && event.getInventory().getName().startsWith("Configurações")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("§ePermissões"))
				ScreenShareConfigPermissionsGUI.openGUI(player);
			if (display.equals("§eMensagens"))
				ScreenShareConfigMessagesGUI.openGUI(player);
		}
	}

	public static void openGUI(Player player) {
		Inventory inv = Bukkit.createInventory(null, 54, "Configurações");

		ItemStack glass = new ItemStack(Material.THIN_GLASS);
		for (int i = 0; i < 54; i++) {
			if (i >= 38 && i <= 42)
				continue;
			inv.setItem(i, glass);
		}

		ItemStack config = new ItemStack(Material.NETHER_STAR);
		ItemMeta mConfig = config.getItemMeta();
		mConfig.setDisplayName("§6Configurações");
		config.setItemMeta(mConfig);
		inv.setItem(13, config);

		ItemStack permissions = new ItemStack(Material.NAME_TAG);
		ItemMeta mPermissions = permissions.getItemMeta();
		mPermissions.setDisplayName("§ePermissões");
		permissions.setItemMeta(mPermissions);
		inv.addItem(permissions);

		ItemStack messages = new ItemStack(Material.PAPER);
		ItemMeta mMessages = messages.getItemMeta();
		mMessages.setDisplayName("§eMensagens");
		messages.setItemMeta(mMessages);
		inv.addItem(messages);

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