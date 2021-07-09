package org.example.ataraxiawarmup.item.customitem.ability;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomWeapon;

public class AbilityApplyingInventoryListener implements Listener {
    private Main plugin;

    public AbilityApplyingInventoryListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle() == "Ability Anvil") {
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(true);
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        event.getWhoClicked().closeInventory();
                    });
                }

                if (event.getCursor() != null) {
                    CustomWeapon weapon = (CustomWeapon) CustomItem.fromName(event.getCursor().getItemMeta().getDisplayName());
                    if (weapon != null) {
                        ItemStack inputThere = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                        ItemMeta inputMeta = inputThere.getItemMeta();
                        inputMeta.setLore(null);
                        inputMeta.addEnchant(Enchantment.DURABILITY, 0, true);
                        inputMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        inputMeta.setDisplayName("");
                        inputThere.setItemMeta(inputMeta);

                        event.getInventory().setItem(11, inputThere);
                        event.getInventory().setItem(12, inputThere);
                        event.getInventory().setItem(20, inputThere);

                        ItemStack item = event.getCursor();
                        event.getView().setCursor(null);
                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            event.getView().setItem(event.getSlot(), item);
                        });
                    } else {
                        ItemStack item = event.getCursor();
                        event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            event.getView().setCursor(item);
                        });
                    }
                } else {

                }
            }
        }
    }
}
