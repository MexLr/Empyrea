package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

import java.util.ArrayList;
import java.util.List;

public class RecipeInventory {

    private List<Inventory> inventories = new ArrayList<Inventory>();
    private Inventory viewRecipeInventory;

    public RecipeInventory(List<CustomRecipe> recipes, CustomItem forItem) {
        if (recipes.size() == 0) {
            return;
        }
        for (int i = 0; i < recipes.size(); i++) {
            Inventory inventory = Bukkit.createInventory(null, 54, "Recipes for " + forItem.getItemMeta().getDisplayName() + ChatColor.RESET + " " + (i + 1) + "/" + recipes.size());

            for (int f = 0; f < 54; f++) {
                inventory.setItem(f, Main.FILLER_ITEM);
            }
            inventory.setItem(49, Main.CLOSE_BARRIER);

            if (i < recipes.size() - 1) {
                ItemStack forwardArrow = new ItemStack(Material.ARROW);
                ItemMeta itemMeta = forwardArrow.getItemMeta();
                itemMeta.setDisplayName(ChatColor.WHITE + "Next");
                forwardArrow.setItemMeta(itemMeta);
                inventory.setItem(53, forwardArrow);
            }

            if (i > 0) {
                ItemStack backArrow = new ItemStack(Material.ARROW);
                ItemMeta itemMeta = backArrow.getItemMeta();
                itemMeta.setDisplayName(ChatColor.WHITE + "Previous");
                backArrow.setItemMeta(itemMeta);
                inventory.setItem(45, backArrow);
            }

            for (int s = 0; s < CraftingInventory.SLOTS.length; s++) {
                if (recipes.get(i).getMatrix()[s] == null) {
                    inventory.setItem(CraftingInventory.SLOTS[s], new ItemStack(Material.AIR));
                    continue;
                }
                inventory.setItem(CraftingInventory.SLOTS[s], recipes.get(i).getMatrix()[s].toItemStack());
                inventory.setItem(24, recipes.get(i).getResult().toItemStack());
            }
            inventories.add(inventory);
        }
    }

    public RecipeInventory(CustomItem recipeFor) {
        if (CustomRecipe.fromName(recipeFor.getItemMeta().getDisplayName()) != null) {
            Inventory inventory = Bukkit.createInventory(null, 54, "Recipe of " + recipeFor.getItemMeta().getDisplayName());

            for (int f = 0; f < 54; f++) {
                inventory.setItem(f, Main.FILLER_ITEM);
            }
            inventory.setItem(49, Main.CLOSE_BARRIER);

            for (int s = 0; s < CraftingInventory.SLOTS.length; s++) {
                if (recipeFor.getRecipe().getMatrix()[s] == null) {
                    inventory.setItem(CraftingInventory.SLOTS[s], new ItemStack(Material.AIR));
                    continue;
                }
                inventory.setItem(CraftingInventory.SLOTS[s], recipeFor.getRecipe().getMatrix()[s].toItemStack());
                inventory.setItem(24, recipeFor.getRecipe().getResult().toItemStack());
            }
            this.viewRecipeInventory = inventory;
        }
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public Inventory getViewRecipeInventory() {
        return this.viewRecipeInventory;
    }

}
