package org.example.ataraxiawarmup.item.customitem.bow.shortbow;

import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.customitem.*;

import java.util.List;
import java.util.Map;

public class ChaoticShortbow extends CustomShortbow {
    public ChaoticShortbow(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap) {
        super(name, rarity, recipeMatrix, 3, elements, lowerBounds, upperBounds, attributeMap);
    }

    @Override
    public void onUseRight(Player player) {

    }
}
