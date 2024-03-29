package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class CustomSword extends CustomWeapon {
    public CustomSword(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap, String extraLore, int combatLevelReq) {
        this(material, name, rarity, recipeMatrix, false, elements, lowerBounds, upperBounds, attributeMap, extraLore, combatLevelReq);
    }
    public CustomSword(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, boolean shapeless, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap, String extraLore, int combatLevelReq) {
        super(material, name, rarity, recipeMatrix, shapeless, elements, lowerBounds, upperBounds, attributeMap, extraLore, combatLevelReq);
    }

    @Override
    public void onUseLeft(Player player) {

    }
}
