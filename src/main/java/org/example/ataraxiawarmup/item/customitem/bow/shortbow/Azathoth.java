package org.example.ataraxiawarmup.item.customitem.bow.shortbow;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.CustomItemStack;
import org.example.ataraxiawarmup.item.CustomShortbow;
import org.example.ataraxiawarmup.item.Element;
import org.example.ataraxiawarmup.item.Rarity;

import java.util.List;
import java.util.Random;

public class Azathoth extends CustomShortbow {
    public Azathoth(String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds) {
        super(name, rarity, recipeMatrix, 3, elements, lowerBounds, upperBounds);
    }

    public void onFireballHitsMob(Player player, Entity damaged) {
        onDamageMob(player, damaged, 80.0D);
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

    @Override
    public void onUseRight(Player player) {
        if (addToCooldown(player, "Fireball", 2000)) {
            Entity target = null;

            List<Entity> nearbyEntities = player.getNearbyEntities(15.0, 15.0, 15.0);
            for (int i = 0; i < nearbyEntities.size(); i++) {
                if (nearbyEntities.get(i) instanceof Projectile || nearbyEntities.get(i) instanceof Player) {
                    continue;
                }
                target = nearbyEntities.get(i);
            }

            if (target != null) {
                Random random = new Random();
                int randomValue = random.nextInt(6) - 3;
                Location targetLocation = target.getLocation().add(new Location(player.getWorld(), randomValue, 0, randomValue));
                Fireball fireball = (Fireball) player.getWorld().spawnEntity(targetLocation.add(new Location(player.getWorld(), 0.0, 10.0, 0.0)), EntityType.FIREBALL);
                Vector vector = target.getLocation().toVector().subtract(fireball.getLocation().toVector());
                fireball.setDirection(vector);
                fireball.setShooter(player);
            }
        }
    }
}
