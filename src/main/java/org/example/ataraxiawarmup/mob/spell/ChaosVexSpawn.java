package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.List;

public class ChaosVexSpawn extends MobSpell {
    public ChaosVexSpawn(double damage, List<Element> elements) {
        super(damage, elements, Spell.CHAOSVEXSPAWN);
    }

    @Override
    void onUse(CustomMob user, Location location, boolean setCasting, Entity target) {
        if (setCasting) {
            user.setCasting(true);
        }
        new BukkitRunnable() {
            int ticks = 1;
            @Override
            public void run() {
                int x = -1;
                int z;
                for (int i = 0; i < 4; i++) {
                    x *= -1;
                    if (i < 2) {
                        z = 1;
                    } else {
                        z = -1;
                    }
                    Location particleLocation = location.clone().add(x * 10, 3, z * 10);
                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
                    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, ticks, 0.3, 0.3, 0.3, 0, dust, true);
                }
                if (ticks >= 40) {
                    CustomMob vex = CustomMob.fromName("40Vex?");
                    if (vex != null) {
                        for (int i = 0; i < 4; i++) {
                            x *= -1;
                            if (i < 2) {
                                z = 1;
                            } else {
                                z = -1;
                            }
                            Location vexLocation = location.clone().add(x * 10, 3, z * 10);
                            vex.spawn(vexLocation);
                            ((LivingEntity) vex.getEntity()).setAI(false);
                            vex.getEntity().setInvulnerable(false);
                        }
                    }
                    cancel();
                    user.setCasting(false);
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
