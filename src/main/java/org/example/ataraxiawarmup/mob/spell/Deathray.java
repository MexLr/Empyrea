package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;

public class Deathray extends MobSpell {
    public Deathray(double damage, List<Element> elements) {
        super(damage, elements, Spell.DEATHRAY);
    }

    @Override
    void onUse(CustomMob user, Location location, boolean setCasting, Entity target) {
        if (setCasting) {
            user.setCasting(true);
        }
        new BukkitRunnable() {
            int ticks = 0;
            int ticksPhase1 = 40;
            @Override
            public void run() {
                if (ticks > 180) {
                    if (setCasting) {
                        user.setCasting(false);
                    }
                    cancel();
                }
                if (ticks <= ticksPhase1) {
                    if (ticks % 5 == 0) {
                        Location location = ((LivingEntity) user.getEntity()).getEyeLocation();
                        double yOffset = location.clone().subtract(user.getEntity().getLocation()).getY() / 2;
                        Location newLocation = location.clone().subtract(0, yOffset, 0);
                        newLocation.setYaw(location.getYaw() + ticks * 4);
                        Vector vector = newLocation.getDirection();
                        Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, (int) (((double) ticks / ticksPhase1) * 255), (int) (((double) ticks / ticksPhase1) * 255)), 2);
                        for (int p = 0; p < 10; p++) {
                            user.getEntity().getWorld().spawnParticle(Particle.REDSTONE, newLocation.clone().add(vector.clone().multiply(p)), 1, 0, 0, 0, dust);
                        }
                    }
                } else {
                    Location location = ((LivingEntity) user.getEntity()).getEyeLocation();
                    double yOffset = location.clone().subtract(user.getEntity().getLocation()).getY() / 2;
                    Location newLocation = location.clone().subtract(0, yOffset, 0);
                    newLocation.setYaw(location.getYaw() + ticks * 4);
                    Vector vector = newLocation.getDirection();
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 2);
                    for (int p = 0; p < 50; p++) {
                        Location particleLocation = newLocation.clone().add(vector.clone().multiply(p / 5));
                        user.getEntity().getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, dust);
                        List<Entity> nearbyEntities = (List<Entity>) particleLocation.getWorld().getNearbyEntities(particleLocation, 1, 1, 1);
                        for (Entity entity : nearbyEntities) {
                            if (entity instanceof Player) {
                                CustomPlayer customPlayer = CustomPlayer.fromPlayer((Player) entity);
                                customPlayer.damage(user.getDamage() / 10, user.getElements());
                            }
                        }
                    }
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
