package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.example.ataraxiawarmup.Main;

public class CustomItemStack {

    private CustomItem item;
    private int amount;

    public CustomItemStack(final CustomItem item) {
        this(item, 1);
    }

    public CustomItemStack(final CustomItem item, final int amount) {
        this.item = item;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    public ItemStack toItemStack() {
        return this.item.toItemStack(this.amount);
    }

    public static CustomItemStack fromItemStack(ItemStack item) {
        return new CustomItemStack(CustomItem.itemFromName(item.getItemMeta().getDisplayName()), item.getAmount());
    }

    public static CustomItemStack fromItemStack(ItemStack item, int extraAmount) {
        return new CustomItemStack(CustomItem.itemFromName(item.getItemMeta().getDisplayName()), item.getAmount() + extraAmount);
    }

    public CustomItemType getType() {
        return item.getCustomItemType();
    }

    public String toString() {
        return "CustomItemStack{" + item.getCustomItemType() + ", " + item.getItemMeta() + ", " + this.amount + "}";
    }

    public boolean isSimilar(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() == other.getAmount() && getItemMeta() == other.getItemMeta() && getType() == other.getType();
    }

    public boolean isLess(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() <= other.getAmount() && getItemMeta() == other.getItemMeta() && getType() == other.getType();
    }

}

/**
public class CustomItemStack {

    private CustomItem type;
    private int amount = 0;
    private ItemMeta meta;
    private SkullMeta skullMeta;

    public CustomItemStack(CustomItem type, ItemMeta meta) {
        this(type, meta, 1);
    }

    public CustomItemStack(CustomItem type, int amount) {
        this(type, type.getItemMeta(), amount);
    }

    public CustomItemStack(CustomItem type, ItemMeta meta, int amount) {
        this.type = type;
        this.meta = meta;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemMeta getItemMeta() {
        return meta;
    }

    public void setItemMeta(ItemMeta meta) {
        this.meta = meta;
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(type.getMaterial(), amount);
        item.setItemMeta(meta);
        return item;
    }

    public static CustomItemStack fromItemStack(ItemStack item) {
        return new CustomItemStack(CustomItem.fromName(ChatColor.stripColor(item.getItemMeta().getDisplayName())), item.getItemMeta(), item.getAmount());
    }

    public static CustomItemStack fromItemStack(ItemStack item, int extraAmount) {
        return new CustomItemStack(CustomItem.fromName(ChatColor.stripColor(CustomItem.addUnderscores(item.getItemMeta().getDisplayName()))), item.getItemMeta(), item.getAmount() + extraAmount);
    }

    public CustomItem getType() {
        return type;
    }

    public String toString() {
        return "CustomItemStack{" + this.type + ", " + this.meta + ", " + this.amount + "}";
    }

    public boolean isSimilar(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() == other.getAmount() && getItemMeta() == other.getItemMeta() && getType() == other.getType();
    }

    public boolean isLess(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() <= other.getAmount() && getItemMeta() == other.getItemMeta() && getType() == other.getType();
    }

}
*/