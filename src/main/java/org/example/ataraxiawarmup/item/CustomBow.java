package org.example.ataraxiawarmup.item;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class CustomBow extends CustomWeapon {

    public CustomBow(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds) {
        super(material, name, rarity, recipeMatrix, elements, lowerBounds, upperBounds);
    }

    public void onArrowHitsMob(Player player, Entity damaged) {
        onDamageMob(player, damaged, 1.0D);
    }
}
