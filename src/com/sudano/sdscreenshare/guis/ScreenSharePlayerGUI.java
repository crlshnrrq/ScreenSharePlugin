package com.sudano.sdscreenshare.guis;

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

import com.sudano.sdscreenshare.ScreenShare;
import com.sudano.sdscreenshare.ScreenShareAPI;
import com.sudano.sdscreenshare.ScreenSharePlugin;

public final class ScreenSharePlayerGUI implements Listener {

	@EventHandler
	private void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player && event.getInventory().getName().startsWith("Informa��es de ")
				&& event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			String display = event.getCurrentItem().getItemMeta().getDisplayName();
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);

			String nickname = event.getInventory().getName().replace("Informa��es de ", "");
			Player target = Bukkit.getPlayer(nickname);
			ScreenShare ss = ScreenSharePlugin.getScreenShareBySuspect(nickname);

			if (display.equals("�ePuxar para ScreenShare")) {
				if (target != null) {
					if (ss == null)
						ScreenShareAPI.createScreenShare(player, target);
					else
						player.sendMessage("�c" + target.getName() + " j� est� em uma ScreenShare!");
				} else
					player.sendMessage("�c" + nickname + " n�o foi encontrado!");
				player.closeInventory();
			}
			if (display.equals("�cFinalizar a ScreenShare")) {
				if (ss != null)
					ScreenShareAPI.finalizeScreenShare(player, ss);
				else
					player.sendMessage("�cA ScreenShare j� foi finalizada!");
				player.closeInventory();
			}
			if (display.equals("�aEntrar na Sess�o")) {
				if (ss != null)
					ScreenShareAPI.joinScreenShare(player, ss);
				else
					player.sendMessage("�cA ScreenShare j� foi finalizada!");
				player.closeInventory();
			}
			if (display.equals("�cSair da Sess�o")) {
				if (ss != null)
					ScreenShareAPI.quitScreenShare(player, ss);
				else
					player.sendMessage("�cA ScreenShare j� foi finalizada!");
				player.closeInventory();
			}
		}
	}

	public static void openGUI(Player player, String nickname) {
		Inventory inv = Bukkit.createInventory(null, 54, "Informa��es de " + nickname);

		ItemStack glass = new ItemStack(Material.THIN_GLASS);
		for (int i = 0; i < 54; i++) {
			if (i >= 38 && i <= 42)
				continue;
			inv.setItem(i, glass);
		}

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta mHead = (SkullMeta) head.getItemMeta();
		mHead.setOwner(nickname);
		mHead.setDisplayName("�6" + nickname);
		head.setItemMeta(mHead);
		inv.setItem(13, head);

		Player target = Bukkit.getPlayer(nickname);
		if (target != null) {
			if (ScreenShareAPI.hasScreenShare(target)) {
				ScreenShare ss = ScreenShareAPI.getScreenShare(target);

				if (ss.getAuthor().equalsIgnoreCase(player.getName()) || player.hasPermission("sdscreenshare.bypass")) {
					ItemStack finalizar = new ItemStack(Material.INK_SACK, 1, (short) 1);
					ItemMeta mFinalizar = finalizar.getItemMeta();
					mFinalizar.setDisplayName("�cFinalizar a ScreenShare");
					ArrayList<String> lore = new ArrayList<>();
					lore.add("�7Clique para finalizar a sess�o");
					mFinalizar.setLore(lore);
					finalizar.setItemMeta(mFinalizar);
					inv.setItem(inv.firstEmpty(), finalizar);
				}

				if (!ss.getAuthor().equalsIgnoreCase(player.getName())
						&& !ss.getSuspect().equalsIgnoreCase(player.getName())) {
					if (ss.getAllPlayersInScreenShare().contains(player.getName())) {
						ItemStack sair = new ItemStack(Material.ENDER_PEARL);
						ItemMeta mSair = sair.getItemMeta();
						mSair.setDisplayName("�cSair da Sess�o");
						ArrayList<String> lore = new ArrayList<>();
						lore.add("�7Clique para sair");
						mSair.setLore(lore);
						sair.setItemMeta(mSair);
						inv.setItem(inv.firstEmpty(), sair);
					} else {
						ItemStack entrar = new ItemStack(Material.EYE_OF_ENDER);
						ItemMeta mEntrar = entrar.getItemMeta();
						mEntrar.setDisplayName("�aEntrar na Sess�o");
						ArrayList<String> lore = new ArrayList<>();
						lore.add("�7Clique para entrar");
						mEntrar.setLore(lore);
						entrar.setItemMeta(mEntrar);
						inv.setItem(inv.firstEmpty(), entrar);
					}
				}

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
			} else if (!player.getName().equals(nickname)) {
				ItemStack puxar = new ItemStack(Material.NETHER_STAR);
				ItemMeta mPuxar = puxar.getItemMeta();
				mPuxar.setDisplayName("�ePuxar para ScreenShare");
				puxar.setItemMeta(mPuxar);
				inv.setItem(inv.firstEmpty(), puxar);
			}
		}

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