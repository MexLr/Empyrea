package org.example.ataraxiawarmup.spawner.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.spawner.PlaceableSpawner;
import org.example.ataraxiawarmup.spawner.Spawner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnerMenuInventory {

    private static final Map<Inventory, Spawner> INVENTORY_SPAWNER_MAP = new HashMap<>();

    private final Inventory inv;

    public SpawnerMenuInventory(PlaceableSpawner spawner, Player player) {
        inv = Bukkit.createInventory(null, 27, spawner.getMobType().getName() + " Spawner " + ChatColor.AQUA + "(L" + spawner.getLevel() + ")");

        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);

        for (int i = 0; i < 27; i++) {
            inv.setItem(i, Main.FILLER_ITEM);
        }

        inv.setItem(22, Main.CLOSE_BARRIER);

        ItemStack upgrade = new ItemStack(Material.RED_CONCRETE);
        ItemMeta upgradeMeta = upgrade.getItemMeta();
        assert upgradeMeta != null;
        upgradeMeta.setDisplayName(ChatColor.AQUA + "Level up!");
        upgradeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        List<String> upgradeLore = new ArrayList<>();

        List<CustomItemStack> playerItems = customPlayer.getItemsInInventory();
        Map<CustomItem, Integer> playerItemMap = new HashMap<>();

        boolean canLevel = true;

        for (CustomItemStack item : playerItems) {
            playerItemMap.put(item.getItem(), item.getAmount());
        }

        for (CustomItemStack item : spawner.getItemsToLevelUp()) {
            int itemAmount;
            itemAmount = playerItemMap.getOrDefault(item.getItem(), 0);
            int reqAmount;
            if (item.getAmount() == 0) {
                reqAmount = 1;
            } else {
                reqAmount = item.getAmount();
            }
            upgradeLore.add(item.getItemMeta().getDisplayName() + ": " + itemAmount + "/" + reqAmount);
            if (itemAmount < item.getAmount()) {
                canLevel = false;
            }
        }

        if (canLevel) {
            upgrade.setType(Material.GREEN_CONCRETE);
            upgradeMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        } else {
            upgradeLore.add(0, ChatColor.RED + "to level up this spawner!");
            upgradeLore.add(0, ChatColor.RED + "You're missing some items needed");
        }
        upgradeMeta.setLore(upgradeLore);
        upgrade.setItemMeta(upgradeMeta);

        for (int s = 0; s < 7; s++) {
            ItemStack spawnerItem = spawner.toItem(s + 1);
            ItemMeta spawnerMeta = spawnerItem.getItemMeta();
            assert spawnerMeta != null;
            if (s + 1 == spawner.getLevel()) {
                spawnerMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            }
            spawnerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            spawnerItem.setItemMeta(spawnerMeta);
            inv.setItem(10 + s, spawnerItem);
        }

        ItemStack removeItem = new ItemStack(Material.RED_CONCRETE);
        ItemMeta removeMeta = removeItem.getItemMeta();
        assert removeMeta != null;
        removeMeta.setDisplayName(ChatColor.RED + "Remove");
        removeItem.setItemMeta(removeMeta);

        inv.setItem(18, removeItem);

        if (spawner.getLevel() == 7) {
            upgrade.setType(Material.CYAN_CONCRETE);
            upgradeMeta.setDisplayName("Max Level!");
            upgradeLore.clear();
            upgradeLore.add(ChatColor.GREEN + "You reached the max level of this spawner!");
            upgradeMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            upgradeMeta.setLore(upgradeLore);
            upgrade.setItemMeta(upgradeMeta);
        }
        inv.setItem(26, upgrade);
        INVENTORY_SPAWNER_MAP.put(inv, spawner);
    }

    public Inventory getInv() {
        return inv;
    }

    public static Spawner spawnerFromInventory(Inventory inventory) {
        return INVENTORY_SPAWNER_MAP.get(inventory);
    }

    public static void removeInventory(Inventory inventory) {
        INVENTORY_SPAWNER_MAP.remove(inventory);
    }

}
