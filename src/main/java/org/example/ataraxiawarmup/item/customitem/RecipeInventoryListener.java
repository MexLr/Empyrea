package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.ataraxiawarmup.Main;

public class RecipeInventoryListener implements Listener {
    private Main plugin;

    public RecipeInventoryListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClickInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Recipe")) {
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(true);
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        event.getWhoClicked().closeInventory();
                    });
                }
                if (event.getCurrentItem() == null) {
                   return;
                }

                if (!event.getCurrentItem().getType().equals(Material.BARRIER) && !event.getCurrentItem().getType().equals(Material.ARROW) && !event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                    if (event.getClick().equals(ClickType.LEFT)) {
                        RecipeInventory recipeInventory = new RecipeInventory(CustomItem.fromName(event.getCurrentItem().getItemMeta().getDisplayName()));
                        if (recipeInventory.getViewRecipeInventory() == null) {
                            return;
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            event.getWhoClicked().openInventory(recipeInventory.getViewRecipeInventory());
                        });
                    }
                    if (event.getClick().equals(ClickType.RIGHT)) {
                        RecipeInventory recipeInventory = new RecipeInventory(CustomRecipe.usesItem(CustomItem.fromName(event.getCurrentItem().getItemMeta().getDisplayName())), CustomItem.fromName(event.getCurrentItem().getItemMeta().getDisplayName()));
                        if (recipeInventory.getInventories().size() == 0) {
                            return;
                        }
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            event.getWhoClicked().openInventory(recipeInventory.getInventories().get(0));
                        });
                    }
                }
            }
        }
        if (event.getView().getTitle().startsWith("Recipes")) {
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(true);
                String itemName = event.getView().getTitle()
                        .split(" for ")[1]
                        .split("/")[0]
                        .trim();

                itemName = itemName.replace(itemName.charAt(itemName.length() - 1), ' ').trim();

                CustomItem forItem = CustomItem.fromName(itemName);
                int page = Integer.parseInt(event.getView().getTitle()
                        .split("/")[0]
                        .split(" ")[event.getView().getTitle().split("/")[0].split(" ").length - 1]
                        .trim());
                switch (event.getSlot()) {
                    case 45:
                        if (event.getCurrentItem().getType().equals(Material.ARROW)) {
                            RecipeInventory newInventory = new RecipeInventory(CustomRecipe.usesItem(forItem), forItem);
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                event.getWhoClicked().openInventory(newInventory.getInventories().get(page - 2));
                            });
                        }
                        break;
                    case 49:
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            event.getWhoClicked().closeInventory();
                        });
                        break;
                    case 53:
                        if (event.getCurrentItem().getType().equals(Material.ARROW)) {
                            RecipeInventory newInventory = new RecipeInventory(CustomRecipe.usesItem(forItem), forItem);
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                event.getWhoClicked().openInventory(newInventory.getInventories().get(page));
                            });
                        }
                        break;
                }
            }
        }
    }

}
