package org.example.ataraxiawarmup.item;

public abstract class CustomIngredient extends CustomItem {


    public CustomIngredient(CustomItemType type, String name, Rarity rarity, CustomItemStack[] recipe) {
        super(type, name, rarity, recipe);
    }
}
