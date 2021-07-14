package org.example.ataraxiawarmup.item.customitem.trinity;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Eidolon extends CustomShortbow {
    public Eidolon(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap) {
        super(name, rarity, recipeMatrix, 3, elements, lowerBounds, upperBounds, attributeMap, "");
    }

    @Override
    public void onUseLeft(Player player) {
        if (addToCooldown(player, "Shortbow", 250)) {
            new BukkitRunnable() {
                Player player1 = player;
                Location location = player1.getLocation();
                int shots = 0;

                @Override
                public void run() {
                    if (shots == 5) {
                        this.cancel();
                    }
                    for (int i = 0; i < 3; i++) {
                        AbstractArrow arrow = player1.launchProjectile(Arrow.class, location.getDirection().clone().subtract(new Vector(-location.getDirection().getZ(), 0, location.getDirection().getX()).multiply(0.1D * Math.floor(i - 1))));
                    }
                    shots++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 1);
        }
    }
}
