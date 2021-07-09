package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;

public class RecipeListener implements Listener {

    private Main plugin;
    public RecipeListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClickInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Crafting Table")) {
            CraftingInventory craftingInventory = new CraftingInventory();
            CustomItemStack[] previousItems = craftingInventory.getCustomMatrix(event.getInventory()); // this prevents the dupe glitch caused by fixing shift clicking an item into the output slot; before it would check to see if the recipe was valid a tick later than it should have
            if (event.getSlot() == event.getRawSlot()) {
                if (event.getInventory().getItem(event.getSlot()) != null) {
                    if (event.getInventory().getItem(event.getSlot()).getType().equals(Material.GRAY_STAINED_GLASS_PANE) || event.getInventory().getItem(event.getSlot()).getType().equals(Material.BARRIER)) {
                        event.setCancelled(true);
                        if (event.getSlot() == 49) {
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                event.getWhoClicked().closeInventory();
                            });
                        }
                    }
                }
                if (event.getSlot() == 24) {
                    event.setCancelled(true);
                }
                if (event.getSlot() == 24 && event.getInventory().getItem(24) != null && event.getClick().equals(ClickType.LEFT)) {
                    if (event.getCursor().getType().equals(event.getInventory().getItem(24).getType()) && event.getCursor().getItemMeta().equals(event.getInventory().getItem(24).getItemMeta()) || event.getCursor().getType().equals(Material.AIR)) {
                        ItemStack[] items1 = craftingInventory.getMatrix(event.getInventory());

                        final ItemStack savedItem;
                        if (event.getInventory().getItem(24) != null) {
                            savedItem = event.getInventory().getItem(24);
                        } else {
                            savedItem = null;
                        }
                        CustomItemStack[] recipe = CustomRecipe.fromName(event.getInventory().getItem(24).getItemMeta().getDisplayName()).getMatrix();

                        for (int i = 0; i < items1.length; i++) {
                            if (items1[i] != null) {
                                ItemStack addedItem = items1[i];
                                addedItem.setAmount(addedItem.getAmount() - recipe[i].getAmount());
                                event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                            }
                        }
                        ItemStack cursor = event.getView().getCursor();

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            if (!cursor.getType().equals(Material.AIR) && savedItem.getType().equals(cursor.getType()) && savedItem.getItemMeta().equals(cursor.getItemMeta())) {
                                event.getView().setCursor(CustomItemStack.fromItemStack(cursor, 1).toItemStack());
                            } else {
                                event.getView().setCursor(savedItem);
                            }
                        });
                    }
                } else if (event.getSlot() == 24 && event.getInventory().getItem(24) != null && event.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if (event.getCursor().getType().equals(event.getInventory().getItem(24).getType()) && event.getCursor().getItemMeta().equals(event.getInventory().getItem(24).getItemMeta()) || event.getCursor().getType().equals(Material.AIR)) {
                        CustomItemStack[] items = craftingInventory.getCustomMatrix(event.getInventory());
                        CustomItemStack[] recipe = CustomRecipe.fromName(event.getInventory().getItem(24).getItemMeta().getDisplayName()).getMatrix();

                        ItemStack[] items1 = craftingInventory.getMatrix(event.getInventory());

                        while (CustomRecipe.getResult(items) != null) {
                            for (int i = 0; i < items1.length; i++) {
                                if (items1[i] != null) {
                                    ItemStack addedItem = items1[i];
                                    addedItem.setAmount(addedItem.getAmount() - recipe[i].getAmount());
                                    event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                                }
                            }
                            event.getWhoClicked().getInventory().addItem(CustomRecipe.getResult(items).toItemStack());
                            items = craftingInventory.getCustomMatrix(event.getInventory());
                        }
                        event.getInventory().setItem(24, new ItemStack(Material.AIR));
                    }
                    return;
                }
            } else {
                if (event.getClick().equals(ClickType.SHIFT_LEFT)) {
                    if (event.getInventory().getItem(24) != null && event.getCurrentItem() != null) {
                        if (event.getInventory().getItem(24).isSimilar(event.getCurrentItem())) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
            Bukkit.getScheduler().runTask(plugin, () -> {
                CustomItemStack[] items = craftingInventory.getCustomMatrix(event.getInventory());

                if (event.getInventory().getItem(24) != null && CustomRecipe.getResult(previousItems) == null) {
                    event.getWhoClicked().getInventory().addItem(event.getInventory().getItem(24));
                    event.getInventory().setItem(24, new ItemStack(Material.AIR));
                }

                if (CustomRecipe.getResult(items) != null) {
                    event.getInventory().setItem(24, CustomRecipe.getResult(items).toItemStack());
                } else {
                    event.getInventory().setItem(24, new ItemStack(Material.AIR));
                }
            });
        }
    }

    @EventHandler
    public void onDragClick(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Crafting Table")) {
            CraftingInventory craftingInventory = new CraftingInventory();
            if (event.getInventorySlots().contains(24)) {
                event.getInventory().setItem(24, new ItemStack(Material.AIR));
                if (event.getCursor() == null) {
                    event.setCursor(event.getNewItems().get(24));
                } else {
                    event.setCursor(CustomItemStack.fromItemStack(event.getCursor(), event.getNewItems().get(24).getAmount()).toItemStack());
                }
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                CustomItemStack[] items = craftingInventory.getCustomMatrix(event.getInventory());

                if (CustomRecipe.getResult(items) != null) {
                    event.getInventory().setItem(24, CustomRecipe.getResult(items).toItemStack());
                } else {
                    event.getInventory().setItem(24, new ItemStack(Material.AIR));
                }
            });
        }
    }
}
