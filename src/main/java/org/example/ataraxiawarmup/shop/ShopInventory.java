package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

import java.util.ArrayList;
import java.util.List;

public class ShopInventory {

    private static final List<String> inventoryNames = List.of("Fire Items", "Water Items", "Earth Items", "Thunder Items", "Air Items", "Chaos Items", "Miscellaneous Ingredients", "Weapons", "Orders");

    private static Inventory baseInv;
    private static final Inventory fireInv;
    private static final Inventory waterInv;
    private static final Inventory earthInv;
    private static final Inventory thunderInv;
    private static final Inventory airInv;
    private static final Inventory chaosInv;
    private static final Inventory ingredientInv;
    private static final Inventory weaponInv;

    static {
        baseInv = Bukkit.createInventory(null, 54, "");
        for (int i = 0; i < 54; i++) {
            baseInv.setItem(i, Main.FILLER_ITEM);
        }
        baseInv.setItem(49, Main.CLOSE_BARRIER);
        baseInv.setItem(8, new ItemStack(Material.RED_STAINED_GLASS_PANE));

        ItemStack fireTab = new ItemStack(Material.BLAZE_ROD);
        ItemMeta fireMeta = fireTab.getItemMeta();
        fireMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        fireMeta.setDisplayName(ChatColor.RED + "Fire Items");

        fireInv = Bukkit.createInventory(null, 54, "Fire Items");
        fireInv.setContents(baseInv.getContents());
        List<ItemStack> tabItems = getTabs("Fire Items");
        for (int i = 0; i < tabItems.size(); i++) {
            fireInv.setItem(i, tabItems.get(i));
        }
        fireInv.setItem(21, getSpawnEgg("Fire Items"));

        waterInv = Bukkit.createInventory(null, 54, "Water Items");
        waterInv.setContents(baseInv.getContents());
        tabItems = getTabs("Water Items");
        for (int i = 0; i < tabItems.size(); i++) {
            waterInv.setItem(i, tabItems.get(i));
        }
        waterInv.setItem(21, getSpawnEgg("Water Items"));

        earthInv = Bukkit.createInventory(null, 54, "Earth Items");
        earthInv.setContents(baseInv.getContents());
        tabItems = getTabs("Earth Items");
        for (int i = 0; i < tabItems.size(); i++) {
            earthInv.setItem(i, tabItems.get(i));
        }
        earthInv.setItem(21, getSpawnEgg("Earth Items"));

        thunderInv = Bukkit.createInventory(null, 54, "Thunder Items");
        thunderInv.setContents(baseInv.getContents());
        tabItems = getTabs("Thunder Items");
        for (int i = 0; i < tabItems.size(); i++) {
            thunderInv.setItem(i, tabItems.get(i));
        }
        thunderInv.setItem(21, getSpawnEgg("Thunder Items"));

        airInv = Bukkit.createInventory(null, 54, "Air Items");
        airInv.setContents(baseInv.getContents());
        tabItems = getTabs("Air Items");
        for (int i = 0; i < tabItems.size(); i++) {
            airInv.setItem(i, tabItems.get(i));
        }
        airInv.setItem(21, getSpawnEgg("Air Items"));

        chaosInv = Bukkit.createInventory(null, 54, "Chaos Items");
        chaosInv.setContents(baseInv.getContents());
        tabItems = getTabs("Chaos Items");
        for (int i = 0; i < tabItems.size(); i++) {
            chaosInv.setItem(i, tabItems.get(i));
        }
        chaosInv.setItem(21, getSpawnEgg("Chaos Items"));

        ingredientInv = Bukkit.createInventory(null, 54, "Miscellaneous Ingredients");
        ingredientInv.setContents(baseInv.getContents());
        tabItems = getTabs("Miscellaneous Ingredients");
        for (int i = 0; i < tabItems.size(); i++) {
            ingredientInv.setItem(i, tabItems.get(i));
        }

        weaponInv = Bukkit.createInventory(null, 54, "Weapons");
        weaponInv.setContents(baseInv.getContents());
        tabItems = getTabs("Weapons");
        for (int i = 0; i < tabItems.size(); i++) {
            weaponInv.setItem(i, tabItems.get(i));
        }
    }

    public Inventory getFireMobInv() {
        return null;
    }

    public static Inventory getInventory(String name) {
        switch (name.toUpperCase()) {
            case "FIRE":
            case "FIRE ITEMS":
                return fireInv;
            case "WATER":
            case "WATER ITEMS":
                return waterInv;
            case "EARTH":
            case "EARTH ITEMS":
                return earthInv;
            case "THUNDER":
            case "THUNDER ITEMS":
                return thunderInv;
            case "AIR":
            case "AIR ITEMS":
                return airInv;
            case "CHAOS":
            case "CHAOS ITEMS":
                return chaosInv;
            case "INGREDIENTS":
            case "MISCELLANEOUS ITEMS":
                return ingredientInv;
            case "WEAPONS":
                return weaponInv;
            case "ORDERS":
                return getOrderInv();
        }
        return null;
    }

    private int getIndexOfTab(String tabName) {
        switch (tabName) {
            case "Fire Items":
                return 0;
            case "Water Items":
                return 1;
            case "Earth Items":
                return 2;
            case "Thunder Items":
                return 3;
            case "Air Items":
                return 4;
            case "Chaos Items":
                return 5;
            case "Miscellaneous Ingredients":
                return 6;
            case "Weapons":
                return 7;
            default:
                break;
        }
        return -1;
    }

    private static ItemStack getSpawnEgg(String inventoryFor) {
        ItemStack spawnEgg = new ItemStack(Material.SPIDER_SPAWN_EGG);
        ItemMeta spawnMeta = spawnEgg.getItemMeta();
        switch (inventoryFor) {
            case "Fire Items":
                spawnEgg.setType(Material.MAGMA_CUBE_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.RED + "Fire Mob Drops");
                break;
            case "Water Items":
                spawnEgg.setType(Material.GUARDIAN_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.AQUA + "Water Mob Drops");
                break;
            case "Earth Items":
                spawnEgg.setType(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.DARK_GREEN + "Earth Mob Drops");
                break;
            case "Thunder Items":
                spawnEgg.setType(Material.BLAZE_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.YELLOW + "Thunder Mob Drops");
                break;
            case "Air Items":
                spawnEgg.setType(Material.GHAST_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.GRAY + "Air Mob Drops");
                break;
            case "Chaos Items":
                spawnEgg.setType(Material.SHULKER_SPAWN_EGG);
                spawnMeta.setDisplayName(ChatColor.DARK_PURPLE + "Chaos Mob Drops");
                break;
        }
        return spawnEgg;
    }

    private static List<ItemStack> getTabs(String inventoryFor) {
        List<ItemStack> items = new ArrayList<>();
        for (String string : inventoryNames) {
            ItemStack itemFor = getItemFor(string);
            if (string.equalsIgnoreCase(inventoryFor)) {
                ItemMeta itemMeta = itemFor.getItemMeta();
                itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
                itemFor.setItemMeta(itemMeta);
            }
            items.add(itemFor);
        }
        return items;
    }

    private static ItemStack getItemFor(String tabFor) {
        ItemStack tab = new ItemStack(Material.BARRIER);
        ItemMeta tabMeta = tab.getItemMeta();
        tabMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        switch (tabFor) {
            case "Fire Items":
                tab.setType(Material.BLAZE_ROD);
                tabMeta.setDisplayName(ChatColor.RED + tabFor);
                break;
            case "Water Items":
                tab.setType(Material.HEART_OF_THE_SEA);
                tabMeta.setDisplayName(ChatColor.AQUA + tabFor);
                break;
            case "Earth Items":
                tab.setType(Material.OAK_SAPLING);
                tabMeta.setDisplayName(ChatColor.DARK_GREEN + tabFor);
                break;
            case "Thunder Items":
                tab.setType(Material.SUNFLOWER);
                tabMeta.setDisplayName(ChatColor.YELLOW + tabFor);
                break;
            case "Air Items":
                tab.setType(Material.PHANTOM_MEMBRANE);
                tabMeta.setDisplayName(ChatColor.GRAY + tabFor);
                break;
            case "Chaos Items":
                tab.setType(Material.ENDER_EYE);
                tabMeta.setDisplayName(ChatColor.DARK_PURPLE + tabFor);
                break;
            case "Miscellaneous Ingredients":
                tab.setType(Material.ROTTEN_FLESH);
                tabMeta.setDisplayName(ChatColor.GREEN + tabFor);
                break;
            case "Weapons":
                tab.setType(Material.DIAMOND_SWORD);
                tabMeta.setDisplayName(ChatColor.DARK_AQUA + tabFor);
                break;
            case "Orders":
                tab.setType(Material.CHAIN);
                tabMeta.setDisplayName(ChatColor.DARK_GRAY + tabFor);
                break;
            default:
                break;
        }
        tab.setItemMeta(tabMeta);
        return tab;
    }

    public static Inventory getOrderInv() {
        Inventory orderInv = Bukkit.createInventory(null, 54, "Orders");
        orderInv.setContents(baseInv.getContents());
        List<ItemStack> tabItems = getTabs("Orders");
        for (int i = 0; i < tabItems.size(); i++) {
            orderInv.setItem(i, tabItems.get(i));
        }
        ItemStack rewardClaim = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = rewardClaim.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "Rewards");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Click here to view your rewards\nfrom past orders!");
        itemMeta.setLore(lore);
        rewardClaim.setItemMeta(itemMeta);

        orderInv.setItem(48, rewardClaim);

        List<Order> activeOrders = new ArrayList<>();
        activeOrders.addAll(Order.getActiveOrders());

        for (int i = 0; i < activeOrders.size(); i++) {
            int index = 18 + i * 2;
            if (i > 3) {
                index++;
            }
            if (i > 7) {
                index = 39 + (i - 7) * 2;
            }
            orderInv.setItem(index, activeOrders.get(i).getItem());
        }

        return orderInv;
    }

}
