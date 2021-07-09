package org.example.ataraxiawarmup.item.customitem.bow.shortbow;

import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.customitem.*;

import java.util.List;
import java.util.Map;

public class Apollo extends CustomShortbow {
    public Apollo(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap) {
        super(name, rarity, recipeMatrix, 5, elements, lowerBounds, upperBounds, attributeMap);
    }

    @Override
    public void onUseRight(Player player) {

    }
}
