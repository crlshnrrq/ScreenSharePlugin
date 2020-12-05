package com.github.crlshnrrq.screenshareplugin.configuration.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePermissions;
import com.github.crlshnrrq.screenshareplugin.configuration.ConfigModificationType;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI;

public final class ScreenShareConfigPermissionsGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Configurar Permissões ") && event.getCurrentItem() != null
				&& event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
//			int page = Integer.parseInt(event.getInventory().getName().replace("Configurar Permissões #", ""));
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

//			if (display.equals("§aPágina Anterior"))
//				openGUI(player, page - 1);
//			if (display.equals("§aPágina Posterior"))
//				openGUI(player, page + 1);
			if (display.equals("§aVoltar ao Menu anterior"))
				ScreenShareConfigGUI.openGUI(player);

			if (display.startsWith("§e"))
				ScreenShareConfigPermissionGUI.openGUI(player,
						ScreenSharePermissions.getPermissionByName(ChatColor.stripColor(display)));
		}
	}

//	public static void openGUI(Player player) {
//		openGUI(player, 1);
//	}

	public static void openGUI(Player player/* , int page */) {
		Inventory inv = Bukkit.createInventory(null, 54, "Configurar Permissões "/* + (page < 10 ? "0" : "") + page */);

//		if (page > 1) {
		ItemStack voltar = new ItemStack(Material.ARROW);
		ItemMeta mVoltar = voltar.getItemMeta();
		mVoltar.setDisplayName("§aPágina Anterior");
		voltar.setItemMeta(mVoltar);
		inv.setItem(45, voltar);
//		} else {
//			ItemStack voltar = new ItemStack(Material.ARROW);
//			ItemMeta mVoltar = voltar.getItemMeta();
//			mVoltar.setDisplayName("§7Página Anterior");
//			voltar.setItemMeta(mVoltar);
//			inv.setItem(45, voltar);
//		}

//		ScreenSharePermissions[] permissions = ScreenSharePermissions.values();
//		for (int index = ((page - 1) * 45) + 1; index <= page * 45; index++) {
		for (ScreenSharePermissions permission : ScreenSharePermissions.values()) {
//			if (permissions.length >= index) {
//				ScreenSharePermissions permission = permissions[index - 1];
//				if (permission != null) {
			ItemStack item = new ItemStack(Material.NAME_TAG);
			ItemMeta mItem = item.getItemMeta();
			mItem.setDisplayName("§e" + permission.getName());
			ArrayList<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add(" §8» §fPadrão: §7" + permission.getDefault().name());
			lore.add(" §8» §fDescrição:");
			lore.add("    §7" + permission.getDescription());
			lore.add(" ");
			mItem.setLore(lore);
			item.setItemMeta(mItem);
			inv.setItem(inv.firstEmpty(), item);
		}

		ItemStack voltarMenu = new ItemStack(Material.ARROW);
		ItemMeta mVoltarMenu = voltarMenu.getItemMeta();
		mVoltarMenu.setDisplayName("§aVoltar ao Menu anterior");
		voltarMenu.setItemMeta(mVoltarMenu);
		inv.setItem(48, voltarMenu);
//			}
//				}

//		double pages = Math.ceil((double) ScreenSharePermissions.values().length / 45);
//		if (pages > page) {
//			ItemStack avancar = new ItemStack(Material.ARROW);
//			ItemMeta mAvancar = avancar.getItemMeta();
//			mAvancar.setDisplayName("§aPágina Posterior");
//			avancar.setItemMeta(mAvancar);
//			inv.setItem(53, avancar);
//		} else {
		ItemStack avancar = new ItemStack(Material.ARROW);
		ItemMeta mAvancar = avancar.getItemMeta();
		mAvancar.setDisplayName("§7Página Posterior");
		avancar.setItemMeta(mAvancar);
		inv.setItem(53, avancar);
//		}

		player.openInventory(inv);
	}
}