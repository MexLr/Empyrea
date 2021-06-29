package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.example.ataraxiawarmup.Main;

public class CustomItemListener implements Listener {

    private Main plugin;

    public CustomItemListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {

        if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        if (CustomItem.itemFromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) == null) {
            return;
        }
        CustomItem heldItem = CustomItem.itemFromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            heldItem.onUseLeft(event.getPlayer());
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            heldItem.onUseRight(event.getPlayer());
        }

    }

}
