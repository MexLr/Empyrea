package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CustomBow extends CustomWeapon {

    public CustomBow(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap, String extraLore) {
        this(material, name, rarity, recipeMatrix, false, elements, lowerBounds, upperBounds, attributeMap, extraLore);
    }

    public CustomBow(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, boolean shapeless, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap, String extraLore) {
        super(material, name, rarity, recipeMatrix, shapeless, elements, lowerBounds, upperBounds, attributeMap, extraLore);
    }

    public void onArrowHitsMob(Player player, Entity damaged, double multi) {
        onDamageMob(player, damaged, multi);
    }

    @Override
    public void onUseRight(Player player) {

    }

    @Override
    public void onUseLeft(Player player) {
        getAbility().performAbility(player, getAbilityLevel());
    }
}
