package com.github.crlshnrrq.screenshareplugin.configuration.gui;

import java.util.ArrayList;
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

import com.github.crlshnrrq.screenshareplugin.ScreenShareMessages;
import com.github.crlshnrrq.screenshareplugin.configuration.ConfigModificationType;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI;
import com.github.crlshnrrq.screenshareplugin.configuration.ScreenShareConfigAPI.Edit;

public final class ScreenShareConfigMessagesGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player
				&& event.getInventory().getName().startsWith("Configurar Mensagens ") && event.getCurrentItem() != null
				&& event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
//			int page = Integer.parseInt(event.getInventory().getName().replace("Configurar Mensagens #", ""));
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

//			if (display.equals("§aPágina Anterior"))
//				openGUI(player, page - 1);
//			if (display.equals("§aPágina Posterior"))
//				openGUI(player, page + 1);
			if (display.equals("§aVoltar ao Menu anterior"))
				ScreenShareConfigGUI.openGUI(player);

			if (display.startsWith("§e") && event.getCurrentItem().getItemMeta().hasLore()) {
				ScreenShareMessages message = ScreenShareMessages
						.getScreenShareMessageByPath(display.replace("§e", ""));
				if (message != null) {
					List<String> lore = event.getCurrentItem().getItemMeta().getLore();

					if (lore.contains("§8» §fBotão Esquerdo: §7Alterar mensagem") && event.isLeftClick()) {
						ScreenShareConfigAPI.setEdit(player,
								new Edit<ScreenShareMessages>(message, ConfigModificationType.SET_MESSAGE));
						player.sendMessage("§8Alterar Mensagem §7» §aEscreva a nova mensagem a ser definida:");
						player.closeInventory();
					}
					if (lore.contains("§8» §fBotão Direito: §7Adicionar mensagem") && event.isRightClick()) {
						ScreenShareConfigAPI.setEdit(player,
								new Edit<ScreenShareMessages>(message, ConfigModificationType.ADD_MESSAGE));
						player.sendMessage("§8Alterar Mensagem §7» §aEscreva a nova mensagem a ser adicionada:");
						player.closeInventory();
					}

					if (lore.contains("§8» §fBotão Esquerdo: §7Adicionar mensagem") && event.isLeftClick()) {
						ScreenShareConfigAPI.setEdit(player,
								new Edit<ScreenShareMessages>(message, ConfigModificationType.ADD_MESSAGE));
						player.sendMessage("§8Alterar Mensagem §7» §aEscreva a nova mensagem a ser adicionada:");
						player.closeInventory();
					}
					if (lore.contains("§8» §fBotão Direito: §7Remover mensagem") && event.isRightClick()) {
						ScreenShareConfigAPI.setEdit(player,
								new Edit<ScreenShareMessages>(message, ConfigModificationType.REMOVE_MESSAGE));
						player.sendMessage("§8Alterar Mensagem §7» §aDigite o número da linha a ser removida:");
						player.closeInventory();
					}
				}
			}
		}
	}

//	public static void openGUI(Player player) {
//		openGUI(player, 1);
//	}

	public static void openGUI(Player player/* , int page */) {
		Inventory inv = Bukkit.createInventory(null, 54, "Configurar Mensagens "/* + (page < 10 ? "0" : "") + page */);

//		if (page > 1) {
//			ItemStack voltar = new ItemStack(Material.ARROW);
//			ItemMeta mVoltar = voltar.getItemMeta();
//			mVoltar.setDisplayName("§aPágina Anterior");
//			voltar.setItemMeta(mVoltar);
//			inv.setItem(45, voltar);
//		} else {
		ItemStack voltar = new ItemStack(Material.ARROW);
		ItemMeta mVoltar = voltar.getItemMeta();
		mVoltar.setDisplayName("§7Página Anterior");
		voltar.setItemMeta(mVoltar);
		inv.setItem(45, voltar);
//		}

//		ScreenShareMessages[] messages = ScreenShareMessages.values();
//		for (int index = ((page - 1) * 45) + 1; index <= page * 45; index++) {
		for (ScreenShareMessages message : ScreenShareMessages.values()) {
//			if (messages.length >= index) {
//				ScreenShareMessages message = messages[index - 1];
//				if (message != null) {
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta mItem = item.getItemMeta();
			mItem.setDisplayName("§e" + message.getPath());
			ArrayList<String> lore = new ArrayList<>();
			lore.add(" ");
			lore.add(" §8» §fMensagens:");
			message.getMessages().forEach(line -> lore.add("    §r" + line));
			lore.add(" ");
			if (message.isMultipleLine()) {
				if (message.getMessages().size() == 1) {
					lore.add("§8» §fBotão Esquerdo: §7Alterar mensagem");
					lore.add("§8» §fBotão Direito: §7Adicionar mensagem");
				} else {
					lore.add("§8» §fBotão Esquerdo: §7Adicionar mensagem");
					lore.add("§8» §fBotão Direito: §7Remover mensagem");
				}
			} else
				lore.add("§8» §fBotão Esquerdo: §7Alterar mensagem");
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

//		double pages = Math.ceil((double) ScreenShareMessages.values().length / 45);
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