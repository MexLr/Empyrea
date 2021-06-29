package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;

public class CraftingInventory {

    public static final int[] SLOTS = new int[]{11, 12, 13, 20, 21, 22, 29, 30, 31};

    private Inventory inv;

    public CraftingInventory() {

        inv = Bukkit.createInventory(null, 54, "Crafting Table");

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.getInstance().FILLER_ITEM);
        }

        inv.setItem(49, Main.getInstance().CLOSE_BARRIER);
        inv.setItem(11, new ItemStack(Material.AIR));
        inv.setItem(12, new ItemStack(Material.AIR));
        inv.setItem(13, new ItemStack(Material.AIR));

        inv.setItem(20, new ItemStack(Material.AIR));
        inv.setItem(21, new ItemStack(Material.AIR));
        inv.setItem(22, new ItemStack(Material.AIR));

        inv.setItem(29, new ItemStack(Material.AIR));
        inv.setItem(30, new ItemStack(Material.AIR));
        inv.setItem(31, new ItemStack(Material.AIR));

        inv.setItem(24, new ItemStack(Material.AIR));

    }

    public CraftingInventory(ItemStack[] matrix, ItemStack result) {

        inv = Bukkit.createInventory(null, 54, "Crafting Table");

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.getInstance().FILLER_ITEM);
        }

        inv.setItem(49, Main.getInstance().CLOSE_BARRIER);
        inv.setItem(11, matrix[0]);
        inv.setItem(12, matrix[1]);
        inv.setItem(13, matrix[2]);

        inv.setItem(20, matrix[3]);
        inv.setItem(21, matrix[4]);
        inv.setItem(22, matrix[5]);

        inv.setItem(29, matrix[6]);
        inv.setItem(30, matrix[7]);
        inv.setItem(31, matrix[8]);

        inv.setItem(24, result);

    }

    public Inventory getInv() {
        return inv;
    }

    public ItemStack[] getMatrix(Inventory inventory) {
        ItemStack[] matrix = new ItemStack[9];
        for (int i = 0; i < SLOTS.length; i++) {
            matrix[i] = inventory.getItem(SLOTS[i]);
        }
        return matrix;
    }

    public CustomItemStack[] getCustomMatrix(Inventory inventory) {
        CustomItemStack[] matrix = new CustomItemStack[9];
        for (int i = 0; i < SLOTS.length; i++) {
            if (inventory.getItem(SLOTS[i]) == null) {
                matrix[i] = null;
                continue;
            }
            matrix[i] = new CustomItemStack(CustomItem.itemFromName(inventory.getItem(SLOTS[i]).getItemMeta().getDisplayName()), inventory.getItem(SLOTS[i]).getAmount());
        }
        return matrix;
    }

}
