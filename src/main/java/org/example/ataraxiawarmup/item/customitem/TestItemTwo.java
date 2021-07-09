package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestItemTwo extends CustomItem {


    public TestItemTwo(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix) {
        super(material, name, rarity, recipeMatrix);
        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {

    }
}
