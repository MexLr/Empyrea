package org.example.ataraxiawarmup.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.ItemAttribute;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.ArrayList;
import java.util.List;

public class MenuInventory {

    private Inventory inv;

    public MenuInventory(Player player) {
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);

        inv = Bukkit.createInventory(null, 54, "Eidolon Menu");

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.FILLER_ITEM);
        }

        ItemStack damageStatsSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta damageMeta = damageStatsSword.getItemMeta();
        damageMeta.setDisplayName(ChatColor.DARK_AQUA + "Your Elemental Damages");
        List<String> damageLore = new ArrayList<>();

        ItemStack defenseStatsChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta defenseMeta = defenseStatsChestplate.getItemMeta();
        defenseMeta.setDisplayName(ChatColor.DARK_AQUA + "Your Elemental Defenses");
        List<String> defenseLore = new ArrayList<>();
        defenseLore.add(ChatColor.DARK_RED + "♥" + customPlayer.getMaxHealth());
        defenseLore.add("");

        ItemStack miscStatsCompass = new ItemStack(Material.COMPASS);
        ItemMeta miscMeta = miscStatsCompass.getItemMeta();
        miscMeta.setDisplayName(ChatColor.DARK_AQUA + "Your Other Stats");
        List<String> miscLore = new ArrayList<>();

        for (ItemAttribute itemAttribute : ItemAttribute.getAttributeOrder()) {
            if (customPlayer.getAttributes().containsKey(itemAttribute)) {
                String addedLore = new StringBuilder().append(itemAttribute.getColor()).append(itemAttribute.getName()).append(": ").append(customPlayer.getValueOfAttribute(itemAttribute)).toString().trim();
                if (itemAttribute.getName().contains("Damage")) {
                    damageLore.add(addedLore);
                } else if (itemAttribute.getName().contains("Defense")) {
                    defenseLore.add(addedLore);
                } else if (itemAttribute.getName() != "All") {
                    miscLore.add(addedLore);
                }
            }
        }
        damageMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        damageMeta.setLore(damageLore);
        damageStatsSword.setItemMeta(damageMeta);

        defenseMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        defenseMeta.setLore(defenseLore);
        defenseStatsChestplate.setItemMeta(defenseMeta);

        miscMeta.setLore(miscLore);
        miscStatsCompass.setItemMeta(miscMeta);

        inv.setItem(12, damageStatsSword);
        inv.setItem(13, defenseStatsChestplate);
        inv.setItem(14, miscStatsCompass);

        inv.setItem(49, Main.CLOSE_BARRIER);
    }

    public Inventory getInv() {
        return inv;
    }

}
