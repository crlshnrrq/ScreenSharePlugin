package com.github.crlshnrrq.screenshareplugin.configuration.gui;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.permissions.PermissionDefault;

import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;
import com.github.crlshnrrq.screenshareplugin.ScreenSharePermissions;
import com.github.crlshnrrq.screenshareplugin.configuration.ConfigModificationType;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI.Edit;

public final class ScreenShareConfigPermissionGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && event.getInventory().getName().startsWith("Permissão: ")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			ScreenSharePermissions permission = ScreenSharePermissions
					.getPermissionByName(event.getInventory().getName().replace("Permissão: ", ""));
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("§aVoltar ao Menu anterior"))
				ScreenShareConfigPermissionsGUI.openGUI(player);
			if (display.equals("§aEditar Nome")) {
				ScreenShareConfigAPI.setEdit(player,
						new Edit<ScreenSharePermissions>(permission, ConfigModificationType.EDIT_PERMISSION_NAME));
				player.sendMessage(
						"§aInsira o novo nome que deseja aplicar na Permissão " + permission.getName() + ":");
				player.closeInventory();
			}
			if (display.equals("§aEditar Descrição")) {
				ScreenShareConfigAPI.setEdit(player, new Edit<ScreenSharePermissions>(permission,
						ConfigModificationType.EDIT_PERMISSION_DESCRIPTION));
				player.sendMessage(
						"§aInsira a nova descrição que deseja aplicar na Permissão " + permission.getName() + ":");
				player.closeInventory();
			}
			if (display.equals("§aEditar Padrão")) {
				int ordinal = permission.getDefault().ordinal();
				PermissionDefault[] values = PermissionDefault.values();
				if ((values.length - 1) > ordinal)
					ordinal += 1;
				else
					ordinal = 0;

				PermissionDefault permissionDefault = PermissionDefault.values()[ordinal];
				permission.setDefault(permissionDefault);
				openGUI(player, permission);
			}
		}
	}

	public static void openGUI(Player player, ScreenSharePermissions permission) {
		Inventory inv = Bukkit.createInventory(null, 54, "Permissão: " + permission.getName());

		ItemStack glass = new ItemStack(Material.THIN_GLASS);
		for (int i = 0; i < 54; i++) {
			if (i >= 38 && i <= 42)
				continue;
			inv.setItem(i, glass);
		}

		ItemStack voltarMenu = new ItemStack(Material.ARROW);
		ItemMeta mVoltarMenu = voltarMenu.getItemMeta();
		mVoltarMenu.setDisplayName("§aVoltar ao Menu anterior");
		voltarMenu.setItemMeta(mVoltarMenu);
		inv.setItem(45, voltarMenu);

		ItemStack item = new ItemStack(Material.NAME_TAG);
		ItemMeta mItem = item.getItemMeta();
		mItem.setDisplayName("§e" + permission.getName());
		ArrayList<String> lore = new ArrayList<>();
		lore.add(" ");
		lore.add(" §8» §fPadrão: §7" + permission.getDefault().toString().toUpperCase());
		lore.add(" §8» §fDescrição:");
		lore.add("    §7" + permission.getDescription());
		lore.add(" ");
		mItem.setLore(lore);
		item.setItemMeta(mItem);
		inv.setItem(13, item);

		ItemStack permissionName = new ItemStack(Material.PAPER);
		ItemMeta mPermissionName = permissionName.getItemMeta();
		mPermissionName.setDisplayName("§aEditar Nome");
		mPermissionName.setLore(Arrays.asList("§7Clique para alterar o nome da Permissão."));
		permissionName.setItemMeta(mPermissionName);
		inv.addItem(permissionName);

		ItemStack permissionDescription = new ItemStack(Material.BOOK);
		ItemMeta mPermissionDescription = permissionDescription.getItemMeta();
		mPermissionDescription.setDisplayName("§aEditar Descrição");
		mPermissionDescription.setLore(Arrays.asList("§7Clique para alterar a descrição da Permissão."));
		permissionDescription.setItemMeta(mPermissionDescription);
		inv.addItem(permissionDescription);

		ItemStack permissionDefault = new ItemStack(Material.DIODE);
		ItemMeta mPermissionDefault = permissionDefault.getItemMeta();
		mPermissionDefault.setDisplayName("§aEditar Padrão");
		List<String> permissionDefaultLore = new ArrayList<>();
		for (PermissionDefault permissionDefaultValue : PermissionDefault.values()) {
			if (permissionDefaultValue == permission.getDefault())
				permissionDefaultLore.add(" §8» §a" + permissionDefaultValue.name());
			else
				permissionDefaultLore.add(" §8» §7" + permissionDefaultValue.name());
		}
		mPermissionDefault.setLore(permissionDefaultLore);
		permissionDefault.setItemMeta(mPermissionDefault);
		inv.addItem(permissionDefault);

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