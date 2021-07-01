package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;

public class SpawnerListener implements Listener {

    private Main plugin;

    public SpawnerListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerPlacesSpawner(BlockPlaceEvent event) {
        if (!event.getBlockPlaced().getType().equals(Material.SPAWNER))
            return;
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        SpawnerItem spawnerItem = new SpawnerItem(item);

        Spawner spawner = new Spawner(spawnerItem.getSpawnType(), spawnerItem.getInterval(), event.getBlock().getLocation(), plugin);
        spawner.startSpawning();
    }
}
