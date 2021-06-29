package org.example.ataraxiawarmup.item;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class TestItem extends CustomItem {

    public TestItem(CustomItemType type, String name, Rarity rarity, CustomItemStack[] recipe) {
        super(type, name, rarity, recipe);
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
