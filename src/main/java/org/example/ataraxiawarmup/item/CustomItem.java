package org.example.ataraxiawarmup.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class CustomItem {

    public static final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();

    private final ItemMeta meta = new ItemStack(Material.BARRIER).getItemMeta();
    private final CustomItemType type;
    private final String name;
    private final Rarity rarity;
    private CustomRecipe recipe;
    private CustomItemStack[] recipeMatrix;

    public CustomItem(CustomItemType type, String name, Rarity rarity, CustomItemStack[] recipeMatrix) {
        this.type = type;
        this.name = name;
        this.rarity = rarity;
        this.recipeMatrix = recipeMatrix;

        this.meta.setDisplayName(this.rarity.getColor() + this.name);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(this.rarity.getLore());
        this.meta.setLore(lore);

        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    /**
     * Executes when the player uses left click while holding the item
     *
     * @param player - The player that is interacting
     */
    public abstract void onUseLeft(Player player);

    /**
     * Executes when the player uses right click while holding the item
     *
     * @param player - The player that is interacting
     */
    public abstract void onUseRight(Player player);

    /**
     * Creates and initializes the recipe for this item, if it has one
     */
    public void createRecipe() {
        if (this.recipeMatrix == null) {
            return;
        }
        this.recipe = new CustomRecipe(this.recipeMatrix, this.toCustomItemStack());
    }

    /**
     * Returns the CustomItem in ItemStack form
     *
     * @return - ItemStack of CustomItem
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(this.type.getMaterial());
        ItemMeta itemMeta = this.meta;
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Returns the CustomItem in ItemStack form, with the specified amount
     *
     * @param amount - Amount of items in the stack
     * @return - ItemStack of CustomItem with a specified amount
     */
    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(this.type.getMaterial(), amount);
        ItemMeta itemMeta = this.meta;
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * Returns the CustomItem in CustomItemStack form, defaulted to a stack size of 1
     *
     * @return - A CustomItemStack with stack size 1 of this CustomItem
     */
    public CustomItemStack toCustomItemStack() {
        return new CustomItemStack(this, 1);
    };

    /**
     * Returns the CustomItem in CustomItemStack form, with the specified stack size
     *
     * @return - A CustomItemStack with the specified stack size of this CustomItem
     */
    public CustomItemStack toCustomItemStack(int amount) {
        return new CustomItemStack(this, amount);
    };

    /**
     * Returns the CustomRecipe of this item
     *
     * @return - The CustomRecipe of this item
     */
    public CustomRecipe getRecipe() {
        return this.recipe;
    }

    /**
     * Returns the CustomItemType of this item
     *
     * @return - The CustomItemType of this item
     */
    public CustomItemType getType() {
        return this.type;
    }

    /**
     * Returns this item's ItemMeta
     *
     * @return - The ItemMeta of this item
     */
    public ItemMeta getItemMeta() {
        return this.meta;
    };

    /**
     * Gets a CustomItem by name
     *
     * @param name - The name of the CustomItem that is being searched for
     * @return - The CustomItem, or null if there is no CustomItem with the name
     */
    public static CustomItem itemFromName(String name) {
        return CUSTOM_ITEMS.get(ChatColor.stripColor(name).toLowerCase());
    }
}