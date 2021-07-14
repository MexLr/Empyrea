package org.example.ataraxiawarmup.item.customitem.boss;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;

public class PedestalListener implements Listener {
    private Main plugin;
    public PedestalListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicksPedestal(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            event.setCancelled(true);
            SummonPedestal pedestal = SummonPedestal.fromStand(armorStand);
            if (pedestal == null) {
                return;
            }
            if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                event.getPlayer().getInventory().setItemInMainHand(pedestal.getItem());
                pedestal.setItem(new ItemStack(Material.AIR));
            } else {
                pedestal.setItem(event.getPlayer().getInventory().getItemInMainHand());
                event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            }
        }
    }
}
