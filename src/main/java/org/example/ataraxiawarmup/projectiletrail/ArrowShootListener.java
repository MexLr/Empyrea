package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.example.ataraxiawarmup.Main;

public class ArrowShootListener implements Listener {

    private Main plugin;
    public ArrowShootListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerShootsArrow(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            TrailType type = TrailType.fromName(ChatColor.stripColor(Main.getInstance().playerStats.get(player.getUniqueId()).get(0)));

            ProjectileTrail trail = type.isBasic() ? new BasicTrail(type) : new SpiralTrail(type);
            
            trail.setArrow((AbstractArrow) event.getEntity());

            int task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, trail.getRunnable(), 0, 0);
            trail.setTaskID(task);
        }
    }

}
