package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Cooldowns;

public abstract class CustomCooldownableItem extends CustomItem {

    public CustomCooldownableItem(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix) {
        super(material, name, rarity, recipeMatrix);
    }

    public boolean addToCooldown(Player player, String name, long length) {
        return Cooldowns.tryCooldown(player, name, length);
    }

    @Override
    public CustomCooldownableItem clone() {
        CustomCooldownableItem item = (CustomCooldownableItem) super.clone();

        return item;
    }
}
