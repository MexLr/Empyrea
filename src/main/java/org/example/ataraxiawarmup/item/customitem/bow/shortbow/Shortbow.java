package org.example.ataraxiawarmup.item.customitem.bow.shortbow;

import org.example.ataraxiawarmup.item.customitem.*;

import java.util.List;
import java.util.Map;

public class Shortbow extends CustomShortbow {
    public Shortbow(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap) {
        super(name, rarity, recipeMatrix, 1, elements, lowerBounds, upperBounds, attributeMap);
    }
}
