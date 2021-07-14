package org.example.ataraxiawarmup.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;
import org.example.ataraxiawarmup.Main;

public class CustomPlayerListener implements Listener {
    private Main plugin;

    public CustomPlayerListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoins(PlayerJoinEvent event) {
        CustomPlayer player = new CustomPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLosesHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRegensNaturally(EntityRegainHealthEvent event) {
        if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakesFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerGainsEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getNewEffect() == null) {
                return;
            }
            if (event.getNewEffect().getType() == null) {
                return;
            }
            if (event.getNewEffect().getType().equals(PotionEffectType.WITHER)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerGainsExp(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerDamagesPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof AbstractArrow) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    ((Projectile) event.getDamager()).setBounce(true);
                    event.setCancelled(true);
                }
            }
        }
    }
}
