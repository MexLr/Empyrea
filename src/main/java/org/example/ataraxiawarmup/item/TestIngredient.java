package org.example.ataraxiawarmup.item;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

public class TestIngredient extends CustomIngredient {

    @Override
    protected ItemDefinition getItemDefinition() {
        return new ItemDefinition("Test Ingredient", getCustomItemType().getMaterial(), Rarity.UNCOMMON);
    }

    @Override
    public CustomRecipe getRecipe() {
        return null;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.BLAZE_POWDER;
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {

    }

    @Override
    public void initialize() {
        createRecipe(null);
    }
}
