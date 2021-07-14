package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;

public class ApplierListener implements Listener {

    private Main plugin;

    public ApplierListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Main main = Main.getInstance();

        if (event.getView().getTitle().equalsIgnoreCase("Projectile Trails")) {
            if (!event.getClick().isLeftClick()) {
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
                if (event.getSlot() == event.getRawSlot()) {
                    ItemStack clickedItem = event.getClickedInventory().getItem(event.getSlot());
                    if (!clickedItem.getType().equals(Material.GRAY_STAINED_GLASS_PANE) && !clickedItem.getType().equals(Material.ARROW) && !clickedItem.getType().equals(Material.BARRIER)) {
                        CustomPlayer.fromPlayer(player).setTrail(ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
                        ProjectileTrailApplierInventory applierInventory = new ProjectileTrailApplierInventory(player);
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.openInventory(applierInventory.getInv());
                        });
                    } else {
                        if (event.getSlot() == 49) {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                player.closeInventory();
                            });
                        }
                    }
                }
            }
        }
    }

}
