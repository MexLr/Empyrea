package org.example.ataraxiawarmup.item.customitem.bow.shortbow;

import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.CustomItemStack;
import org.example.ataraxiawarmup.item.CustomShortbow;
import org.example.ataraxiawarmup.item.Element;
import org.example.ataraxiawarmup.item.Rarity;

import java.util.List;

public class Apollo extends CustomShortbow {
    public Apollo(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds) {
        super(name, rarity, recipeMatrix, 5, elements, lowerBounds, upperBounds);
    }

    @Override
    public void onUseRight(Player player) {

    }
}
