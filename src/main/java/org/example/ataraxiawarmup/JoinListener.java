package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class JoinListener implements Listener {

    private Main plugin;

    public JoinListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoins(PlayerJoinEvent event) {
        List<String> stats = new ArrayList<String>();
        stats.add("Basic");
        plugin.playerStats.put(event.getPlayer().getUniqueId(), stats);
    }

}
