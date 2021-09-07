package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class CustomItem implements Cloneable {

    public static final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();

    private final Material material;
    private final String name;
    private final Rarity rarity;
    private ItemMeta meta;
    private CustomRecipe recipe;
    private CustomItemStack[] recipeMatrix;
    private boolean isShapeless;
    private CustomItem replaces;
    private double value;

    public CustomItem(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, boolean shapeless, CustomItem replaces) {
        this.material = material;
        this.name = name;
        this.rarity = rarity;
        this.recipeMatrix = recipeMatrix;
        this.isShapeless = shapeless;
        this.replaces = replaces;

        this.meta = new ItemStack(Material.BARRIER).getItemMeta();

        if (name == "Nadir") {
            this.meta.setDisplayName(ChatColor.BLACK + this.name);
        } else if (name == "Zenith") {
            this.meta.setDisplayName(ChatColor.WHITE + this.name);
        } else {
            this.meta.setDisplayName(this.rarity.getColor() + this.name);
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(this.rarity.getLore());
        lore.add("placeholder");
        this.meta.setUnbreakable(true);
        this.meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        this.meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        this.meta.setLore(lore);
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
     * Creates and initializes the recipe for this item, if it has one - also sets the value.
     */
    public void initialize() {
        if (this.recipeMatrix != null) {
            this.recipe = new CustomRecipe(this.recipeMatrix, this.toCustomItemStack(), this.isShapeless);
        }

        int index = 2;
        if (this instanceof CustomWeapon) {
            index += ((CustomWeapon) this).getElements().size();
            if (((CustomWeapon) this).hasAbility()) {
                return;
            }
        }
        this.value = getTotalValue();
        List<String> lore = this.meta.getLore();

        lore.set(index, ChatColor.LIGHT_PURPLE + "Value: " + this.value);
        this.meta.setLore(lore);
    }

    /**
     * Returns the CustomItem in ItemStack form
     *
     * @return - ItemStack of CustomItem
     */
    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(this.material);
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
        ItemStack item = new ItemStack(this.material, amount);
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
     * Sets the recipe of this item.
     *
     * @param recipe - The new recipe for this item to have
     */
    public void setRecipe(CustomRecipe recipe) {
        if (recipe == null) {
            this.recipeMatrix = null;
            removeRecipe();
        } else {
            this.recipeMatrix = recipe.getMatrix();
        }
        this.recipe = recipe;
    }

    /**
     * Removes this item's recipe from the CustomRecipe maps.
     */
    public void removeRecipe() {
        CustomRecipe.NAME_MAP.remove(this.getItemMeta().getDisplayName());
        CustomRecipe.RECIPE_MAP.remove(this.getRecipe());
    }

    /**
     * Returns the Material of this item
     *
     * @return - The Material of this item
     */
    public Material getMaterial() {
        return this.material;
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
     * Sets the item meta of the item.
     *
     * @param meta - New meta to set the item's ItemMeta to
     */
    public void setItemMeta(ItemMeta meta) {
        this.meta = meta;
    }

    /**
     * Returns the rarity of the item.
     *
     * @return - The rarity of the item
     */
    public Rarity getRarity() {
        return this.rarity;
    }

    /**
     * Gets the item that replaces this one in a recipe. Example: empowered gemstone -> gemstone
     *
     * @return - The item that replaces this in a recipe.
     */
    public CustomItem getReplaces() {
        return replaces;
    }

    /**
     * Gets the value of the item, determined by its rarity and recipe.
     *
     * @return - The value of the item.
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the item, if it needs to change for any reason.
     *
     * @param value - The new value of the item.
     */
    public void setValue(double value) {
        this.value = value;
        int index = 2;
        if (this instanceof CustomWeapon) {
            index += ((CustomWeapon) this).getElements().size();
        }
        List<String> lore = this.meta.getLore();
        lore.set(index, ChatColor.LIGHT_PURPLE + "Value: " + this.value);
        this.meta.setLore(lore);
    }

    // recursive call
    public double getTotalValue() {
        double totalValue = 0;
        if (recipeMatrix == null || recipeMatrix.length < 8) {
            return Math.pow(this.rarity.getId(), 7);
        }
        for (CustomItemStack customItemStack : recipeMatrix) {
            if (customItemStack == null) {
                continue;
            }
            totalValue += customItemStack.getItem().getTotalValue() * customItemStack.getAmount();
        }
        return totalValue;
    }

    /**
     * Gets a CustomItem by name
     *
     * @param name - The name of the CustomItem that is being searched for
     * @return - The CustomItem, or null if there is no CustomItem with the name
     */
    public static CustomItem fromName(String name) {
        return CUSTOM_ITEMS.get(ChatColor.stripColor(name).toLowerCase());
    }

    @Override
    public CustomItem clone() {
        try {
            CustomItem customItem = (CustomItem) super.clone();

            ItemMeta itemMeta = this.meta.clone();
            customItem.setItemMeta(itemMeta);

            if (this.recipeMatrix == null) {
                customItem.recipeMatrix = null;
            } else {
                customItem.recipeMatrix = this.recipeMatrix.clone();
            }
            customItem.recipe = this.recipe;

            return customItem;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}