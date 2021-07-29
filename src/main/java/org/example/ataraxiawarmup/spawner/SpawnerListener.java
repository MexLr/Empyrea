package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;

import java.util.Arrays;
import java.util.List;

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

        PlaceableSpawner spawner = new PlaceableSpawner(spawnerItem.getSpawnType(), spawnerItem.getLevel(), event.getBlock().getLocation().clone().subtract(-0.5, 2, -0.5), plugin);
        spawner.startSpawning();
    }

    @EventHandler
    public void onSpawnerUnloads(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        if (Spawner.fromChunk(chunk) != null) {
            for (Spawner spawner : InvisibleSpawner.fromChunk(chunk)) {
                spawner.unload();
            }
        }
    }

    @EventHandler
    public void onSpawnerLoads(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        if (Spawner.fromChunk(chunk) != null) {
            for (Spawner spawner : InvisibleSpawner.fromChunk(chunk)) {
                spawner.load();
            }
        }
    }

    @EventHandler
    public void onPlayerBreaksBlock(PlayerHarvestBlockEvent event) {
        if (event.getHarvestedBlock().getType().equals(Material.SPAWNER)) {
            event.getPlayer().sendMessage("§cYou can't break this block!");
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClicksSpawner(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand stand = (ArmorStand) event.getRightClicked();
            if (stand.getCustomName().equals("Spawner")) {
                PlaceableSpawner spawner = PlaceableSpawner.fromArmorStand(stand);
                event.getPlayer().openInventory(spawner.getGUI(event.getPlayer()).getInv());
            }
        }
    }

}
