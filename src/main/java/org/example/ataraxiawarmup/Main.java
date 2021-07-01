package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.ataraxiawarmup.item.*;
import org.example.ataraxiawarmup.item.customitem.bow.shortbow.*;
import org.example.ataraxiawarmup.mob.*;
import org.example.ataraxiawarmup.projectiletrail.ApplierListener;
import org.example.ataraxiawarmup.projectiletrail.ArrowShootListener;
import org.example.ataraxiawarmup.spawner.SpawnerListener;

import java.util.*;

public class Main extends JavaPlugin {

    public static final ItemStack FILLER_ITEM; // filler item for inventories
    public static final ItemStack BACK_ARROW; // back arrow for inventories
    public static final ItemStack CLOSE_BARRIER; // close barrier for inventories

    private static Main instance;

    static {
        FILLER_ITEM = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = FILLER_ITEM.getItemMeta();
        fillerMeta.setDisplayName(" ");
        fillerMeta.setLore(null);
        FILLER_ITEM.setItemMeta(fillerMeta);

        BACK_ARROW = new ItemStack(Material.ARROW);
        ItemMeta backMeta = BACK_ARROW.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "Back");
        BACK_ARROW.setItemMeta(backMeta);

        CLOSE_BARRIER = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = CLOSE_BARRIER.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeMeta.setLore(null);
        CLOSE_BARRIER.setItemMeta(closeMeta);
    }

    public Map<UUID, List<String>> playerStats = new HashMap<UUID, List<String>>();

    // so MANIFEST.MF recognizes this as a main class
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        new SpawnerListener(this);
        new CommandListener(this);
        new ArrowShootListener(this);
        new ApplierListener(this);
        new JoinListener(this);
        new RecipeListener(this);
        new CustomItemListener(this);
        new RecipeInventoryListener(this);
        new CustomMobListener(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> stats = new ArrayList<String>();
            stats.add("Basic");
            playerStats.put(player.getUniqueId(), stats);
        }

        // items
        // ingredients
        CustomIngredient stick = new CustomIngredient(Material.STICK, "Stick", Rarity.COMMON, null, false){};
        CustomIngredient string = new CustomIngredient(Material.STRING, "String", Rarity.COMMON, null, false){};
        CustomIngredient iron = new CustomIngredient(Material.IRON_INGOT, "Iron Ingot", Rarity.COMMON, null, false){};
        CustomIngredient enchantedStick = new CustomIngredient(Material.STICK, "Enchanted Stick", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(stick, 64), null,
                new CustomItemStack(stick, 64), new CustomItemStack(stick, 64), new CustomItemStack(stick, 64),
                null, new CustomItemStack(stick, 64), null
        }, true){};
        CustomIngredient enchantedString = new CustomIngredient(Material.STRING, "Enchanted String", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(string, 32), null,
                new CustomItemStack(string, 32), new CustomItemStack(string, 32), new CustomItemStack(string, 32),
                null, new CustomItemStack(string, 32), null
        }, true){};
        CustomIngredient enchantedIron = new CustomIngredient(Material.IRON_INGOT, "Enchanted Iron Ingot", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(iron, 32), null,
                new CustomItemStack(iron, 32), new CustomItemStack(iron, 32), new CustomItemStack(iron, 32),
                null, new CustomItemStack(iron, 32), null
        }, true){};
        CustomIngredient compactStick = new CustomIngredient(Material.STICK, "Compact Stick", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 64), null,
                new CustomItemStack(enchantedStick, 64), new CustomItemStack(enchantedStick, 64), new CustomItemStack(enchantedStick, 64),
                null, new CustomItemStack(enchantedStick, 64), null
        }, true){};
        CustomIngredient enchantedWeb = new CustomIngredient(Material.COBWEB, "Enchanted Web", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedString, 32), null,
                new CustomItemStack(enchantedString, 32), new CustomItemStack(enchantedString, 32), new CustomItemStack(enchantedString, 32),
                null, new CustomItemStack(enchantedString, 32), null
        }, true){};
        CustomIngredient enchantedIronBlock = new CustomIngredient(Material.IRON_BLOCK, "Enchanted Iron Block", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedIron, 32), null,
                new CustomItemStack(enchantedIron, 32), new CustomItemStack(enchantedIron, 32), new CustomItemStack(enchantedIron, 32),
                null, new CustomItemStack(enchantedIron, 32), null
        }, true){};
        CustomIngredient fireShard = new CustomIngredient(Material.BLAZE_POWDER, "Fire Shard", Rarity.RARE, null, true){};
        CustomIngredient earthShard = new CustomIngredient(Material.WHEAT_SEEDS, "Earth Shard", Rarity.RARE, null, true){};
        CustomIngredient waterShard = new CustomIngredient(Material.HEART_OF_THE_SEA, "Water Shard", Rarity.RARE, null, true){};
        CustomIngredient elementalCore = new CustomIngredient(Material.CYAN_DYE, "Elemental Core", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(earthShard, 1), new CustomItemStack(waterShard, 1), null,
                new CustomItemStack(fireShard, 1), null, null,
                null, null, null
        }, true){};
        CustomIngredient chaoticCore = new CustomIngredient(Material.ENDER_PEARL, "Chaotic Core", Rarity.LEGENDARY, null, true){};
        CustomIngredient godlyEssence = new CustomIngredient(Material.SUNFLOWER, "Godly Essence", Rarity.GODLIKE, null, true){};
        CustomIngredient elementalNexus = new CustomIngredient(Material.MAGMA_CREAM, "Elemental Nexus", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(elementalCore, 16), null,
                new CustomItemStack(elementalCore, 16), new CustomItemStack(chaoticCore, 1), new CustomItemStack(elementalCore, 16),
                null, new CustomItemStack(elementalCore, 16), null
        }, true){};
        CustomIngredient reinforcedWeb = new CustomIngredient(Material.COBWEB, "Reinforced Web", Rarity.EPIC, new CustomItemStack[]{
            null, new CustomItemStack(enchantedWeb, 32), null,
            new CustomItemStack(enchantedWeb, 32), new CustomItemStack(enchantedIronBlock, 32), new CustomItemStack(enchantedWeb, 32),
            null, new CustomItemStack(enchantedWeb, 32), null
        }, true){};
        CustomIngredient inactiveDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Inactive Demonic Essence", Rarity.LOVECRAFTIAN, null, false){};
        CustomIngredient activeDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Demonic Essence", Rarity.LOVECRAFTIAN, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus, 64), null,
                new CustomItemStack(elementalNexus, 64), new CustomItemStack(inactiveDemonicEssence), new CustomItemStack(elementalNexus, 64),
                null, new CustomItemStack(elementalNexus, 64), null
        }, true){};
        CustomIngredient demonCore = new CustomIngredient(Material.ENDER_EYE, "Demon Core", Rarity.LOVECRAFTIAN, null, true){};
        CustomIngredient demonicStick = new CustomIngredient(Material.STICK, "Demon-Infused Stick", Rarity.LOVECRAFTIAN, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 64), null,
                new CustomItemStack(compactStick, 64), new CustomItemStack(demonCore), new CustomItemStack(compactStick, 64),
                null, new CustomItemStack(compactStick, 64), null
        }, true){};
        CustomIngredient demonicWeb = new CustomIngredient(Material.COBWEB, "Demon-Infused Web", Rarity.LOVECRAFTIAN, new CustomItemStack[]{
               null, new CustomItemStack(reinforcedWeb, 64), null,
               new CustomItemStack(reinforcedWeb, 64), new CustomItemStack(demonCore), new CustomItemStack(reinforcedWeb, 64),
                null, new CustomItemStack(reinforcedWeb, 64), null
        }, true){};

        // weapons
        CustomBow bow = new CustomBow(Material.BOW, "Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(stick, 1), new CustomItemStack(string, 1),
                new CustomItemStack(stick, 1), null, new CustomItemStack(string, 1),
                null, new CustomItemStack(stick, 1), new CustomItemStack(string, 1)
        }, List.of(Element.NEUTRAL), List.of(50), List.of(100)) {
            @Override
            public void onUseLeft(Player player) {

            }

            @Override
            public void onUseRight(Player player) {

            }
        };
        CustomShortbow shortbow = new Shortbow("Shortbow", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1),
                new CustomItemStack(enchantedStick, 1), new CustomItemStack(bow, 1), new CustomItemStack(enchantedString, 1),
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1)
        }, List.of(Element.NEUTRAL), List.of(75), List.of(150));
        ElementalShortbow elementalShortbow = new ElementalShortbow("Elemental Shortbow", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(shortbow, 1), new CustomItemStack(elementalCore, 4),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.NEUTRAL), List.of(100, 100, 100, 50), List.of(200, 200, 200, 100));
        ChaoticShortbow chaoticShortbow = new ChaoticShortbow("Chaotic Shortbow", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(elementalShortbow), new CustomItemStack(chaoticCore, 1),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.CHAOS), List.of(999), List.of(9999));
        Apollo apollo = new Apollo("Apollo", Rarity.GODLIKE, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32),
                new CustomItemStack(elementalNexus), new CustomItemStack(chaoticShortbow), new CustomItemStack(godlyEssence, 1),
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32)
        }, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.CHAOS, Element.NEUTRAL), List.of(1000, 750, 1000, 5000, 5000), List.of(1500, 1250, 1500, 5000, 10000));
        Azathoth azathoth = new Azathoth("Azathoth", Rarity.LOVECRAFTIAN, new CustomItemStack[]{
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb),
                new CustomItemStack(demonicStick), new CustomItemStack(apollo), new CustomItemStack(activeDemonicEssence),
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb)
        }, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.CHAOS, Element.NEUTRAL), List.of(9999, 9999, 9999, 9999, 49999), List.of(19999, 19999, 19999, 9999, 99999));

        // loot tables
        // name is l + level + entity name + Loot
        CustomLootTable l1SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(1, 3));
        CustomLootTable l10SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(4, 7));
        CustomLootTable l25SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(45, 57));
        CustomLootTable l50SpiderLoot = new CustomLootTable(List.of(enchantedString), List.of(100D), List.of(1, 3));
        CustomLootTable l100SpiderLoot = new CustomLootTable(List.of(enchantedWeb), List.of(100D), List.of(1, 3));

        // mobs
        // constructor: name, elements, damage, level, defense, maxHealth
        CustomBaseMob spider1 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 10, 1, 100, 100, l1SpiderLoot, true){};
        CustomBaseMob spider10 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 100, 10, 200, 1000, l10SpiderLoot, true){};
        CustomBaseMob spider25 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 250, 25, 500, 5000, l25SpiderLoot, true){};
        CustomBaseMob spider50 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 500, 50, 750, 10000, l50SpiderLoot, true){};
        CustomBaseMob spider100 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 1000, 100, 1000, 50000, l100SpiderLoot, true){};
        CustomBaseMob wither100 = new CustomBaseMob("Wither", EntityType.WITHER, List.of(Element.FIRE), 100, 100, 50000, 1000000000, null, true){};

        for (CustomItem item : CustomItem.CUSTOM_ITEMS.values()) {
            item.createRecipe();
        }

        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }
}
