package org.example.ataraxiawarmup.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.ataraxiawarmup.Main;

public class MenuInventoryListener implements Listener {

    private Main plugin;

    public MenuInventoryListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicksInMenuInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Eidolon Menu")) {
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(true);
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        event.getWhoClicked().closeInventory();
                    });
                }
            }
        }
    }

}
