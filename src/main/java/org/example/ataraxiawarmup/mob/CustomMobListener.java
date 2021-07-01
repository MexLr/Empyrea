package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
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
            if (CustomMob.fromEntity(event.getDamager()) != null) {
                CustomMob.fromEntity(event.getDamager()).onAttackPlayer((Player) event.getEntity());
            }
        }
    }

    @EventHandler
    public void onMobDies(EntityDeathEvent event) {
        event.getDrops().clear();
    }
}
