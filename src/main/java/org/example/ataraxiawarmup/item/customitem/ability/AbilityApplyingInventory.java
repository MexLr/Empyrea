package org.example.ataraxiawarmup.item.customitem.ability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

import java.util.List;

public class AbilityApplyingInventory {

    private Inventory inv;

    public AbilityApplyingInventory() {
        inv = Bukkit.createInventory(null, 54, "Ability Anvil");

        for (int f = 0; f < 54; f++) {
            inv.setItem(f, Main.FILLER_ITEM);
        }
        inv.setItem(49, Main.CLOSE_BARRIER);

        ItemStack anvil = new ItemStack(Material.ANVIL);
        ItemMeta itemMeta = anvil.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Click to combine!");
        anvil.setItemMeta(itemMeta);

        ItemStack outputSlot = new ItemStack(Material.BARRIER);
        ItemMeta outputMeta = outputSlot.getItemMeta();
        outputMeta.setDisplayName(ChatColor.RED + "Output");
        outputSlot.setItemMeta(outputMeta);

        ItemStack weaponTo = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta weaponToMeta = weaponTo.getItemMeta();
        weaponToMeta.setDisplayName(ChatColor.GOLD + "Place a weapon in the slot below!");
        weaponTo.setItemMeta(weaponToMeta);

        ItemStack artifactTo = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta artifactToMeta = artifactTo.getItemMeta();
        artifactToMeta.setDisplayName(ChatColor.GOLD + "Place an artifact in the slot below!");
        artifactTo.setItemMeta(artifactToMeta);

        inv.setItem(13, outputSlot);
        inv.setItem(11, weaponTo);
        inv.setItem(12, weaponTo);
        inv.setItem(20, weaponTo);
        inv.setItem(14, artifactTo);
        inv.setItem(15, artifactTo);
        inv.setItem(24, artifactTo);
        inv.setItem(22, anvil);

        inv.setItem(29, new ItemStack(Material.AIR));
        inv.setItem(33, new ItemStack(Material.AIR));

    }

    public Inventory getInventory() {
        return inv;
    }
}
