package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;

public class WeakChaosLightning extends MobSpell {
    public WeakChaosLightning(double damage, List<Element> elements) {
        super(damage, elements, Spell.WEAKCHAOSLIGHTNING);
    }

    @Override
    void onUse(CustomMob user, Location location, boolean setCasting, Entity target) {
        if (setCasting) {
            user.setCasting(true);
        }
        Location newLocation = location.clone();
        while (newLocation.getBlock().getType().isAir() && newLocation.getY() > 0) {
            newLocation.subtract(0, 1, 0);
        }
        newLocation.setY(Math.floor(newLocation.getY()));
        new BukkitRunnable() {
            int ticks = 0;
            int maxTicks = 30;
            @Override
            public void run() {
                if (ticks >= maxTicks) {
                    newLocation.getWorld().strikeLightningEffect(newLocation);
                    List<Entity> hitEntities = (List<Entity>) newLocation.getWorld().getNearbyEntities(newLocation.clone().add(0, 2, 0), 1, 1, 1);
                    for (Entity entity : hitEntities) {
                        if (entity instanceof Player) {
                            CustomPlayer customPlayer = CustomPlayer.fromPlayer((Player) entity);
                            customPlayer.damage(user.getDamage() * 3, user.getElements());
                        }
                    }
                    if (setCasting) {
                        user.setCasting(false);
                    }
                    cancel();
                }
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB((int) ((double) ticks / maxTicks * 127), 0, (int) ((double) ticks / maxTicks * 200)), 1);
                if (ticks > maxTicks * 0.7) {
                    dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
                }
                int angle = 0;
                for (int i = 0; i < 90; i++) {
                    angle += 4;
                    Location particleLocation = new Location(newLocation.getWorld(), newLocation.getX() + Math.sin(angle * Math.PI / 180), newLocation.getY() + 1, newLocation.getZ() + Math.cos(angle * Math.PI / 180));
                    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, 0, dust, true);

                }

                ticks += 2;
            }
        }.runTaskTimer(Main.getInstance(), 0, 2);
    }
}
