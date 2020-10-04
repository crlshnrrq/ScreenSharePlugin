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
import com.sudano.sdscreenshare.ScreenShareAPI;

public final class ScreenShareInfoGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && event.getInventory().getName().startsWith("ScreenShare de ")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String suspect = event.getInventory().getName().replace("ScreenShare de ", "");
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			ScreenShare ss = ScreenSharePlugin.getScreenShareBySuspect(suspect);
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			if (display.equals("§aEntrar na ScreenShare"))
				ScreenShareAPI.joinScreenShare(player, ss);
			if (display.equals("§cSair da ScreenShare"))
				ScreenShareAPI.quitScreenShare(player, ss);
			if (display.equals("§cFinalizar ScreenShare"))
				ScreenShareAPI.finalizeScreenShare(player, ss);
		}
	}

	public static boolean openGUI(Player player, int id) {
		ScreenShare ss = ScreenSharePlugin.getScreenShareByID(id);
		if (ss != null) {
			openGUI(player, ss);
			return true;
		}
		return false;
	}

	public static boolean openGUI(Player player, String suspect) {
		ScreenShare ss = ScreenSharePlugin.getScreenShareBySuspect(suspect);
		if (ss != null) {
			openGUI(player, ss);
			return true;
		}
		return false;
	}

	public static void openGUI(Player player, ScreenShare ss) {
		Inventory inv = Bukkit.createInventory(null, 54, "ScreenShare de " + ss.getSuspect());

		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		ItemMeta mVidro = vidro.getItemMeta();
		mVidro.setDisplayName(" ");
		vidro.setItemMeta(mVidro);

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta mHead = (SkullMeta) head.getItemMeta();
		mHead.setOwner(ss.getSuspect());
		mHead.setDisplayName("§6Informações");
		ArrayList<String> headLore = new ArrayList<>();
		headLore.add(" ");
		headLore.add("§7Suspeito: §c" + ss.getSuspect());
		headLore.add("§7Autor: §a" + ss.getAuthor());
		headLore.add(" ");
		mHead.setLore(headLore);
		head.setItemMeta(mHead);
		inv.setItem(13, head);

		if (ss.getAuthor().equalsIgnoreCase(player.getName())) {
			ItemStack sair = new ItemStack(Material.ENDER_PEARL);
			ItemMeta mSair = sair.getItemMeta();
			mSair.setDisplayName("§7Sair da ScreenShare");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§cO Autor da ScreenShare não pode sair da sessão");
			mSair.setLore(lore);
			sair.setItemMeta(mSair);
			inv.setItem(37, sair);
		} else if (ScreenShareAPI.hasScreenShare(player)
				&& ScreenShareAPI.getScreenShare(player).getID() == ss.getID()) {
			ItemStack sair = new ItemStack(Material.ENDER_PEARL);
			ItemMeta mSair = sair.getItemMeta();
			mSair.setDisplayName("§cSair da ScreenShare");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7Clique para sair da sessão");
			mSair.setLore(lore);
			sair.setItemMeta(mSair);
			inv.setItem(37, sair);
		} else {
			ItemStack entrar = new ItemStack(Material.EYE_OF_ENDER);
			ItemMeta mEntrar = entrar.getItemMeta();
			mEntrar.setDisplayName("§aEntrar na ScreenShare");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7Clique para entrar na sessão");
			mEntrar.setLore(lore);
			entrar.setItemMeta(mEntrar);
			inv.setItem(37, entrar);
		}

		if (ss.getAuthor().equalsIgnoreCase(player.getName()) || player.hasPermission("sdscreenshare.bypass")) {
			ItemStack finalizar = new ItemStack(Material.INK_SACK, 1, (short) 1);
			ItemMeta mFinalizar = finalizar.getItemMeta();
			mFinalizar.setDisplayName("§cFinalizar ScreenShare");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7Clique para finalizar a sessão");
			mFinalizar.setLore(lore);
			finalizar.setItemMeta(mFinalizar);
			inv.setItem(38, finalizar);
		} else {
			ItemStack finalizar = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta mFinalizar = finalizar.getItemMeta();
			mFinalizar.setDisplayName("§7Finalizar ScreenShare");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§cApenas o Autor pode finalizar a sessão");
			mFinalizar.setLore(lore);
			finalizar.setItemMeta(mFinalizar);
			inv.setItem(38, finalizar);
		}

		player.openInventory(inv);
	}
}