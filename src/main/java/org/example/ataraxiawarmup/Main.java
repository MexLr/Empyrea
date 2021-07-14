package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.ataraxiawarmup.item.customitem.*;
import org.example.ataraxiawarmup.item.customitem.ability.Ability;
import org.example.ataraxiawarmup.item.customitem.ability.AbilityApplyingInventoryListener;
import org.example.ataraxiawarmup.item.customitem.ability.CustomArtifact;
import org.example.ataraxiawarmup.item.customitem.boss.PedestalListener;
import org.example.ataraxiawarmup.item.customitem.boss.SummonPedestal;
import org.example.ataraxiawarmup.item.customitem.trinity.Eidolon;
import org.example.ataraxiawarmup.mob.*;
import org.example.ataraxiawarmup.mob.boss.BossType;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.player.CustomPlayerListener;
import org.example.ataraxiawarmup.projectiletrail.ApplierListener;
import org.example.ataraxiawarmup.projectiletrail.ArrowShootListener;
import org.example.ataraxiawarmup.spawner.SpawnerListener;

import java.util.*;

public class Main extends JavaPlugin {

    public static final ItemStack FILLER_ITEM; // filler item for inventories
    public static final ItemStack BACK_ARROW; // back arrow for inventories
    public static final ItemStack CLOSE_BARRIER; // close barrier for inventories
    public static final ItemStack SHAPELESS_INDICATOR; // indicates whether or nota shown recipe is shapeless

    public static final CustomIngredient CUSTOM_AIR = new CustomIngredient(Material.AIR, "aaaaaaaaaa", Rarity.NULL, null, false); // a null item without actually being null, for shapeless recipes

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

        SHAPELESS_INDICATOR = new ItemStack(Material.WARPED_SIGN);
        ItemMeta shapelessMeta = SHAPELESS_INDICATOR.getItemMeta();
        shapelessMeta.setDisplayName(ChatColor.AQUA + "This recipe is shapeless.");
        shapelessMeta.setLore(null);
        SHAPELESS_INDICATOR.setItemMeta(shapelessMeta);
    }

    // so MANIFEST.MF recognizes this as a main class
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        new SpawnerListener(this);
        new CommandListener(this);
        new ArrowShootListener(this);
        new ApplierListener(this);
        new RecipeListener(this);
        new CustomItemListener(this);
        new RecipeInventoryListener(this);
        new CustomMobListener(this);
        new AbilityApplyingInventoryListener(this);
        new CustomPlayerListener(this);
        new PedestalListener(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            CustomPlayer customPlayer = new CustomPlayer(player);
            Bukkit.getScheduler().runTask(this, () -> {
                customPlayer.updateAttributes();
            });
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    customPlayer.regenHealth(2.5);
                    customPlayer.regenAbilityCharge(5.0);
                }
            }
        }.runTaskTimer(this, 0, 40);

        // PUT SUMMON PEDESTALS IN SEPARATE AREAS TO REDUCE LAG FROM ARMOR STANDS

        new SummonPedestal(new Location(Bukkit.getWorld("Hub"), 3468, 90, -204), UUID.fromString("72371adf-2e8d-4c92-a734-a93c8279deb9"), 2, BossType.WITHER);
        new SummonPedestal(new Location(Bukkit.getWorld("Hub"), 3473, 90, -205), UUID.fromString("72371adf-2e8d-4c92-a734-a93c8279deb9"), 4, BossType.WITHER);
        // new SummonPedestal(new Location(Bukkit.getWorld("Hub"), 1568, 93, -161), UUID.fromString("72371adf-2e8d-4c92-a734-a93c8279deb9"), 2, "The Wither");
        // new SummonPedestal(new Location(Bukkit.getWorld("Hub"), 1570, 93, -163), UUID.fromString("72371adf-2e8d-4c92-a734-a93c8279deb9"), 3, "The Wither");
        // new SummonPedestal(new Location(Bukkit.getWorld("Hub"), 1572, 93, -161), UUID.fromString("72371adf-2e8d-4c92-a734-a93c8279deb9"), 4, "The Wither");

        // items
        // ingredients
        CustomIngredient stick = new CustomIngredient(Material.STICK, "Stick", Rarity.COMMON, null, false);
        CustomIngredient string = new CustomIngredient(Material.STRING, "String", Rarity.COMMON, null, false);
        CustomIngredient spiderEye = new CustomIngredient(Material.SPIDER_EYE, "Spider Eye", Rarity.COMMON, null, false);
        CustomIngredient copper = new CustomIngredient(Material.COPPER_INGOT, "Copper Ingot", Rarity.COMMON, null, false);
        CustomIngredient nautilus = new CustomIngredient(Material.NAUTILUS_SHELL, "Nautilus Shell", Rarity.COMMON, null, false);
        CustomIngredient iron = new CustomIngredient(Material.IRON_INGOT, "Iron Ingot", Rarity.COMMON, null, false);
        CustomIngredient gold = new CustomIngredient(Material.GOLD_INGOT, "Gold Ingot", Rarity.COMMON, null, false);
        CustomIngredient blazeRod = new CustomIngredient(Material.BLAZE_ROD, "Blaze Rod", Rarity.COMMON, null, false);
        CustomIngredient magmaCream = new CustomIngredient(Material.BLAZE_ROD, "Magma Cream", Rarity.COMMON, null, false);
        CustomIngredient rottenFlesh = new CustomIngredient(Material.ROTTEN_FLESH, "Rotten Flesh", Rarity.COMMON, null, false);
        CustomIngredient bone = new CustomIngredient(Material.BONE, "Bone", Rarity.COMMON, null, false);
        CustomIngredient coal = new CustomIngredient(Material.COAL, "Coal", Rarity.COMMON, null, false);
        CustomIngredient porkchop = new CustomIngredient(Material.PORKCHOP, "Porkchop", Rarity.COMMON, null, false);
        CustomIngredient leather = new CustomIngredient(Material.LEATHER, "Leather", Rarity.COMMON, null, false);
        CustomIngredient amethyst = new CustomIngredient(Material.AMETHYST_SHARD, "Amethyst", Rarity.COMMON, null, false);
        CustomIngredient diamond = new CustomIngredient(Material.DIAMOND, "Diamond", Rarity.COMMON, null, false);
        CustomIngredient prisShard = new CustomIngredient(Material.PRISMARINE_SHARD, "Prismarine Shard", Rarity.COMMON, null, false);
        CustomIngredient prisCrystal = new CustomIngredient(Material.PRISMARINE_CRYSTALS, "Prismarine Crystals", Rarity.COMMON, null, false);
        CustomIngredient gunpowder = new CustomIngredient(Material.GUNPOWDER, "Gunpowder", Rarity.COMMON, null, false);
        CustomIngredient soulSand = new CustomIngredient(Material.SOUL_SAND, "Soul Sand", Rarity.UNCOMMON, null, false);
        CustomIngredient witherRose = new CustomIngredient(Material.WITHER_ROSE, "Wither Rose", Rarity.UNCOMMON, null, false);
        CustomIngredient ghastTear = new CustomIngredient(Material.GHAST_TEAR, "Ghast Tear", Rarity.UNCOMMON, null, false);
        CustomIngredient flamingPumpkin = new CustomIngredient(Material.JACK_O_LANTERN, "Flaming Pumpkin", Rarity.UNCOMMON, null, false);
        CustomIngredient theWithersAshes = new CustomIngredient(Material.SOUL_SAND, "The Wither's Ashes", Rarity.RARE, null, false);
        CustomIngredient enchantedStick = new CustomIngredient(Material.STICK, "Enchanted Stick", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(stick, 64), null,
                new CustomItemStack(stick, 64), new CustomItemStack(stick, 64), new CustomItemStack(stick, 64),
                null, new CustomItemStack(stick, 64), null
        }, true, true){};
        CustomIngredient enchantedString = new CustomIngredient(Material.STRING, "Enchanted String", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(string, 32), null,
                new CustomItemStack(string, 32), new CustomItemStack(string, 32), new CustomItemStack(string, 32),
                null, new CustomItemStack(string, 32), null
        }, true, true){};
        CustomIngredient enchantedIron = new CustomIngredient(Material.IRON_INGOT, "Enchanted Iron Ingot", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(iron, 32), null,
                new CustomItemStack(iron, 32), new CustomItemStack(iron, 32), new CustomItemStack(iron, 32),
                null, new CustomItemStack(iron, 32), null
        }, true, true){};
        CustomIngredient enchantedFlesh = new CustomIngredient(Material.ROTTEN_FLESH, "Enchanted Rotten Flesh", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(rottenFlesh, 32), null,
                new CustomItemStack(rottenFlesh, 32), new CustomItemStack(rottenFlesh, 32), new CustomItemStack(rottenFlesh, 32),
                null, new CustomItemStack(rottenFlesh, 32), null
        }, true, true){};
        CustomIngredient enchantedAmethyst = new CustomIngredient(Material.AMETHYST_SHARD, "Enchanted Amethyst", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(amethyst, 32), null,
                new CustomItemStack(amethyst, 32), new CustomItemStack(amethyst, 32), new CustomItemStack(amethyst, 32),
                null, new CustomItemStack(amethyst, 32), null
        }, true, true){};
        CustomIngredient enchantedDiamond = new CustomIngredient(Material.IRON_INGOT, "Enchanted Diamond", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(diamond, 32), null,
                new CustomItemStack(diamond, 32), new CustomItemStack(diamond, 32), new CustomItemStack(diamond, 32),
                null, new CustomItemStack(diamond, 32), null
        }, true, true){};
        CustomIngredient enchantedMagmaCream = new CustomIngredient(Material.MAGMA_CREAM, "Enchanted Magma Cream", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(magmaCream, 32), null,
                new CustomItemStack(magmaCream, 32), new CustomItemStack(magmaCream, 32), new CustomItemStack(magmaCream, 32),
                null, new CustomItemStack(magmaCream, 32), null
        }, true, true){};
        CustomIngredient enchantedBlazeRod = new CustomIngredient(Material.BLAZE_ROD, "Enchanted Blaze Rod", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(blazeRod, 32), null,
                new CustomItemStack(blazeRod, 32), new CustomItemStack(blazeRod, 32), new CustomItemStack(blazeRod, 32),
                null, new CustomItemStack(blazeRod, 32), null
        }, true, true){};
        CustomIngredient compactStick = new CustomIngredient(Material.STICK, "Compact Stick", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 64), null,
                new CustomItemStack(enchantedStick, 64), new CustomItemStack(enchantedStick, 64), new CustomItemStack(enchantedStick, 64),
                null, new CustomItemStack(enchantedStick, 64), null
        }, true, true){};
        CustomIngredient compactFlesh = new CustomIngredient(Material.ROTTEN_FLESH, "Compact Rotten Flesh", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedFlesh, 32), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedFlesh, 32), null
        }, true, true){};
        CustomIngredient enchantedWeb = new CustomIngredient(Material.COBWEB, "Enchanted Web", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedString, 32), null,
                new CustomItemStack(enchantedString, 32), new CustomItemStack(enchantedString, 32), new CustomItemStack(enchantedString, 32),
                null, new CustomItemStack(enchantedString, 32), null
        }, true, true){};
        CustomIngredient enchantedIronBlock = new CustomIngredient(Material.IRON_BLOCK, "Enchanted Iron Block", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedIron, 32), null,
                new CustomItemStack(enchantedIron, 32), new CustomItemStack(enchantedIron, 32), new CustomItemStack(enchantedIron, 32),
                null, new CustomItemStack(enchantedIron, 32), null
        }, true, true){};
        CustomIngredient enchantedAmethystBlock = new CustomIngredient(Material.AMETHYST_BLOCK, "Enchanted Amethyst Block", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedAmethyst, 32), null,
                new CustomItemStack(enchantedAmethyst, 32), new CustomItemStack(enchantedAmethyst, 32), new CustomItemStack(enchantedAmethyst, 32),
                null, new CustomItemStack(enchantedAmethyst, 32), null
        }, true, true){};
        CustomIngredient enchantedDiamondBlock = new CustomIngredient(Material.DIAMOND_BLOCK, "Enchanted Diamond Block", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedDiamond, 32), null,
                new CustomItemStack(enchantedDiamond, 32), new CustomItemStack(enchantedDiamond, 32), new CustomItemStack(enchantedDiamond, 32),
                null, new CustomItemStack(enchantedDiamond, 32), null
        }, true, true){};
        CustomIngredient fireShard = new CustomIngredient(Material.BLAZE_POWDER, "Fire Shard", Rarity.UNCOMMON, null, true);
        CustomIngredient earthShard = new CustomIngredient(Material.WHEAT_SEEDS, "Earth Shard", Rarity.UNCOMMON, null, true);
        CustomIngredient waterShard = new CustomIngredient(Material.PRISMARINE_SHARD, "Water Shard", Rarity.UNCOMMON, null, true);
        CustomIngredient thunderShard = new CustomIngredient(Material.FIREWORK_STAR, "Thunder Shard", Rarity.UNCOMMON, null, true);
        CustomIngredient airShard = new CustomIngredient(Material.FEATHER, "Air Shard", Rarity.UNCOMMON, null, true);
        CustomIngredient chaosShard = new CustomIngredient(Material.ENDER_PEARL, "Chaos Shard", Rarity.UNCOMMON, null, true);

        CustomIngredient inactiveFireCore = new CustomIngredient(Material.BLAZE_ROD, "Inactive Fire Core", Rarity.UNCOMMON, null, false);
        CustomIngredient inactiveWaterCore = new CustomIngredient(Material.HEART_OF_THE_SEA, "Inactive Water Core", Rarity.UNCOMMON, null, false);
        CustomIngredient inactiveEarthCore = new CustomIngredient(Material.OAK_SAPLING, "Inactive Earth Core", Rarity.UNCOMMON, null, false);
        CustomIngredient inactiveThunderCore = new CustomIngredient(Material.SUNFLOWER, "Inactive Thunder Core", Rarity.UNCOMMON, null, false);
        CustomIngredient inactiveAirCore = new CustomIngredient(Material.PHANTOM_MEMBRANE, "Inactive Air Core", Rarity.UNCOMMON, null, false);
        CustomIngredient inactiveChaosCore = new CustomIngredient(Material.ENDER_EYE, "Inactive Chaos Core", Rarity.UNCOMMON, null, false);

        CustomIngredient inactiveElementalCore = new CustomIngredient(Material.CYAN_DYE, "Inactive Elemental Core", Rarity.RARE, null, false);

        CustomIngredient fireEssence = new CustomIngredient(Material.BLAZE_ROD, "Fire Essence", Rarity.LEGENDARY, null, true);
        CustomIngredient waterEssence = new CustomIngredient(Material.HEART_OF_THE_SEA, "Water Essence", Rarity.LEGENDARY, null, true);
        CustomIngredient earthEssence = new CustomIngredient(Material.OAK_SAPLING, "Earth Essence", Rarity.LEGENDARY, null, true);
        CustomIngredient thunderEssence = new CustomIngredient(Material.SUNFLOWER, "Thunder Essence", Rarity.LEGENDARY, null, true);
        CustomIngredient airEssence = new CustomIngredient(Material.PHANTOM_MEMBRANE, "Air Essence", Rarity.LEGENDARY, null, true);
        CustomIngredient chaosEssence = new CustomIngredient(Material.ENDER_EYE, "Chaos Essence", Rarity.LEGENDARY, null, true);

        CustomIngredient fireCore = new CustomIngredient(Material.BLAZE_ROD, "Fire Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(fireShard, 16), null,
                new CustomItemStack(fireShard, 16), new CustomItemStack(inactiveFireCore, 1), new CustomItemStack(fireShard, 16),
                null, new CustomItemStack(fireShard, 16), null
        }, true, true);
        CustomIngredient waterCore = new CustomIngredient(Material.HEART_OF_THE_SEA, "Water Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(waterShard, 16), null,
                new CustomItemStack(waterShard, 16), new CustomItemStack(inactiveWaterCore, 1), new CustomItemStack(waterShard, 16),
                null, new CustomItemStack(waterShard, 16), null
        }, true, true);
        CustomIngredient earthCore = new CustomIngredient(Material.OAK_SAPLING, "Earth Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(earthShard, 16), null,
                new CustomItemStack(earthShard, 16), new CustomItemStack(inactiveEarthCore, 1), new CustomItemStack(earthShard, 16),
                null, new CustomItemStack(earthShard, 16), null
        }, true, true);
        CustomIngredient thunderCore = new CustomIngredient(Material.SUNFLOWER, "Thunder Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(thunderShard, 16), null,
                new CustomItemStack(thunderShard, 16), new CustomItemStack(inactiveThunderCore, 1), new CustomItemStack(thunderShard, 16),
                null, new CustomItemStack(thunderShard, 16), null
        }, true, true);
        CustomIngredient airCore = new CustomIngredient(Material.PHANTOM_MEMBRANE, "Air Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(airShard, 16), null,
                new CustomItemStack(airShard, 16), new CustomItemStack(inactiveAirCore, 1), new CustomItemStack(airShard, 16),
                null, new CustomItemStack(airShard, 16), null
        }, true, true);
        CustomIngredient chaosCore = new CustomIngredient(Material.ENDER_EYE, "Chaos Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(chaosShard, 16), null,
                new CustomItemStack(chaosShard, 16), new CustomItemStack(inactiveChaosCore, 1), new CustomItemStack(chaosShard, 16),
                null, new CustomItemStack(chaosShard, 16), null
        }, true, true);

        CustomIngredient elementalCore = new CustomIngredient(Material.CYAN_DYE, "Elemental Core", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(fireCore, 64), new CustomItemStack(waterCore, 64), new CustomItemStack(earthCore, 64),
                null, new CustomItemStack(inactiveElementalCore, 1), null,
                new CustomItemStack(thunderCore, 64), new CustomItemStack(airCore, 64), new CustomItemStack(chaosCore, 64)
        }, true, true);

        CustomIngredient chaoticCore = new CustomIngredient(Material.ENDER_PEARL, "Chaotic Core", Rarity.LEGENDARY, null, true){};
        CustomIngredient godlyEssence = new CustomIngredient(Material.SUNFLOWER, "Godly Essence", Rarity.GODLIKE, null, true){};
        CustomIngredient elementalNexus = new CustomIngredient(Material.MAGMA_CREAM, "Elemental Nexus", Rarity.LEGENDARY, new CustomItemStack[]{
                new CustomItemStack(fireEssence, 1), new CustomItemStack(waterEssence, 1), new CustomItemStack(earthEssence, 1),
                null, new CustomItemStack(elementalCore, 64), null,
                new CustomItemStack(thunderEssence, 1), new CustomItemStack(airEssence, 1), new CustomItemStack(chaosEssence, 1)
        }, true, true){};
        CustomIngredient leviathanWish = new CustomIngredient(Material.HEART_OF_THE_SEA, "Leviathan's Last Wish", Rarity.GODLIKE, null, true);
        CustomIngredient indraBreath = new CustomIngredient(Material.FIREWORK_STAR, "Indra's Dying Breath", Rarity.GODLIKE, null, true);
        CustomIngredient artemisSoul = new CustomIngredient(Material.BLACK_DYE, "Artemis' Soul", Rarity.GODLIKE, null, true);
        CustomIngredient reinforcedWeb = new CustomIngredient(Material.COBWEB, "Reinforced Web", Rarity.EPIC, new CustomItemStack[]{
            null, new CustomItemStack(enchantedWeb, 32), null,
            new CustomItemStack(enchantedWeb, 32), new CustomItemStack(enchantedIronBlock, 32), new CustomItemStack(enchantedWeb, 32),
            null, new CustomItemStack(enchantedWeb, 32), null
        }, true, true){};
        CustomIngredient inactiveDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Inactive Demonic Essence", Rarity.TRINITY, null, false){};
        CustomIngredient activeDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Demonic Essence", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus, 64), null,
                new CustomItemStack(elementalNexus, 64), new CustomItemStack(inactiveDemonicEssence), new CustomItemStack(elementalNexus, 64),
                null, new CustomItemStack(elementalNexus, 64), null
        }, true, true){};
        CustomIngredient demonCore = new CustomIngredient(Material.ENDER_EYE, "Demon Core", Rarity.TRINITY, null, true){};
        CustomIngredient demonicStick = new CustomIngredient(Material.STICK, "Demon-Infused Stick", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 64), null,
                new CustomItemStack(compactStick, 64), new CustomItemStack(demonCore), new CustomItemStack(compactStick, 64),
                null, new CustomItemStack(compactStick, 64), null
        }, true, true){};
        CustomIngredient demonicWeb = new CustomIngredient(Material.COBWEB, "Demon-Infused Web", Rarity.TRINITY, new CustomItemStack[]{
               null, new CustomItemStack(reinforcedWeb, 64), null,
               new CustomItemStack(reinforcedWeb, 64), new CustomItemStack(demonCore), new CustomItemStack(reinforcedWeb, 64),
                null, new CustomItemStack(reinforcedWeb, 64), null
        }, true, true){};

        // artifacts
        CustomArtifact fire1 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier I", Rarity.UNCOMMON, null, Ability.METEOR, 1);
        CustomArtifact fire2 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64),
                new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64),
                new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64), new CustomItemStack(fire1, 64)
        }, Ability.METEOR, 2);
        CustomArtifact fire3 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64),
                new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64),
                new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64), new CustomItemStack(fire2, 64)
        }, Ability.METEOR, 3);

        CustomArtifact water1 = new CustomArtifact(Material.PRISMARINE_SHARD, "Water Artifact Tier I", Rarity.UNCOMMON, null, Ability.WATER, 1);
        CustomArtifact water2 = new CustomArtifact(Material.PRISMARINE_SHARD, "Water Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(water1, 64), new CustomItemStack(water1, 64), new CustomItemStack(water1, 64),
                new CustomItemStack(water1, 64), new CustomItemStack(water1, 64), new CustomItemStack(water1, 64),
                new CustomItemStack(water1, 64), new CustomItemStack(water1, 64), new CustomItemStack(water1, 64)
        }, Ability.WATER, 2);
        CustomArtifact water3 = new CustomArtifact(Material.PRISMARINE_SHARD, "Water Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(water2, 64), new CustomItemStack(water2, 64), new CustomItemStack(water2, 64),
                new CustomItemStack(water2, 64), new CustomItemStack(water2, 64), new CustomItemStack(water2, 64),
                new CustomItemStack(water2, 64), new CustomItemStack(water2, 64), new CustomItemStack(water2, 64)
        }, Ability.WATER, 3);

        CustomArtifact earth1 = new CustomArtifact(Material.WHEAT_SEEDS, "Earth Artifact Tier I", Rarity.UNCOMMON, null, Ability.EARTH, 1);
        CustomArtifact earth2 = new CustomArtifact(Material.WHEAT_SEEDS, "Earth Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64),
                new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64),
                new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64), new CustomItemStack(earth1, 64)
        }, Ability.EARTH, 2);
        CustomArtifact earth3 = new CustomArtifact(Material.WHEAT_SEEDS, "Earth Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64),
                new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64),
                new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64), new CustomItemStack(earth2, 64)
        }, Ability.EARTH, 3);

        CustomArtifact thunder1 = new CustomArtifact(Material.FIREWORK_STAR, "Thunder Artifact Tier I", Rarity.UNCOMMON, null, Ability.THUNDER, 1);
        CustomArtifact thunder2 = new CustomArtifact(Material.FIREWORK_STAR, "Thunder Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64),
                new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64),
                new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64), new CustomItemStack(thunder1, 64)
        }, Ability.THUNDER, 2);
        CustomArtifact thunder3 = new CustomArtifact(Material.FIREWORK_STAR, "Thunder Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64),
                new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64),
                new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64), new CustomItemStack(thunder2, 64)
        }, Ability.THUNDER, 3);

        CustomArtifact air1 = new CustomArtifact(Material.FEATHER, "Air Artifact Tier I", Rarity.UNCOMMON, null, Ability.AIR, 1);
        CustomArtifact air2 = new CustomArtifact(Material.FEATHER, "Air Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(air1, 64), new CustomItemStack(air1, 64), new CustomItemStack(air1, 64),
                new CustomItemStack(air1, 64), new CustomItemStack(air1, 64), new CustomItemStack(air1, 64),
                new CustomItemStack(air1, 64), new CustomItemStack(air1, 64), new CustomItemStack(air1, 64)
        }, Ability.AIR, 2);
        CustomArtifact air3 = new CustomArtifact(Material.FEATHER, "Air Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(air2, 64), new CustomItemStack(air2, 64), new CustomItemStack(air2, 64),
                new CustomItemStack(air2, 64), new CustomItemStack(air2, 64), new CustomItemStack(air2, 64),
                new CustomItemStack(air2, 64), new CustomItemStack(air2, 64), new CustomItemStack(air2, 64)
        }, Ability.AIR, 3);

        CustomArtifact chaos1 = new CustomArtifact(Material.ENDER_PEARL, "Chaos Artifact Tier I", Rarity.UNCOMMON, null, Ability.CHAOS, 1);
        CustomArtifact chaos2 = new CustomArtifact(Material.ENDER_PEARL, "Chaos Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64),
                new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64),
                new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64), new CustomItemStack(chaos1, 64)
        }, Ability.CHAOS, 2);
        CustomArtifact chaos3 = new CustomArtifact(Material.ENDER_PEARL, "Chaos Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64),
                new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64),
                new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64), new CustomItemStack(chaos2, 64)
        }, Ability.CHAOS, 3);


        // bows

        CustomBow bow = new CustomBow(Material.BOW, "Bow", Rarity.COMMON, new CustomItemStack[]{
                null, new CustomItemStack(stick, 1), new CustomItemStack(string, 1),
                new CustomItemStack(stick, 1), null, new CustomItemStack(string, 1),
                null, new CustomItemStack(stick, 1), new CustomItemStack(string, 1)
        }, List.of(Element.NEUTRAL), List.of(50), List.of(75), Map.of(), "Just a regular bow.");

        CustomBow flamingBow = new CustomBow(Material.BOW, "Flaming Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4),
                new CustomItemStack(enchantedStick, 4), new CustomItemStack(bow, 1), new CustomItemStack(fireShard, 1),
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4)
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(20, 50), List.of(50, 75), Map.of(ItemAttribute.FIREDEF, 10, ItemAttribute.FIREPERCENT, 5), "A bow born from flame.");

        CustomBow aqueousBow = new CustomBow(Material.BOW, "Aqueous Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4),
                new CustomItemStack(enchantedStick, 4), new CustomItemStack(bow, 1), new CustomItemStack(waterShard, 1),
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4)
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(20, 50), List.of(50, 75), Map.of(ItemAttribute.WATERDEF, 10, ItemAttribute.WATERPERCENT, 5), "A bow born from the ocean.");

        CustomBow terrestrialBow = new CustomBow(Material.BOW, "Terrestrial Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4),
                new CustomItemStack(enchantedStick, 4), new CustomItemStack(bow, 1), new CustomItemStack(earthShard, 1),
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4)
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(20, 50), List.of(50, 75), Map.of(ItemAttribute.EARTHDEF, 10, ItemAttribute.EARTHPERCENT, 5), "A bow born from the earth.");

        CustomBow lightningBow = new CustomBow(Material.BOW, "Lightning Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4),
                new CustomItemStack(enchantedStick, 4), new CustomItemStack(bow, 1), new CustomItemStack(thunderShard, 1),
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4)
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(20, 50), List.of(50, 75), Map.of(ItemAttribute.THUNDERDEF, 10, ItemAttribute.THUNDERPERCENT, 5), "A bow born from the storms.");

        CustomBow zephyrBow = new CustomBow(Material.BOW, "Zephyr Bow", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4),
                new CustomItemStack(enchantedStick, 4), new CustomItemStack(bow, 1), new CustomItemStack(airShard, 1),
                null, new CustomItemStack(enchantedStick, 4), new CustomItemStack(enchantedString, 4)
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(20, 50), List.of(50, 75), Map.of(ItemAttribute.AIRDEF, 10, ItemAttribute.AIRPERCENT, 5), "A bow born from the winds.");

        CustomBow inferno = new CustomBow(Material.BOW, "Inferno", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(flamingBow, 1), new CustomItemStack(fireCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(50, 150), List.of(75, 200), Map.of(ItemAttribute.FIREDEF, 20, ItemAttribute.FIREPERCENT, 10), "Raging on and on, destroying everything in its wake.");

        CustomBow tsunami = new CustomBow(Material.BOW, "Tsunami", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(aqueousBow, 1), new CustomItemStack(waterCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(50, 150), List.of(75, 200), Map.of(ItemAttribute.WATERDEF, 20, ItemAttribute.WATERPERCENT, 10), "A cruel force of aquatic destruction.");

        CustomBow earthquake = new CustomBow(Material.BOW, "Earthquake", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(terrestrialBow, 1), new CustomItemStack(earthCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(50, 150), List.of(75, 200), Map.of(ItemAttribute.EARTHDEF, 20, ItemAttribute.EARTHPERCENT, 10), "Earthly destructive turbulence.");

        CustomBow thunderstorm = new CustomBow(Material.BOW, "Thunderstorm", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(lightningBow, 1), new CustomItemStack(thunderCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(50, 125), List.of(75, 175), Map.of(ItemAttribute.THUNDERDEF, 20, ItemAttribute.THUNDERPERCENT, 10), "A stormy, electric force of destruction.");

        CustomBow cyclone = new CustomBow(Material.BOW, "Cyclone", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(zephyrBow, 1), new CustomItemStack(airCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(50, 80), List.of(75, 120), Map.of(ItemAttribute.AIRDEF, 20, ItemAttribute.AIRPERCENT, 10), "An incredibly strong and detructive tempest.");

        CustomBow disarray = new CustomBow(Material.BOW, "Disarray", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(chaosCore, 1), new CustomItemStack(enchantedString, 32),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.CHAOS), List.of(50, 75), List.of(75, 100), Map.of(ItemAttribute.CHAOSDEF, 20, ItemAttribute.CHAOSPERCENT, 10), "A bow born from havoc.");

        CustomBow muspelheim = new CustomBow(Material.BOW, "Muspelheim", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(inferno, 1), new CustomItemStack(fireCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(150, 500), List.of(300, 750), Map.of(ItemAttribute.FIREDEF, 50, ItemAttribute.FIREPERCENT, 25), "The power of primordial fire rests in your hands.");

        CustomBow niflheim = new CustomBow(Material.BOW, "Niflheim", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(tsunami, 1), new CustomItemStack(waterCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(150, 500), List.of(400, 1000), Map.of(ItemAttribute.WATERDEF, 50, ItemAttribute.WATERPERCENT, 25), "The power of primordial ice and water rests in your hands.");

        CustomBow yggdrasil = new CustomBow(Material.BOW, "Yggdrasil", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(earthquake, 1), new CustomItemStack(earthCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(150, 575), List.of(300, 675), Map.of(ItemAttribute.EARTHDEF, 50, ItemAttribute.EARTHPERCENT, 25), "The near-indestructible World Tree, the force that binds everything together.");

        CustomBow gungnir = new CustomBow(Material.BOW, "Gungnir", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(thunderstorm, 1), new CustomItemStack(thunderCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(150, 100), List.of(300, 1500), Map.of(ItemAttribute.THUNDERDEF, 50, ItemAttribute.THUNDERPERCENT, 25), "Once the spear of Odin, now in your hands. As a bow. Don't ask questions.");

        CustomBow asgard = new CustomBow(Material.BOW, "Asgard", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(cyclone, 1), new CustomItemStack(airCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(150, 500), List.of(300, 600), Map.of(ItemAttribute.AIRDEF, 50, ItemAttribute.AIRPERCENT, 25), "The power of the Aesir resides in this bow.");

        CustomBow nidavellir = new CustomBow(Material.BOW, "Nidavellir", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(disarray, 1), new CustomItemStack(chaosCore, 64),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, List.of(Element.NEUTRAL, Element.CHAOS), List.of(150, 400), List.of(300, 600), Map.of(ItemAttribute.CHAOSDEF, 50, ItemAttribute.CHAOSPERCENT, 25), "The power of the dark elves resides in this bow.");

        CustomBow norn = new CustomBow(Material.BOW, "Norn", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(elementalCore, 1), new CustomItemStack(muspelheim, 1), new CustomItemStack(elementalCore, 1),
                new CustomItemStack(niflheim, 1), new CustomItemStack(nidavellir, 1), new CustomItemStack(yggdrasil, 1),
                new CustomItemStack(gungnir, 1), new CustomItemStack(elementalCore, 1), new CustomItemStack(asgard, 1)
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 250, 300), List.of(500, 500, 300, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 40, ItemAttribute.ALLPERCENT, 20), "The weaver of fate.");

        CustomBow fireImbuedNorn = new CustomBow(Material.BOW, "Fire-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(fireEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 250, 1500), List.of(500, 500, 300, 750, 350, 500, 2000), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.FIREDEF, 50, ItemAttribute.FIREPERCENT, 25), "The weaver of hellish fate.");

        CustomBow waterImbuedNorn = new CustomBow(Material.BOW, "Water-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(waterEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 1250, 300), List.of(500, 500, 300, 750, 350, 2500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.WATERDEF, 50, ItemAttribute.WATERPERCENT, 25), "The weaver of nautical fate.");

        CustomBow earthImbuedNorn = new CustomBow(Material.BOW, "Earth-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(earthEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 1500, 250, 300), List.of(500, 500, 300, 750, 1750, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.EARTHDEF, 50, ItemAttribute.EARTHPERCENT, 25), "The weaver of planetary fate.");

        CustomBow thunderImbuedNorn = new CustomBow(Material.BOW, "Thunder-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(thunderEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 250, 300, 250, 300), List.of(500, 500, 300, 3750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.THUNDERDEF, 50, ItemAttribute.THUNDERPERCENT, 25), "The weaver of voltaic fate.");

        CustomBow airImbuedNorn = new CustomBow(Material.BOW, "Air-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(airEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 1000, 50, 300, 250, 300), List.of(500, 500, 1500, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.AIRDEF, 50, ItemAttribute.AIRPERCENT, 25), "The weaver of heavenly fate.");

        CustomBow chaosImbuedNorn = new CustomBow(Material.BOW, "Chaos-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(chaosEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 1000, 200, 50, 300, 250, 300), List.of(500, 2500, 300, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.CHAOSDEF, 50, ItemAttribute.CHAOSPERCENT, 25), "The weaver of the apocalypse.");

        CustomBow elementalImbuedNorn = new CustomBow(Material.BOW, "Elemental-Imbued Norn", Rarity.GODLIKE, new CustomItemStack[]{
                new CustomItemStack(elementalNexus, 1), new CustomItemStack(fireImbuedNorn, 1), new CustomItemStack(elementalNexus, 1),
                new CustomItemStack(waterImbuedNorn, 1), new CustomItemStack(earthImbuedNorn, 1), new CustomItemStack(thunderImbuedNorn, 1),
                new CustomItemStack(airImbuedNorn, 1), new CustomItemStack(elementalNexus, 1), new CustomItemStack(chaosImbuedNorn, 1)
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(500, 1000, 1000, 250, 1500, 1250, 1500), List.of(1000, 2500, 1500, 3750, 1750, 2500, 2000), Map.of(ItemAttribute.ALLDEF, 150, ItemAttribute.ALLPERCENT, 66), "The weaver of all fate, everywhere.");

        CustomBow leviathan = new CustomBow(Material.BOW, "Leviathan", Rarity.GODLIKE, new CustomItemStack[]{
                new CustomItemStack(leviathanWish, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64),
                new CustomItemStack(compactStick, 64), new CustomItemStack(elementalImbuedNorn, 1), new CustomItemStack(godlyEssence, 1),
                new CustomItemStack(leviathanWish, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(1000, 400, 400, 100, 600, 2500, 600), List.of(2000, 1000, 600, 1500, 700, 5000, 800), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 50, ItemAttribute.WATERDEF, 500, ItemAttribute.WATERPERCENT, 250), "The gargantuan sea serpent with incredible power.");

        CustomBow indra = new CustomBow(Material.BOW, "Indra", Rarity.GODLIKE, new CustomItemStack[]{
                new CustomItemStack(indraBreath, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64),
                new CustomItemStack(compactStick, 64), new CustomItemStack(elementalImbuedNorn, 1), new CustomItemStack(godlyEssence, 1),
                new CustomItemStack(indraBreath, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(1000, 400, 400, 500, 600, 500, 600), List.of(2000, 1000, 600, 7500, 700, 1000, 800), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 50, ItemAttribute.THUNDERDEF, 500, ItemAttribute.THUNDERPERCENT, 250), "The king of the devas, who wields the power of thunderstorms.");

        CustomBow artemis = new CustomBow(Material.BOW, "Artemis", Rarity.GODLIKE, new CustomItemStack[]{
                new CustomItemStack(artemisSoul, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64),
                new CustomItemStack(compactStick, 64), new CustomItemStack(elementalImbuedNorn, 1), new CustomItemStack(godlyEssence, 1),
                new CustomItemStack(artemisSoul, 1), new CustomItemStack(compactStick, 64), new CustomItemStack(reinforcedWeb, 64)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(1000, 2000, 400, 100, 600, 500, 600), List.of(2000, 5000, 600, 1500, 700, 1000, 800), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 50, ItemAttribute.CHAOSDEF, 500, ItemAttribute.CHAOSPERCENT, 250), "The goddess of the hunt, capable of causing mass chaos.");

        CustomBow eventHorizon = new CustomBow(Material.BOW, "Event Horizon", Rarity.GODLIKE, new CustomItemStack[]{
                null, new CustomItemStack(leviathan, 1), new CustomItemStack(demonCore, 1),
                new CustomItemStack(indra, 1), null, new CustomItemStack(demonCore, 1),
                null, new CustomItemStack(artemis, 1), new CustomItemStack(demonCore, 1)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(5000, 2000, 1000, 500, 1500, 2500, 1500), List.of(10000, 5000, 1500, 7500, 1750, 5000, 2000), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 100, ItemAttribute.CHAOSDEF, 500, ItemAttribute.CHAOSPERCENT, 250, ItemAttribute.THUNDERDEF, 500, ItemAttribute.THUNDERPERCENT, 250, ItemAttribute.WATERDEF, 500, ItemAttribute.WATERPERCENT, 250), "The edge of infinity.");

        CustomBow apotheosis = new CustomBow(Material.BOW, "Apotheosis", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(demonicStick, 1), new CustomItemStack(demonicWeb, 1),
                new CustomItemStack(demonicStick, 1), new CustomItemStack(eventHorizon, 1), new CustomItemStack(activeDemonicEssence, 1),
                null, new CustomItemStack(demonicStick, 1), new CustomItemStack(demonicWeb, 1)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(10000, 12500, 750, 1000, 1000, 20000, 1000), List.of(50000, 25000, 3500, 50000, 5000, 50000, 5000), Map.of(ItemAttribute.ALLDEF, 500, ItemAttribute.ALLPERCENT, 200, ItemAttribute.CHAOSDEF, 1000, ItemAttribute.CHAOSPERCENT, 500, ItemAttribute.THUNDERDEF, 1000, ItemAttribute.THUNDERPERCENT, 500, ItemAttribute.WATERDEF, 1000, ItemAttribute.WATERPERCENT, 500), "Ascension to divinity.");

        CustomBow nadir = new CustomBow(Material.BOW, "Nadir", Rarity.DUALITY, null, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(99999, 500000, 50000, 10, 50000, 50000, 50000), List.of(99999, 1000000, 250000, 999999, 250000, 250000, 250000), Map.of(ItemAttribute.ALLDEF, 9999, ItemAttribute.ALLPERCENT, 9999, ItemAttribute.CHAOSDEF, 10000, ItemAttribute.CHAOSPERCENT, 10000), "Death.");

        // swords
        CustomSword woodenSword = new CustomSword(Material.WOODEN_SWORD, "Wooden Sword", Rarity.COMMON, null, List.of(Element.NEUTRAL), List.of(10), List.of(20), Map.of(), "Just a regular wooden sword.");
        CustomSword stoneSword = new CustomSword(Material.STONE_SWORD, "Stone Sword", Rarity.COMMON, null, List.of(Element.NEUTRAL), List.of(20), List.of(40), Map.of(), "Just a regular stone sword.");
        CustomSword ironSword = new CustomSword(Material.IRON_SWORD, "Iron Sword", Rarity.COMMON, null, List.of(Element.NEUTRAL), List.of(30), List.of(50), Map.of(), "Just a regular iron sword.");
        CustomSword diamondSword = new CustomSword(Material.DIAMOND_SWORD, "Diamond Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, null, null,
                null, null, null,
                null, null, null
        }, List.of(Element.NEUTRAL), List.of(40), List.of(60), Map.of(ItemAttribute.LIFESTEAL, 1), "Just a regular diamond sword.");

        CustomSword flamingSword = new CustomSword(Material.DIAMOND_SWORD, "Flaming Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(fireShard), null,
                new CustomItemStack(enchantedFlesh, 6), new CustomItemStack(diamondSword), new CustomItemStack(enchantedFlesh, 6),
                null, new CustomItemStack(enchantedStick, 8), null
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(10, 35), List.of(25, 50), Map.of(ItemAttribute.FIREDEF, 25, ItemAttribute.LIFESTEAL, 5), "A sword born from flame.");
        CustomSword aqueousSword = new CustomSword(Material.DIAMOND_SWORD, "Aqueous Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(waterShard), null,
                new CustomItemStack(enchantedFlesh, 6), new CustomItemStack(diamondSword), new CustomItemStack(enchantedFlesh, 6),
                null, new CustomItemStack(enchantedStick, 8), null
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(10, 35), List.of(25, 50), Map.of(ItemAttribute.WATERDEF, 25, ItemAttribute.LIFESTEAL, 5), "A sword born from the ocean.");
        CustomSword terrestrialSword = new CustomSword(Material.DIAMOND_SWORD, "Terrestrial Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(earthShard), null,
                new CustomItemStack(enchantedFlesh, 6), new CustomItemStack(diamondSword), new CustomItemStack(enchantedFlesh, 6),
                null, new CustomItemStack(enchantedStick, 8), null
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(10, 35), List.of(25, 50), Map.of(ItemAttribute.EARTHDEF, 25, ItemAttribute.LIFESTEAL, 5), "A sword born from the earth.");
        CustomSword lightningSword = new CustomSword(Material.DIAMOND_SWORD, "Lightning Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(thunderShard), null,
                new CustomItemStack(enchantedFlesh, 6), new CustomItemStack(diamondSword), new CustomItemStack(enchantedFlesh, 6),
                null, new CustomItemStack(enchantedStick, 8), null
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(10, 35), List.of(25, 50), Map.of(ItemAttribute.THUNDERDEF, 25, ItemAttribute.LIFESTEAL, 5), "A sword born from the storms.");
        CustomSword zephyrSword = new CustomSword(Material.DIAMOND_SWORD, "Zephyr Sword", Rarity.UNCOMMON, new CustomItemStack[]{
                null, new CustomItemStack(airShard), null,
                new CustomItemStack(enchantedFlesh, 6), new CustomItemStack(diamondSword), new CustomItemStack(enchantedFlesh, 6),
                null, new CustomItemStack(enchantedStick, 8), null
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(10, 35), List.of(25, 50), Map.of(ItemAttribute.AIRDEF, 25, ItemAttribute.LIFESTEAL, 5), "A sword born from the winds.");

        CustomSword ablaze = new CustomSword(Material.DIAMOND_SWORD, "Ablaze", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(fireCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(flamingSword), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(25, 75), List.of(50, 100), Map.of(ItemAttribute.FIREDEF, 50, ItemAttribute.FIREPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");
        CustomSword whirlpool = new CustomSword(Material.DIAMOND_SWORD, "Whirlpool", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(waterCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(aqueousSword), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(25, 75), List.of(50, 100), Map.of(ItemAttribute.WATERDEF, 50, ItemAttribute.WATERPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");
        CustomSword quake = new CustomSword(Material.DIAMOND_SWORD, "Quake", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(earthCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(terrestrialSword), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(25, 75), List.of(50, 100), Map.of(ItemAttribute.EARTHDEF, 50, ItemAttribute.EARTHPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");
        CustomSword fusillade = new CustomSword(Material.DIAMOND_SWORD, "Fusillade", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(thunderCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(lightningSword), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(25, 50), List.of(50, 125), Map.of(ItemAttribute.THUNDERDEF, 50, ItemAttribute.THUNDERPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");
        CustomSword tempest = new CustomSword(Material.DIAMOND_SWORD, "Tempest", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(airCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(zephyrSword), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(25, 40), List.of(50, 60), Map.of(ItemAttribute.AIRDEF, 50, ItemAttribute.AIRPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");
        CustomSword anarchy = new CustomSword(Material.DIAMOND_SWORD, "Anarchy", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(chaosCore), null,
                new CustomItemStack(enchantedFlesh, 32), new CustomItemStack(enchantedAmethyst, 16), new CustomItemStack(enchantedFlesh, 32),
                null, new CustomItemStack(enchantedStick, 64), null
        }, List.of(Element.NEUTRAL, Element.CHAOS), List.of(25, 40), List.of(50, 50), Map.of(ItemAttribute.FIREDEF, 50, ItemAttribute.FIREPERCENT, 5, ItemAttribute.LIFESTEAL, 10), "");

        CustomSword surtr = new CustomSword(Material.DIAMOND_SWORD, "Surtr", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(fireCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(ablaze), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(75, 250), List.of(150, 375), Map.of(ItemAttribute.FIREDEF, 100, ItemAttribute.FIREPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword boreas = new CustomSword(Material.DIAMOND_SWORD, "Boreas", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(waterCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(whirlpool), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(75, 200), List.of(150, 500), Map.of(ItemAttribute.WATERDEF, 100, ItemAttribute.WATERPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword volos = new CustomSword(Material.DIAMOND_SWORD, "Volos", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(earthCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(quake), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(75, 300), List.of(150, 350), Map.of(ItemAttribute.EARTHDEF, 100, ItemAttribute.EARTHPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword mjolnir = new CustomSword(Material.DIAMOND_SWORD, "Mjolnir", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(thunderCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(fusillade), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(75, 50), List.of(150, 750), Map.of(ItemAttribute.THUNDERDEF, 100, ItemAttribute.THUNDERPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword anemoi = new CustomSword(Material.DIAMOND_SWORD, "Anemoi", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(airCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(tempest), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(75, 250), List.of(150, 300), Map.of(ItemAttribute.AIRDEF, 100, ItemAttribute.AIRPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword jormungandr = new CustomSword(Material.DIAMOND_SWORD, "Jormungandr", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(chaosCore, 64), null,
                new CustomItemStack(compactFlesh, 32), new CustomItemStack(anarchy), new CustomItemStack(compactFlesh, 32),
                null, new CustomItemStack(compactStick, 64), null
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(75, 200), List.of(150, 300), Map.of(ItemAttribute.CHAOSDEF, 100, ItemAttribute.CHAOSPERCENT, 10, ItemAttribute.LIFESTEAL, 25), "");
        CustomSword bhūtamaya = new CustomSword(Material.DIAMOND_SWORD, "Bhūtamaya", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(elementalCore), new CustomItemStack(surtr), new CustomItemStack(elementalCore),
                new CustomItemStack(boreas), new CustomItemStack(jormungandr), new CustomItemStack(volos),
                new CustomItemStack(mjolnir), new CustomItemStack(elementalCore), new CustomItemStack(anemoi)
        }, true, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(125, 100, 100, 25, 150, 125, 150), List.of(250, 250, 150, 375, 175, 250, 200), Map.of(ItemAttribute.ALLDEF, 75, ItemAttribute.ALLPERCENT, 7, ItemAttribute.LIFESTEAL, 30), "");

        CustomSword zenith = new CustomSword(Material.NETHERITE_SWORD, "Zenith", Rarity.DUALITY, null, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(50000, 50000, 50000, 50000, 50000, 50000, 50000), List.of(100000, 100000, 100000, 100000, 100000, 100000, 100000), Map.of(ItemAttribute.ALLDEF, 9999, ItemAttribute.ALLPERCENT, 9999), "Life.");

        // shortbows
        CustomShortbow shortbow = new CustomShortbow("Shortbow", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1),
                new CustomItemStack(enchantedStick, 1), new CustomItemStack(bow, 1), new CustomItemStack(enchantedString, 1),
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1)
        }, 1, List.of(Element.NEUTRAL), List.of(75), List.of(150), Map.of(), "");
        CustomShortbow elementalShortbow = new CustomShortbow("Elemental Shortbow", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(shortbow, 1), new CustomItemStack(elementalCore, 4),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, 3, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.NEUTRAL), List.of(100, 100, 100, 50), List.of(200, 200, 200, 100),
                Map.of(ItemAttribute.FIREPERCENT, 10, ItemAttribute.WATERPERCENT, 10, ItemAttribute.EARTHPERCENT, 10, ItemAttribute.THUNDERPERCENT, 10, ItemAttribute.AIRPERCENT, 10, ItemAttribute.CHAOSPERCENT, 10), "");
        CustomShortbow chaoticShortbow = new CustomShortbow("Chaotic Shortbow", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(elementalShortbow), new CustomItemStack(chaoticCore, 1),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, 3, List.of(Element.CHAOS), List.of(999), List.of(9999), Map.of(ItemAttribute.CHAOSPERCENT, 50), "");
        CustomShortbow apollo = new CustomShortbow("Apollo", Rarity.GODLIKE, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32),
                new CustomItemStack(elementalNexus), new CustomItemStack(chaoticShortbow), new CustomItemStack(godlyEssence, 1),
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32)
        }, 5, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.CHAOS, Element.NEUTRAL), List.of(1000, 750, 1000, 5000, 5000), List.of(1500, 1250, 1500, 5000, 10000),
                Map.of(ItemAttribute.FIREPERCENT, 25, ItemAttribute.WATERPERCENT, 25, ItemAttribute.EARTHPERCENT, 25, ItemAttribute.THUNDERPERCENT, 25, ItemAttribute.AIRPERCENT, 25, ItemAttribute.CHAOSPERCENT, 25), "");
        Eidolon apothesosis = new Eidolon("Eidolon", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb),
                new CustomItemStack(demonicStick), new CustomItemStack(apollo), new CustomItemStack(activeDemonicEssence),
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb)
        }, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.THUNDER, Element.AIR, Element.CHAOS, Element.NEUTRAL), List.of(9999, 9999, 9999, 9999, 9999, 9999, 49999), List.of(19999, 19999, 19999, 19999, 19999, 9999, 99999),
                Map.of(ItemAttribute.FIREPERCENT, 100, ItemAttribute.WATERPERCENT, 100, ItemAttribute.EARTHPERCENT, 100, ItemAttribute.THUNDERPERCENT, 100, ItemAttribute.AIRPERCENT, 100, ItemAttribute.CHAOSPERCENT, 100));

        // armor

        CustomArmor catharsis = new CustomArmor(Material.DIAMOND_HELMET, "Catharsis", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 800, ItemAttribute.FIREDEF, 30, ItemAttribute.FIREPERCENT, 60, ItemAttribute.LOOTBONUS, 10), "The atonement of your sins has begun to unleash your true power.");
        CustomArmor conflagration = new CustomArmor(Material.GOLDEN_CHESTPLATE, "Conflagration", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 100, ItemAttribute.FIREDEF, 10, ItemAttribute.LOOTBONUS, 30), "Your apathetic destruction of anything and everything attracted this item to you.");
        CustomArmor combustion = new CustomArmor(Material.IRON_LEGGINGS, "Combustion", Rarity.RARE, Map.of(ItemAttribute.HEALTH, 200, ItemAttribute.FIREDEF, 15, ItemAttribute.FIREPERCENT, 5, ItemAttribute.LOOTBONUS, 20), "The continuation of your annihilation has sparked a fire within you.");
        CustomArmor igneous = new CustomArmor(Material.IRON_BOOTS, "Igneous", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 500, ItemAttribute.FIREDEF, 50, ItemAttribute.LOOTBONUS, 5), "Your wake of ruin has ended but a deep desire to wreak havoc has solidified in your soul.");

        CustomArmor alluvion = new CustomArmor(Material.GOLDEN_HELMET, "Alluvion", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 50, ItemAttribute.WATERDEF, 3, ItemAttribute.WATERPERCENT, 3, ItemAttribute.LOOTBONUS, 10), "Harnessing the power of water has created an alluvion at your disposal.");
        CustomArmor deluge = new CustomArmor(Material.IRON_CHESTPLATE, "Deluge", Rarity.RARE, Map.of(ItemAttribute.HEALTH, 150, ItemAttribute.WATERPERCENT, 10, ItemAttribute.LOOTBONUS, 5), "The overwhelming forces of water created a rift allowing this item to exist.");
        CustomArmor torrent = new CustomArmor(Material.IRON_LEGGINGS, "Torrent", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 300, ItemAttribute.WATERDEF, 20, ItemAttribute.WATERPERCENT, 20, ItemAttribute.LOOTBONUS, 20), "Your torrent across the elements will lay waste to all that cross you.");
        CustomArmor inundation = new CustomArmor(Material.DIAMOND_BOOTS, "Inundation", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.WATERDEF, 50, ItemAttribute.WATERPERCENT, 30, ItemAttribute.LOOTBONUS, 5), "The most powerful discharge of water you can muster.");

        CustomArmor earth = new CustomArmor(Material.IRON_HELMET, "Earth", Rarity.RARE, Map.of(ItemAttribute.HEALTH, 250, ItemAttribute.EARTHDEF, 50, ItemAttribute.LOOTBONUS, 5), "");
        CustomArmor natural = new CustomArmor(Material.DIAMOND_CHESTPLATE, "Natural", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1500, ItemAttribute.EARTHDEF, 150, ItemAttribute.LOOTBONUS, 15), "");
        CustomArmor realism = new CustomArmor(Material.GOLDEN_LEGGINGS, "Realism", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 150, ItemAttribute.EARTHDEF, 30, ItemAttribute.LOOTBONUS, 5), "");
        CustomArmor congenital = new CustomArmor(Material.IRON_BOOTS, "Congenital", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 500, ItemAttribute.EARTHDEF, 100, ItemAttribute.EARTHPERCENT, 5, ItemAttribute.LOOTBONUS, 10), "");

        CustomArmor galvanic = new CustomArmor(Material.IRON_HELMET, "Galvanic", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 250, ItemAttribute.THUNDERDEF, 10, ItemAttribute.THUNDERPERCENT, 30, ItemAttribute.LOOTBONUS, 10), "");
        CustomArmor stimulation = new CustomArmor(Material.IRON_CHESTPLATE, "Stimulation", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 100, ItemAttribute.THUNDERPERCENT, 50, ItemAttribute.LOOTBONUS, 20), "");
        CustomArmor dynamo = new CustomArmor(Material.DIAMOND_LEGGINGS, "Dynamo", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 700, ItemAttribute.THUNDERDEF, 30, ItemAttribute.THUNDERPERCENT, 80, ItemAttribute.LOOTBONUS, 40), "");
        CustomArmor generator = new CustomArmor(Material.GOLDEN_LEGGINGS, "Generator", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 20, ItemAttribute.THUNDERDEF, 10, ItemAttribute.THUNDERPERCENT, 10, ItemAttribute.LOOTBONUS, 10), "");

        CustomArmor elevation = new CustomArmor(Material.IRON_HELMET, "Elevation", Rarity.RARE, Map.of(ItemAttribute.HEALTH, 100, ItemAttribute.AIRDEF, 10, ItemAttribute.AIRPERCENT, 25, ItemAttribute.LOOTBONUS, 5), "");
        CustomArmor ascension = new CustomArmor(Material.DIAMOND_CHESTPLATE, "Ascension", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 500, ItemAttribute.AIRDEF, 50, ItemAttribute.AIRPERCENT, 85, ItemAttribute.LOOTBONUS, 15), "");
        CustomArmor uplifted = new CustomArmor(Material.IRON_LEGGINGS, "Uplifted", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 150, ItemAttribute.AIRDEF, 20, ItemAttribute.AIRPERCENT, 35, ItemAttribute.LOOTBONUS, 10), "");
        CustomArmor suspension = new CustomArmor(Material.GOLDEN_BOOTS, "Suspension", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 10, ItemAttribute.LOOTBONUS, 30), "");

        CustomArmor oblivion = new CustomArmor(Material.DIAMOND_HELMET, "Oblivion", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.CHAOSDEF, 100, ItemAttribute.CHAOSPERCENT, 50, ItemAttribute.LOOTBONUS, 10), "");
        CustomArmor confusion = new CustomArmor(Material.GOLDEN_CHESTPLATE, "Confusion", Rarity.UNCOMMON, Map.of(ItemAttribute.HEALTH, 30, ItemAttribute.CHAOSDEF, 5, ItemAttribute.CHAOSPERCENT, 15, ItemAttribute.LOOTBONUS, 10), "");
        CustomArmor havoc = new CustomArmor(Material.IRON_LEGGINGS, "Havoc", Rarity.EPIC, Map.of(ItemAttribute.HEALTH, 130, ItemAttribute.CHAOSDEF, 25, ItemAttribute.CHAOSPERCENT, 30, ItemAttribute.LOOTBONUS, 10), "");
        CustomArmor shambles = new CustomArmor(Material.IRON_BOOTS, "Shambles", Rarity.RARE, Map.of(ItemAttribute.HEALTH, 60, ItemAttribute.CHAOSDEF, 10, ItemAttribute.CHAOSPERCENT, 20, ItemAttribute.LOOTBONUS, 15), "");

        CustomArmor supernal = new CustomArmor(Material.DIAMOND_HELMET, "Supernal", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1, ItemAttribute.ALLDEF, 300, ItemAttribute.ALLPERCENT, 70, ItemAttribute.LOOTBONUS, 50), "");
        CustomArmor marvel = new CustomArmor(Material.DIAMOND_CHESTPLATE, "Marvel", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.ALLDEF, 200, ItemAttribute.ALLPERCENT, 10, ItemAttribute.LOOTBONUS, 20), "");
        CustomArmor prime = new CustomArmor(Material.DIAMOND_LEGGINGS, "Prime", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 750, ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 20, ItemAttribute.LOOTBONUS, 35), "");
        CustomArmor sensation = new CustomArmor(Material.DIAMOND_BOOTS, "Sensation", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 500, ItemAttribute.ALLDEF, 50, ItemAttribute.ALLPERCENT, 10, ItemAttribute.LOOTBONUS, 10, ItemAttribute.ABILITYREGEN, 3), "");

        CustomArmor afire = new CustomArmor(Material.DIAMOND_LEGGINGS, "Afire", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1500, ItemAttribute.FIREDEF, 500, ItemAttribute.LOOTBONUS, 20), "");
        CustomArmor devastation = new CustomArmor(Material.DIAMOND_HELMET, "Devastation", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.FIREDEF, 200, ItemAttribute.CHAOSDEF, 200, ItemAttribute.FIREPERCENT, 50, ItemAttribute.CHAOSPERCENT, 50), "");
        CustomArmor virulence = new CustomArmor(Material.DIAMOND_CHESTPLATE, "Virulence", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.FIREPERCENT, 100, ItemAttribute.CHAOSPERCENT, 100, ItemAttribute.THUNDERPERCENT, 100, ItemAttribute.LOOTBONUS, 50), "");

        CustomArmor compassion = new CustomArmor(Material.DIAMOND_BOOTS, "Compassion", Rarity.LEGENDARY, Map.of(ItemAttribute.HEALTH, 2000, ItemAttribute.FIREDEF, 750, ItemAttribute.WATERDEF, 750, ItemAttribute.EARTHDEF, 750, ItemAttribute.AIRDEF, 750, ItemAttribute.ABILITYREGEN, 5), "");

        CustomArmor godHelmet = new CustomArmor(Material.DIAMOND_HELMET, "God Helmet", Rarity.GODLIKE, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.ALLDEF, 1000, ItemAttribute.ABILITYREGEN, 2), "The helmet of the gods.");
        CustomArmor godChestplate = new CustomArmor(Material.DIAMOND_CHESTPLATE, "God Chestplate", Rarity.GODLIKE, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.ALLDEF, 1000, ItemAttribute.ABILITYREGEN, 2), "The chestplate of the gods.");
        CustomArmor godLeggings = new CustomArmor(Material.DIAMOND_LEGGINGS, "God Leggings", Rarity.GODLIKE, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.ALLDEF, 1000, ItemAttribute.ABILITYREGEN, 2), "The leggings of the gods.");
        CustomArmor godBoots = new CustomArmor(Material.DIAMOND_BOOTS, "God Boots", Rarity.GODLIKE, Map.of(ItemAttribute.HEALTH, 1000, ItemAttribute.ALLDEF, 1000, ItemAttribute.ABILITYREGEN, 2, ItemAttribute.ALLPERCENT, 1000), "The boots of the gods.");

        // loot tables
        // name is l + level + entity name + Loot
        CustomLootTable l1SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(1, 3), Rarity.UNCOMMON, 1);
        CustomLootTable l10SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(4, 7), Rarity.RARE, 1);
        CustomLootTable l25SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(45, 57), Rarity.EPIC, 1);
        CustomLootTable l50SpiderLoot = new CustomLootTable(List.of(enchantedString), List.of(100D), List.of(1, 3), Rarity.LEGENDARY, 1);
        CustomLootTable l100SpiderLoot = new CustomLootTable(List.of(enchantedWeb), List.of(100D), List.of(1, 3), Rarity.GODLIKE, 1);

        CustomLootTable l75TheWitherLoot = new CustomLootTable(List.of(enchantedMagmaCream, enchantedBlazeRod, fireCore, fireEssence, afire, devastation, virulence), List.of(100D, 100D, 100D, 1D, 5D, 5D, 5D), List.of(32, 37, 32, 37, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1), Rarity.GODLIKE, 10);

        CustomLootTable l5FlamingZombieLoot = new CustomLootTable(List.of(rottenFlesh, iron), List.of(100D, 5D), List.of(4, 6, 1, 1), Rarity.UNCOMMON, 1);
        CustomLootTable l5MagmaCubeLoot = new CustomLootTable(List.of(magmaCream), List.of(100D), List.of(1, 3), Rarity.UNCOMMON, 1);
        CustomLootTable l10BlazeLoot = new CustomLootTable(List.of(blazeRod), List.of(100D), List.of(0, 2), Rarity.RARE, 1);
        CustomLootTable l10ZombifiedPiglinLoot = new CustomLootTable(List.of(rottenFlesh, gold), List.of(100D, 50D), List.of(5, 7, 1, 2), Rarity.RARE, 1);
        CustomLootTable l10PiglinLoot = new CustomLootTable(List.of(rottenFlesh, gold), List.of(100D, 50D), List.of(5, 7, 2, 3), Rarity.RARE, 1);
        CustomLootTable l10WitherSkeletonLoot = new CustomLootTable(List.of(bone, coal, witherRose), List.of(100D, 50D, 3D), List.of(5, 7, 1, 2, 1, 1), Rarity.RARE, 1.5);
        CustomLootTable l10HoglinLoot = new CustomLootTable(List.of(porkchop, leather), List.of(100D, 50D), List.of(4, 6, 1, 2), Rarity.RARE, 1);
        CustomLootTable l15GhastLoot = new CustomLootTable(List.of(ghastTear, gunpowder), List.of(100D, 100D), List.of(1, 2, 2, 3), Rarity.RARE, 2);
        CustomLootTable l20FlamingGolemLoot = new CustomLootTable(List.of(inactiveFireCore, fireShard, enchantedMagmaCream, enchantedBlazeRod, flamingPumpkin, fireCore), List.of(100D, 100D, 100D, 100D, 100D, 5D), List.of(1, 1, 4, 6, 1, 3, 1, 3, 1, 2, 1 ,1), Rarity.EPIC, 5);
        CustomLootTable l35WitherMinionLoot = new CustomLootTable(List.of(inactiveFireCore, fireShard, enchantedMagmaCream, enchantedBlazeRod, fireCore), List.of(100D, 100D, 100D, 100D, 1D), List.of(1, 1, 4, 6, 1, 3, 1, 3, 1 ,1), Rarity.RARE, 2);
        CustomLootTable l35LeadMinionLoot = new CustomLootTable(List.of(inactiveFireCore, fireShard, enchantedMagmaCream, enchantedBlazeRod, fireCore), List.of(100D, 100D, 100D, 100D, 10D), List.of(2, 3, 6, 8, 2, 5, 2, 5, 1, 1), Rarity.EPIC, 10);

        CustomLootTable l5FireLoot = new CustomLootTable(List.of(fireShard), List.of(5D), List.of(1, 1), Rarity.COMMON, 0);
        CustomLootTable l10FireLoot = new CustomLootTable(List.of(fireShard), List.of(5D), List.of(1, 2), Rarity.COMMON, 0);
        CustomLootTable l15FireLoot = new CustomLootTable(List.of(fireShard), List.of(10D), List.of(1, 1), Rarity.COMMON, 0);

        // mobs
        // constructor: name, type, elements, damage, level, defense, maxHealth
        CustomBaseMob spider1 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 10, 1, 100, 100, List.of(l1SpiderLoot), true){};
        CustomBaseMob spider10 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 100, 10, 200, 1000, List.of(l10SpiderLoot), true){};
        CustomBaseMob spider25 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 250, 25, 500, 5000, List.of(l25SpiderLoot), true){};
        CustomBaseMob spider50 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 500, 50, 750, 10000, List.of(l50SpiderLoot), true){};
        CustomBaseMob spider100 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 1000, 100, 1000, 50000, List.of(l100SpiderLoot), true){};
        CustomBaseMob spider0 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 10000, 0, 0, 100000, null, true){};
        CustomBaseMob wither100 = new CustomBaseMob("Wither", EntityType.WITHER, List.of(Element.FIRE), 100, 100, 50000, 1000000000, null, true){};
        CustomBaseMob witherBoss = new CustomBaseMob("The Wither", BossType.WITHER, List.of(Element.FIRE, Element.CHAOS), 1000, 75, 1000, 1000000, List.of(l75TheWitherLoot), true){};
        CustomBaseMob elemental1 = new CustomBaseMob("Elemental", EntityType.ZOMBIE, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.THUNDER, Element.AIR, Element.CHAOS), 50, 1, 0, 100, null, true){};
        CustomBaseMob nadirTester = new CustomBaseMob("Nadir Tester", EntityType.WITHER, List.of(Element.FIRE), 100, 100, 1000000, 2147483647, null, true){};
        CustomBaseMob testDummy0 = new CustomBaseMob("Test Dummy", EntityType.WITHER, List.of(), 1000, 0, 0, 1000000000, null, true){};

        // Fire Mobs
        CustomBaseMob flamingZombie = new CustomBaseMob("Flaming Zombie", EntityType.ZOMBIE, List.of(Element.FIRE), 25, 5, 50, 250, List.of(l5FlamingZombieLoot, l5FireLoot), true){};
        CustomBaseMob magmaCube = new CustomBaseMob("Magma Cube", EntityType.MAGMA_CUBE, List.of(Element.FIRE), 25, 5, 50, 250, List.of(l5MagmaCubeLoot, l5FireLoot), true){};
        CustomBaseMob blaze = new CustomBaseMob("Blaze", EntityType.BLAZE, List.of(Element.FIRE), 40, 10, 100, 500, List.of(l10BlazeLoot, l10FireLoot), true){};
        CustomBaseMob zombifiedPiglin = new CustomBaseMob("Zombified Piglin", EntityType.ZOMBIFIED_PIGLIN, List.of(Element.FIRE), 40, 10, 100, 500, List.of(l10ZombifiedPiglinLoot, l10FireLoot), true){};
        CustomBaseMob piglin = new CustomBaseMob("Piglin", EntityType.ZOMBIFIED_PIGLIN, List.of(Element.FIRE), 50, 10, 100, 750, List.of(l10PiglinLoot, l10FireLoot), true){};
        CustomBaseMob witherSkeleton = new CustomBaseMob("Wither Skeleton", EntityType.WITHER_SKELETON, List.of(Element.FIRE), 50, 10, 100, 750, List.of(l10WitherSkeletonLoot, l10FireLoot), true){};
        CustomBaseMob hoglin = new CustomBaseMob("Hoglin", EntityType.HOGLIN, List.of(Element.FIRE), 50, 10, 100, 750, List.of(l10HoglinLoot, l10FireLoot), true){};
        CustomBaseMob ghast = new CustomBaseMob("Ghast", EntityType.GHAST, List.of(Element.FIRE), 40, 15, 100, 1500, List.of(l15GhastLoot, l15FireLoot), true){};
        CustomBaseMob flamingGolem = new CustomBaseMob("Flaming Golem", EntityType.IRON_GOLEM, List.of(Element.FIRE), 200, 20, 500, 10000, null, true){};
        CustomBaseMob leadMinion = new CustomBaseMob("The Wither's Lead Minion", BossType.LEADMINION, List.of(Element.FIRE, Element.CHAOS), 200, 35, 500, 75000, null, true){};
        CustomBaseMob witherMinion = new CustomBaseMob("The Wither's Minion", BossType.WITHERMINION, List.of(Element.FIRE, Element.CHAOS), 200, 35, 500, 50000, null, true){};

        CustomBaseMob spellTester = new CustomBaseMob("Spell Tester", BossType.SPELLTESTER, List.of(Element.FIRE, Element.CHAOS), 200, 100, 500, 50000, null, true){};

        for (CustomItem item : CustomItem.CUSTOM_ITEMS.values()) {
            item.createRecipe();
        }

        // boss summon item initialization
        for (BossType boss : BossType.values()) {
            switch (boss) {
                case WITHER:
                    boss.setItemsToSummon(List.of(new CustomItemStack(soulSand, 4), new CustomItemStack(witherRose, 3)));
                    break;
            }
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
