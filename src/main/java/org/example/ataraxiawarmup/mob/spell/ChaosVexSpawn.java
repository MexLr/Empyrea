package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
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
            boolean vexesSpawned = false;
            boolean rotating = false;
            CustomMob[] mobs = new CustomMob[4];
            int ticks = 1;
            @Override
            public void run() {
                int x = -1;
                int z;
                if (ticks < 40) {
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
                }
                if (ticks >= 40 && !vexesSpawned) {
                    for (int i = 0; i < 4; i++) {
                        x *= -1;
                        if (i < 2) {
                            z = 1;
                        } else {
                            z = -1;
                        }
                        CustomMob vex = CustomMob.fromName("40Vex?");
                        mobs[i] = vex;
                        Location vexLocation = location.clone().add(x * 10, 2, z * 10);
                        vexLocation.setDirection(target.getLocation().clone().toVector().subtract(vexLocation.toVector()));
                        vex.spawn(vexLocation);
                        ((LivingEntity) vex.getEntity()).setAI(false);
                        vex.getEntity().setInvulnerable(false);
                        vex.getEntity().setCustomName(vex.getCustomName() + " " + i);
                    }
                    vexesSpawned = true;
                }
                if (ticks > 40 && vexesSpawned) {
                    if (ticks % 40 < 15) {
                        for (int i = 0; i < mobs.length; i++) {
                            CustomMob vex = mobs[i];
                            Location vexLocation = vex.getEntity().getLocation();
                            Location newLocation = vexLocation.clone().subtract(new Vector(-vexLocation.getDirection().getZ(), 0, vexLocation.getDirection().getX()));
                            Location difference = vexLocation.clone().subtract(newLocation);
                            Location newVexLocation = newLocation.subtract(new Location(difference.getWorld(), difference.getX() / 20, difference.getY() / 20, difference.getZ() / 20));
                            newVexLocation.setY(target.getLocation().getY() + 2);
                            newVexLocation.setDirection(target.getLocation().clone().toVector().subtract(newVexLocation.toVector()));
                            vex.getEntity().teleport(newVexLocation);
                        }
                    }
                    if (ticks % 40 == 20) {
                        for (int i = 0; i < 2; i++) {
                            CustomMob vex1 = mobs[i * 2];
                            int index2 = i * 2 - 1;
                            if (index2 < 0) {
                                index2 += 4;
                            }
                            CustomMob vex2 = mobs[index2];
                            Location location1 = vex1.getEntity().getLocation();
                            Location location2 = vex2.getEntity().getLocation();
                            Location difference = location2.clone().subtract(location1.clone());
                            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(10, 0, 20), 1);
                            for (int p = 0; p < 50; p++) {
                                Location newLocation = location2.subtract(new Location(difference.getWorld(), difference.getX() / 50, difference.getY() / 50, difference.getZ() / 50));
                                newLocation.getWorld().spawnParticle(Particle.REDSTONE, newLocation, 1, 0, 0, 0, dust);
                            }
                        }
                    }
                }
                if (ticks >= 180) {
                    cancel();
                    user.setCasting(false);
                }
                ticks++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }
}
