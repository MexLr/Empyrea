package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

                // get most of the item name. Here's an example:
                // "Recipes for Enchanted Stick 1/14"
                // get the string after " for " and before "/":
                // Enchanted Stick 1
                String itemName = ChatColor.stripColor(event.getView().getTitle()
                        .split(" for ")[1]
                        .split("/")[0]
                        .trim());

                // take the item name and replace the number with air. Example:
                // Enchanted Stick 1
                // split by (" ") and take the last string in the returned array, or the string at index arrayLength - 1
                // replace the last string with nothing, and trim the string
                itemName = itemName.replace(itemName.split(" ")[itemName.split(" ").length - 1], "").trim();

                CustomItem forItem = CustomItem.fromName(itemName);
                // get the page. Here's an example:
                // Enchanted Stick 1/14
                // get the string before "/":
                // Enchanted Stick 1
                // do the same as removing the page number above, except actually use the page number instead of replacing it:
                // split by (" ") and take the last string in the returned array, or the string at index arrayLength - 1
                // trim the string
                int page = Integer.parseInt(event.getView().getTitle()
                        .split("/")[0]
                        .split(" ")[event.getView().getTitle().split("/")[0].split(" ").length - 1]
                        .trim());

                switch (event.getSlot()) {
                    case 45:
                        if (event.getCurrentItem().getType().equals(Material.ARROW)) {
                            RecipeInventory newInventory = new RecipeInventory(CustomRecipe.usesItem(forItem), forItem);
                            if (event.getClick().equals(ClickType.RIGHT)) {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    event.getWhoClicked().openInventory(newInventory.getInventories().get(0));
                                });
                            } else {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    event.getWhoClicked().openInventory(newInventory.getInventories().get(page - 2));
                                });
                            }
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
                            if (event.getClick().equals(ClickType.RIGHT)) {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    event.getWhoClicked().openInventory(newInventory.getInventories().get(newInventory.getInventories().size() - 1)); // get the last page, using the same .length - 1 or .size() - 1 that i've been using to get the last index in an array or list
                                });
                            } else {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    event.getWhoClicked().openInventory(newInventory.getInventories().get(page));
                                });
                            }
                        }
                        break;
                }
            }
        }
    }

}
