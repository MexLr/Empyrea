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
                        CustomItemStack[] items = craftingInventory.getCustomMatrix(event.getInventory()).clone();
                        ItemStack[] items1 = craftingInventory.getMatrix(event.getInventory());

                        final ItemStack savedItem;
                        if (event.getInventory().getItem(24) != null) {
                            savedItem = event.getInventory().getItem(24);
                        } else {
                            savedItem = null;
                        }
                        CustomRecipe recipe = CustomRecipe.fromName(event.getInventory().getItem(24).getItemMeta().getDisplayName());
                        CustomItemStack[] recipeMatrix = recipe.getMatrix().clone();

                        if (recipe.isShapeless()) {
                            // iterate through the array of CustomItemStacks in the (cloned) crafting matrix
                            for (int i = 0; i < items.length; i++) {
                                if (items[i] == null) { // if the item is null, continue
                                    continue;
                                }
                                for (int j = 0; j < recipeMatrix.length; j++) { // iterate through the array of CustomItemStacks in the (cloned) recipe matrix
                                    if (recipeMatrix[j] == null) { // if the recipe's item is null, continue
                                        continue;
                                    }
                                    if (recipeMatrix[j].isLess(items[i])) { // if an item in the recipe and the item in the given matrix matches
                                        items[i] = new CustomItemStack(Main.CUSTOM_AIR); // set the item in the given matrix to essentially a pseudo-null to avoid extra successes or skips
                                        // set the item in the inventory to the same item, minus the amount from the recipe matrix's item
                                        ItemStack addedItem = items1[i];
                                        addedItem.setAmount(addedItem.getAmount() - recipeMatrix[j].getAmount());
                                        event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                                        break; // break out of this for loop, continuing the outer one (the given matrix loop)
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < items1.length; i++) {
                                if (items1[i] != null) {
                                    ItemStack addedItem = items1[i];
                                    addedItem.setAmount(addedItem.getAmount() - recipeMatrix[i].getAmount());
                                    event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                                }
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
                        CustomItemStack[] givingItems = craftingInventory.getCustomMatrix(event.getInventory()).clone(); // the items used as an input for getting the recipe
                        CustomRecipe recipe = CustomRecipe.fromName(event.getInventory().getItem(24).getItemMeta().getDisplayName());

                        CustomItemStack[] recipeMatrix = recipe.getMatrix();

                        ItemStack[] items1 = craftingInventory.getMatrix(event.getInventory());

                        while (CustomRecipe.getResult(givingItems) != null) {
                            if (recipe.isShapeless()) {
                                // iterate through the array of CustomItemStacks in the (cloned) crafting matrix
                                for (int i = 0; i < items.length; i++) {
                                    if (items[i] == null) { // if the item is null, continue
                                        continue;
                                    }
                                    for (int j = 0; j < recipeMatrix.length; j++) { // iterate through the array of CustomItemStacks in the (cloned) recipe matrix
                                        if (recipeMatrix[j] == null) { // if the recipe's item is null, continue
                                            continue;
                                        }
                                        if (recipeMatrix[j].isLess(items[i])) { // if an item in the recipe and the item in the given matrix matches
                                            items[i] = new CustomItemStack(Main.CUSTOM_AIR); // set the item in the given matrix to essentially a pseudo-null to avoid extra successes or skips
                                            // set the item in the inventory to the same item, minus the amount from the recipe matrix's item
                                            ItemStack addedItem = items1[i];
                                            addedItem.setAmount(addedItem.getAmount() - recipeMatrix[j].getAmount());
                                            event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                                            break; // break out of this for loop, continuing the outer one (the given matrix loop)
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < items1.length; i++) {
                                    if (items1[i] != null) {
                                        ItemStack addedItem = items1[i];
                                        addedItem.setAmount(addedItem.getAmount() - recipeMatrix[i].getAmount());
                                        event.getInventory().setItem(craftingInventory.SLOTS[i], addedItem);
                                    }
                                }
                            }
                            event.getWhoClicked().getInventory().addItem(CustomRecipe.getResult(givingItems).toItemStack());
                            // reset all of the arrays to what's actually in the matrix
                            givingItems = craftingInventory.getCustomMatrix(event.getInventory());
                            items = craftingInventory.getCustomMatrix(event.getInventory());
                            items1 = craftingInventory.getMatrix(event.getInventory());
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
