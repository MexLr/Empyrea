package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;
import java.util.Random;

public class ChaosFireRain extends MobSpell {
    public ChaosFireRain(double damage, List<Element> elements) {
        super(damage, elements, Spell.CHAOSFIRERAIN);
    }

    @Override
    void onUse(CustomMob user, Location location, boolean setCasting, Entity target) {
        if (setCasting) {
            user.setCasting(true);
        }
        new BukkitRunnable() {

            int warnings = 0;
            @Override
            public void run() {
                if (warnings >= 3) {
                    cancel();
                    new BukkitRunnable() {
                        double amount = 0;

                        @Override
                        public void run() {
                            if (amount >= 4) {
                                if (setCasting) {
                                    user.setCasting(false);
                                }
                                cancel();
                            }
                            Location location = user.getEntity().getLocation();
                            for (int i = 0; i < 12; i++) {
                                double angle = 30 * i;
                                Vector directionVector = new Vector(Math.sin(angle * Math.PI / 180) * 0.3 * (1 + amount / 2), 0.3 * (1 + amount / 2), Math.cos(angle * Math.PI / 180) * 0.3 * (1 + amount / 2));
                                ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                                armorStand.getEquipment().setHelmet(new ItemStack(Material.RED_WOOL));
                                armorStand.setVelocity(directionVector);
                                armorStand.setInvisible(true);

                                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                                    armorStand.remove();
                                    armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.2f, 1.0f);
                                    armorStand.getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 0.2f, 1.0f);
                                }, 20);

                                // particles
                                new BukkitRunnable() {
                                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);

                                    @Override
                                    public void run() {
                                        if (!armorStand.isValid()) {
                                            armorStand.getWorld().spawnParticle(Particle.CRIMSON_SPORE, armorStand.getLocation().clone().add(0, 1.7, 0), 50, 0.5, 0.5, 0.5, 1D);
                                            armorStand.getWorld().spawnParticle(Particle.WARPED_SPORE, armorStand.getLocation().clone().add(0, 1.7, 0), 50, 0.5, 0.5, 0.5, 1D);
                                            armorStand.getWorld().spawnParticle(Particle.DRAGON_BREATH, armorStand.getLocation().clone().add(0, 1.7, 0), 50, 0.5, 0.5, 0.5, 0.25D);
                                            List<Entity> playersToExplode = (List<Entity>) armorStand.getWorld().getNearbyEntities(armorStand.getLocation(), 3, 3, 3);

                                            for (Entity entity : playersToExplode) {
                                                if (entity instanceof Player) {
                                                    CustomPlayer customPlayer = CustomPlayer.fromPlayer((Player) entity);
                                                    customPlayer.damage(user.getDamage(), user.getElements());
                                                }
                                            }
                                            cancel();
                                        }
                                        armorStand.getWorld().spawnParticle(Particle.REDSTONE, armorStand.getLocation().clone().add(0, 1.7, 0), 1, 0, 0, 0, 0, dust, true);
                                    }
                                }.runTaskTimer(Main.getInstance(), 0, 1);
                            }
                            location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.2f, 1.0f);
                            amount++;
                        }
                    }.runTaskTimer(Main.getInstance(), 0, 10);
                }


                warnings++;
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }
}
