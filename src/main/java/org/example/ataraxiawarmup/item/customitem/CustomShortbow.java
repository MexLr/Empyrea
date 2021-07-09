package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class CustomShortbow extends CustomWeapon {

    private final int arrowAmount;

    public CustomShortbow(String name, Rarity rarity, CustomItemStack[] recipeMatrix, int arrowAmount, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap) {
        super(Material.BOW, name, rarity, recipeMatrix, elements, lowerBounds, upperBounds, attributeMap, "");
        this.arrowAmount = arrowAmount;
    }

    @Override
    public void onUseLeft(Player player) {
        if (addToCooldown(player, "Shortbow", 250)) {
            Location playerLocation = player.getLocation();
            for (int i = 0; i < arrowAmount; i++) {
                AbstractArrow arrow = player.launchProjectile(Arrow.class, playerLocation.getDirection().clone().subtract(new Vector(-playerLocation.getDirection().getZ(), 0, playerLocation.getDirection().getX()).multiply(0.1D * (i - Math.floor(arrowAmount / 2)))));
            }
        }
    }
}
