package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItemStack {

    private final CustomItem item;
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
        return new CustomItemStack(CustomItem.fromName(item.getItemMeta().getDisplayName()), item.getAmount());
    }

    public static CustomItemStack fromItemStack(ItemStack item, int extraAmount) {
        return new CustomItemStack(CustomItem.fromName(item.getItemMeta().getDisplayName()), item.getAmount() + extraAmount);
    }

    public Material getMaterial() {
        return item.getMaterial();
    }

    public CustomItem getItem() {
        return item;
    }

    public int getTotalValue() {
        return (int) (item.getValue() * amount);
    }

    public String toString() {
        return "CustomItemStack{" + item.getMaterial() + ", " + item.getItemMeta() + ", " + this.amount + "}";
    }

    public boolean isSimilar(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() == other.getAmount() && getItemMeta() == other.getItemMeta() && getMaterial() == other.getMaterial();
    }

    public boolean isLess(CustomItemStack other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        return getAmount() <= other.getAmount() && getItemMeta() == other.getItemMeta() && getMaterial() == other.getMaterial();
    }

}