package org.example.ataraxiawarmup.item.customitem.ingredient;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.CustomIngredient;
import org.example.ataraxiawarmup.item.CustomItemStack;
import org.example.ataraxiawarmup.item.Rarity;

public class TestIngredient extends CustomIngredient {

    public TestIngredient(Material material, String name, Rarity rarity, CustomItemStack[] recipe) {
        super(material, name, rarity, recipe, true);
    }

    @Override
    public void onUseLeft(Player player) {

    }
}
