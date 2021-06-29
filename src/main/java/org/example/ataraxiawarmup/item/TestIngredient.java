package org.example.ataraxiawarmup.item;

import org.bukkit.entity.Player;

public class TestIngredient extends CustomIngredient {

    public TestIngredient(CustomItemType type, String name, Rarity rarity, CustomItemStack[] recipe) {
        super(type, name, rarity, recipe);
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {

    }
}
