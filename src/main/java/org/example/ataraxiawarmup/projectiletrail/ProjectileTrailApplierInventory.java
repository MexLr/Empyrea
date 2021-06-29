package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

import java.util.*;

public class ProjectileTrailApplierInventory {

    private Inventory inv;

    private static final Map<TrailType, ChatColor> TYPE_CHAT_COLOR_MAP = new HashMap<TrailType, ChatColor>();
    private static final List<ItemStack> TRAIL_ITEM_LIST = new ArrayList<ItemStack>();

    static {
        for (TrailType type : TrailType.values()) {
            ItemStack item = new ItemStack(type.getMat());
            List<String> itemLore = new ArrayList<String>();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(type.getColor() + type.getName());
            itemLore.add(ChatColor.DARK_GRAY + "Applies the " + type.getColor() + type.getName() + ChatColor.DARK_GRAY + " projectile");
            itemLore.add(ChatColor.DARK_GRAY + "trail to your weapon!");
            itemMeta.setLore(itemLore);
            item.setItemMeta(itemMeta);
            TRAIL_ITEM_LIST.add(item);
        }
    }

    public ProjectileTrailApplierInventory(Player player) {

        inv = Bukkit.createInventory(null, 54, "Projectile Trails");
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.getInstance().FILLER_ITEM);
        }
        for (ItemStack item : TRAIL_ITEM_LIST) {
            if (Main.getInstance().playerStats.get(player.getUniqueId()).get(0).equals(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
                inv.setItem(TRAIL_ITEM_LIST.indexOf(item), getSetSelected(item));
            } else {
                inv.setItem(TRAIL_ITEM_LIST.indexOf(item), item);
            }
        }
        inv.setItem(49, Main.getInstance().CLOSE_BARRIER);
    }

    public Inventory getInv() {
        return inv;
    }

    private ItemStack getSetSelected(ItemStack itemStack) {
        ItemStack newItemStack = new ItemStack(itemStack.getType());
        ItemMeta itemMeta = newItemStack.getItemMeta();
        List<String> newLore = new ArrayList<String>();
        newLore.add(ChatColor.GREEN + "Selected!");
        itemMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
        itemMeta.setLore(newLore);
        newItemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        newItemStack.setItemMeta(itemMeta);
        return newItemStack;
    }
}
