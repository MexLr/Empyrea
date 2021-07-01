package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;

import java.util.List;

public abstract class CustomShortbow extends CustomBow {

    private final int arrowAmount;

    public CustomShortbow(String name, Rarity rarity, CustomItemStack[] recipeMatrix, int arrowAmount, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds) {
        super(Material.BOW, name, rarity, recipeMatrix, elements, lowerBounds, upperBounds);
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
