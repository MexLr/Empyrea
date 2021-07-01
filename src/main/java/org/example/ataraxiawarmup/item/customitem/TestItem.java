package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.CustomItem;
import org.example.ataraxiawarmup.item.CustomItemStack;
import org.example.ataraxiawarmup.item.Rarity;

public class TestItem extends CustomItem {

    public TestItem(Material material, String name, Rarity rarity, CustomItemStack[] recipe) {
        super(material, name, rarity, recipe);
        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    @Override
    public void onUseLeft(Player player) {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
    }

    @Override
    public void onUseRight(Player player) {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
    }
}
