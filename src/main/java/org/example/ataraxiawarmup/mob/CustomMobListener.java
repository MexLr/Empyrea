package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.example.ataraxiawarmup.Main;

public class CustomMobListener implements Listener {

    private Main plugin;

    public CustomMobListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMobDamagesPlayer(EntityDamageByEntityEvent event) {
        event.setDamage(0);
        if (event.getDamager() instanceof Player) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Projectile) {
                if (CustomMob.fromEntity((Entity) (((Projectile) event.getDamager()).getShooter())) != null) {
                    CustomMob.fromEntity((Entity) (((Projectile) event.getDamager()).getShooter())).onDamagePlayer((Player) event.getEntity());
                }
            }
            if (CustomMob.fromEntity(event.getDamager()) != null) {
                CustomMob.fromEntity(event.getDamager()).onDamagePlayer((Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onMobDies(EntityDeathEvent event) {
        event.getDrops().clear();
    }

    @EventHandler
    public void onUndeadIsOnFire(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onZombify(EntityTransformEvent event) {
        if (event.getTransformReason().equals(EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED)) {
            event.setCancelled(true);
        }
    }
}
