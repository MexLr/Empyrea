package org.example.ataraxiawarmup.spawner.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.spawner.PlaceableSpawner;
import org.example.ataraxiawarmup.spawner.Spawner;

import java.util.HashMap;

public class SpawnerMenuListener implements Listener {
    private Main plugin;
    public SpawnerMenuListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Spawner")) {
            if (event.getRawSlot() == event.getSlot()) {
                event.setCancelled(true);
                PlaceableSpawner spawner = (PlaceableSpawner) SpawnerMenuInventory.spawnerFromInventory(event.getClickedInventory());
                switch (event.getSlot()) {
                    case 22:
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            event.getWhoClicked().closeInventory();
                        });
                        break;
                    case 18:
                        spawner.remove(true);
                        event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            event.getWhoClicked().closeInventory();
                        });
                        break;
                    case 26:
                        if (event.getCurrentItem().getType().equals(Material.GREEN_CONCRETE)) {
                            event.getWhoClicked().getInventory().removeItem(spawner.getLevelUpItemstacks());
                            event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                            event.getWhoClicked().sendMessage(ChatColor.GREEN + event.getView().getTitle() + ChatColor.GREEN + " leveled up to level " + (spawner.getLevel() + 1) + "!");
                            ((Player) event.getWhoClicked()).updateInventory();
                            spawner.levelUp();
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                event.getWhoClicked().closeInventory();
                            });
                        }
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClosesInventory(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Spawner")) {

        }
    }
}
