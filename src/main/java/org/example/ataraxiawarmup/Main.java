package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.ataraxiawarmup.item.customitem.*;
import org.example.ataraxiawarmup.item.customitem.ability.Ability;
import org.example.ataraxiawarmup.item.customitem.ability.AbilityApplyingInventory;
import org.example.ataraxiawarmup.item.customitem.ability.AbilityApplyingInventoryListener;
import org.example.ataraxiawarmup.item.customitem.ability.CustomArtifact;
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
        new AbilityApplyingInventoryListener(this);

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
        }, true);
        CustomIngredient waterCore = new CustomIngredient(Material.HEART_OF_THE_SEA, "Water Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(waterShard, 16), null,
                new CustomItemStack(waterShard, 16), new CustomItemStack(inactiveWaterCore, 1), new CustomItemStack(waterShard, 16),
                null, new CustomItemStack(waterShard, 16), null
        }, true);
        CustomIngredient earthCore = new CustomIngredient(Material.OAK_SAPLING, "Earth Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(earthShard, 16), null,
                new CustomItemStack(earthShard, 16), new CustomItemStack(inactiveEarthCore, 1), new CustomItemStack(earthShard, 16),
                null, new CustomItemStack(earthShard, 16), null
        }, true);
        CustomIngredient thunderCore = new CustomIngredient(Material.SUNFLOWER, "Thunder Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(thunderShard, 16), null,
                new CustomItemStack(thunderShard, 16), new CustomItemStack(inactiveThunderCore, 1), new CustomItemStack(thunderShard, 16),
                null, new CustomItemStack(thunderShard, 16), null
        }, true);
        CustomIngredient airCore = new CustomIngredient(Material.PHANTOM_MEMBRANE, "Air Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(airShard, 16), null,
                new CustomItemStack(airShard, 16), new CustomItemStack(inactiveAirCore, 1), new CustomItemStack(airShard, 16),
                null, new CustomItemStack(airShard, 16), null
        }, true);
        CustomIngredient chaosCore = new CustomIngredient(Material.ENDER_EYE, "Chaos Core", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(chaosShard, 16), null,
                new CustomItemStack(chaosShard, 16), new CustomItemStack(inactiveChaosCore, 1), new CustomItemStack(chaosShard, 16),
                null, new CustomItemStack(chaosShard, 16), null
        }, true);

        CustomIngredient elementalCore = new CustomIngredient(Material.CYAN_DYE, "Elemental Core", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(fireCore, 64), new CustomItemStack(waterCore, 64), new CustomItemStack(earthCore, 64),
                null, new CustomItemStack(inactiveElementalCore, 1), null,
                new CustomItemStack(thunderCore, 64), new CustomItemStack(airCore, 64), new CustomItemStack(chaosCore, 64)
        }, true);

        CustomIngredient chaoticCore = new CustomIngredient(Material.ENDER_PEARL, "Chaotic Core", Rarity.LEGENDARY, null, true){};
        CustomIngredient godlyEssence = new CustomIngredient(Material.SUNFLOWER, "Godly Essence", Rarity.GODLIKE, null, true){};
        CustomIngredient elementalNexus = new CustomIngredient(Material.MAGMA_CREAM, "Elemental Nexus", Rarity.LEGENDARY, new CustomItemStack[]{
                new CustomItemStack(fireEssence, 1), new CustomItemStack(waterEssence, 1), new CustomItemStack(earthEssence, 1),
                null, new CustomItemStack(elementalCore, 64), null,
                new CustomItemStack(thunderEssence, 1), new CustomItemStack(airEssence, 1), new CustomItemStack(chaosEssence, 1)
        }, true){};
        CustomIngredient leviathanWish = new CustomIngredient(Material.HEART_OF_THE_SEA, "Leviathan's Last Wish", Rarity.GODLIKE, null, true);
        CustomIngredient indraBreath = new CustomIngredient(Material.FIREWORK_STAR, "Indra's Dying Breath", Rarity.GODLIKE, null, true);
        CustomIngredient artemisSoul = new CustomIngredient(Material.BLACK_DYE, "Artemis' Soul", Rarity.GODLIKE, null, true);
        CustomIngredient reinforcedWeb = new CustomIngredient(Material.COBWEB, "Reinforced Web", Rarity.EPIC, new CustomItemStack[]{
            null, new CustomItemStack(enchantedWeb, 32), null,
            new CustomItemStack(enchantedWeb, 32), new CustomItemStack(enchantedIronBlock, 32), new CustomItemStack(enchantedWeb, 32),
            null, new CustomItemStack(enchantedWeb, 32), null
        }, true){};
        CustomIngredient inactiveDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Inactive Demonic Essence", Rarity.TRINITY, null, false){};
        CustomIngredient activeDemonicEssence = new CustomIngredient(Material.BLACK_DYE, "Demonic Essence", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus, 64), null,
                new CustomItemStack(elementalNexus, 64), new CustomItemStack(inactiveDemonicEssence), new CustomItemStack(elementalNexus, 64),
                null, new CustomItemStack(elementalNexus, 64), null
        }, true){};
        CustomIngredient demonCore = new CustomIngredient(Material.ENDER_EYE, "Demon Core", Rarity.TRINITY, null, true){};
        CustomIngredient demonicStick = new CustomIngredient(Material.STICK, "Demon-Infused Stick", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 64), null,
                new CustomItemStack(compactStick, 64), new CustomItemStack(demonCore), new CustomItemStack(compactStick, 64),
                null, new CustomItemStack(compactStick, 64), null
        }, true){};
        CustomIngredient demonicWeb = new CustomIngredient(Material.COBWEB, "Demon-Infused Web", Rarity.TRINITY, new CustomItemStack[]{
               null, new CustomItemStack(reinforcedWeb, 64), null,
               new CustomItemStack(reinforcedWeb, 64), new CustomItemStack(demonCore), new CustomItemStack(reinforcedWeb, 64),
                null, new CustomItemStack(reinforcedWeb, 64), null
        }, true){};

        // artifacts
        CustomArtifact fire1 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier I", Rarity.UNCOMMON, null, Ability.METEOR, 1);
        CustomArtifact fire2 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier II", Rarity.RARE, new CustomItemStack[]{
                new CustomItemStack(fire1), new CustomItemStack(fire1), new CustomItemStack(fire1),
                new CustomItemStack(fire1), new CustomItemStack(fire1), new CustomItemStack(fire1),
                new CustomItemStack(fire1), new CustomItemStack(fire1), new CustomItemStack(fire1)
        }, Ability.METEOR, 2);
        CustomArtifact fire3 = new CustomArtifact(Material.BLAZE_POWDER, "Fire Artifact Tier III", Rarity.EPIC, new CustomItemStack[]{
                new CustomItemStack(fire2), new CustomItemStack(fire2), new CustomItemStack(fire2),
                new CustomItemStack(fire2), new CustomItemStack(fire2), new CustomItemStack(fire2),
                new CustomItemStack(fire2), new CustomItemStack(fire2), new CustomItemStack(fire2)
        }, Ability.METEOR, 3);

        // bows

        CustomBow bow = new CustomBow(Material.BOW, "Bow", Rarity.COMMON, new CustomItemStack[]{
                null, new CustomItemStack(stick, 1), new CustomItemStack(stick, 1),
                new CustomItemStack(stick, 1), null, new CustomItemStack(stick, 1),
                null, new CustomItemStack(stick, 1), new CustomItemStack(stick, 1)
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
        }, List.of(Element.NEUTRAL, Element.FIRE), List.of(20, 150), List.of(50, 200), Map.of(ItemAttribute.FIREDEF, 20, ItemAttribute.FIREPERCENT, 10), "Raging on and on, destroying everything in its wake.");

        CustomBow tsunami = new CustomBow(Material.BOW, "Tsunami", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(aqueousBow, 1), new CustomItemStack(waterCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.WATER), List.of(20, 150), List.of(50, 200), Map.of(ItemAttribute.WATERDEF, 20, ItemAttribute.WATERPERCENT, 10), "A cruel force of aquatic destruction.");

        CustomBow earthquake = new CustomBow(Material.BOW, "Earthquake", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(terrestrialBow, 1), new CustomItemStack(earthCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.EARTH), List.of(20, 150), List.of(50, 200), Map.of(ItemAttribute.EARTHDEF, 20, ItemAttribute.EARTHPERCENT, 10), "Earthly destructive turbulence.");

        CustomBow thunderstorm = new CustomBow(Material.BOW, "Thunderstorm", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(lightningBow, 1), new CustomItemStack(thunderCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.THUNDER), List.of(20, 125), List.of(50, 175), Map.of(ItemAttribute.THUNDERDEF, 20, ItemAttribute.THUNDERPERCENT, 10), "A stormy, electric force of destruction.");

        CustomBow cyclone = new CustomBow(Material.BOW, "Cyclone", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(zephyrBow, 1), new CustomItemStack(airCore, 1),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.AIR), List.of(20, 80), List.of(50, 120), Map.of(ItemAttribute.AIRDEF, 20, ItemAttribute.AIRPERCENT, 10), "An incredibly strong and detructive tempest.");

        CustomBow disarray = new CustomBow(Material.BOW, "Disarray", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(chaosCore, 1), new CustomItemStack(enchantedString, 32),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, List.of(Element.NEUTRAL, Element.CHAOS), List.of(20, 75), List.of(50, 100), Map.of(ItemAttribute.CHAOSDEF, 20, ItemAttribute.CHAOSPERCENT, 10), "A bow born from havoc.");

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
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 250, 300), List.of(500, 500, 300, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 40, ItemAttribute.ALLPERCENT, 20), "The weaver of fate.");

        CustomBow fireImbuedNorn = new CustomBow(Material.BOW, "Fire-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(fireEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 250, 1500), List.of(500, 500, 300, 750, 350, 500, 2000), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.FIREDEF, 50, ItemAttribute.FIREPERCENT, 25), "The weaver of hellish fate.");

        CustomBow waterImbuedNorn = new CustomBow(Material.BOW, "Water-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(waterEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 300, 1250, 300), List.of(500, 500, 300, 750, 350, 2500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.WATERDEF, 50, ItemAttribute.WATERPERCENT, 25), "The weaver of nautical fate.");

        CustomBow earthImbuedNorn = new CustomBow(Material.BOW, "Earth-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(earthEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 50, 1500, 250, 300), List.of(500, 500, 300, 750, 1750, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.EARTHDEF, 50, ItemAttribute.EARTHPERCENT, 25), "The weaver of planetary fate.");

        CustomBow thunderImbuedNorn = new CustomBow(Material.BOW, "Thunder-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(thunderEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 200, 250, 300, 250, 300), List.of(500, 500, 300, 3750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.THUNDERDEF, 50, ItemAttribute.THUNDERPERCENT, 25), "The weaver of voltaic fate.");

        CustomBow airImbuedNorn = new CustomBow(Material.BOW, "Air-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(airEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 200, 1000, 50, 300, 250, 300), List.of(500, 500, 1500, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.AIRDEF, 50, ItemAttribute.AIRPERCENT, 25), "The weaver of heavenly fate.");

        CustomBow chaosImbuedNorn = new CustomBow(Material.BOW, "Chaos-Imbued Norn", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(chaosEssence, 1), null,
                null, new CustomItemStack(norn, 1), null,
                null, null, null
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(250, 1000, 200, 50, 300, 250, 300), List.of(500, 2500, 300, 750, 350, 500, 400), Map.of(ItemAttribute.ALLDEF, 100, ItemAttribute.ALLPERCENT, 40, ItemAttribute.CHAOSDEF, 50, ItemAttribute.CHAOSPERCENT, 25), "The weaver of the apocalypse.");

        CustomBow elementalImbuedNorn = new CustomBow(Material.BOW, "Elemental-Imbued Norn", Rarity.GODLIKE, new CustomItemStack[]{
                new CustomItemStack(elementalNexus, 1), new CustomItemStack(fireImbuedNorn, 1), new CustomItemStack(elementalNexus, 1),
                new CustomItemStack(waterImbuedNorn, 1), new CustomItemStack(earthImbuedNorn, 1), new CustomItemStack(thunderImbuedNorn, 1),
                new CustomItemStack(airImbuedNorn, 1), new CustomItemStack(elementalNexus, 1), new CustomItemStack(chaosImbuedNorn, 1)
        }, List.of(Element.NEUTRAL, Element.CHAOS, Element.AIR, Element.THUNDER, Element.EARTH, Element.WATER, Element.FIRE), List.of(500, 1000, 1000, 250, 1500, 1250, 1500), List.of(1000, 2500, 1500, 3750, 1750, 2500, 2000), Map.of(ItemAttribute.ALLDEF, 150, ItemAttribute.ALLPERCENT, 66), "The weaver of all fate, everywhere.");

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

        // shortbows
        CustomShortbow shortbow = new CustomShortbow("Shortbow", Rarity.RARE, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1),
                new CustomItemStack(enchantedStick, 1), new CustomItemStack(bow, 1), new CustomItemStack(enchantedString, 1),
                null, new CustomItemStack(enchantedStick, 1), new CustomItemStack(enchantedString, 1)
        }, 1, List.of(Element.NEUTRAL), List.of(75), List.of(150), Map.of());
        CustomShortbow elementalShortbow = new CustomShortbow("Elemental Shortbow", Rarity.EPIC, new CustomItemStack[]{
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32),
                new CustomItemStack(enchantedStick, 32), new CustomItemStack(shortbow, 1), new CustomItemStack(elementalCore, 4),
                null, new CustomItemStack(enchantedStick, 32), new CustomItemStack(enchantedString, 32)
        }, 3, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.NEUTRAL), List.of(100, 100, 100, 50), List.of(200, 200, 200, 100),
                Map.of(ItemAttribute.FIREPERCENT, 10, ItemAttribute.WATERPERCENT, 10, ItemAttribute.EARTHPERCENT, 10, ItemAttribute.THUNDERPERCENT, 10, ItemAttribute.AIRPERCENT, 10, ItemAttribute.CHAOSPERCENT, 10));
        CustomShortbow chaoticShortbow = new CustomShortbow("Chaotic Shortbow", Rarity.LEGENDARY, new CustomItemStack[]{
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32),
                new CustomItemStack(compactStick, 32), new CustomItemStack(elementalShortbow), new CustomItemStack(chaoticCore, 1),
                null, new CustomItemStack(compactStick, 32), new CustomItemStack(enchantedWeb, 32)
        }, 3, List.of(Element.CHAOS), List.of(999), List.of(9999), Map.of(ItemAttribute.CHAOSPERCENT, 50));
        CustomShortbow apollo = new CustomShortbow("Apollo", Rarity.GODLIKE, new CustomItemStack[]{
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32),
                new CustomItemStack(elementalNexus), new CustomItemStack(chaoticShortbow), new CustomItemStack(godlyEssence, 1),
                null, new CustomItemStack(elementalNexus), new CustomItemStack(reinforcedWeb, 32)
        }, 5, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.CHAOS, Element.NEUTRAL), List.of(1000, 750, 1000, 5000, 5000), List.of(1500, 1250, 1500, 5000, 10000),
                Map.of(ItemAttribute.FIREPERCENT, 25, ItemAttribute.WATERPERCENT, 25, ItemAttribute.EARTHPERCENT, 25, ItemAttribute.THUNDERPERCENT, 25, ItemAttribute.AIRPERCENT, 25, ItemAttribute.CHAOSPERCENT, 25));
        Eidolon apothesosis = new Eidolon("Eidolon", Rarity.TRINITY, new CustomItemStack[]{
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb),
                new CustomItemStack(demonicStick), new CustomItemStack(apollo), new CustomItemStack(activeDemonicEssence),
                null, new CustomItemStack(demonicStick), new CustomItemStack(demonicWeb)
        }, List.of(Element.FIRE, Element.WATER, Element.EARTH, Element.THUNDER, Element.AIR, Element.CHAOS, Element.NEUTRAL), List.of(9999, 9999, 9999, 9999, 9999, 9999, 49999), List.of(19999, 19999, 19999, 19999, 19999, 9999, 99999),
                Map.of(ItemAttribute.FIREPERCENT, 100, ItemAttribute.WATERPERCENT, 100, ItemAttribute.EARTHPERCENT, 100, ItemAttribute.THUNDERPERCENT, 100, ItemAttribute.AIRPERCENT, 100, ItemAttribute.CHAOSPERCENT, 100));

        // loot tables
        // name is l + level + entity name + Loot
        CustomLootTable l1SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(1, 3));
        CustomLootTable l10SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(4, 7));
        CustomLootTable l25SpiderLoot = new CustomLootTable(List.of(string), List.of(100D), List.of(45, 57));
        CustomLootTable l50SpiderLoot = new CustomLootTable(List.of(enchantedString), List.of(100D), List.of(1, 3));
        CustomLootTable l100SpiderLoot = new CustomLootTable(List.of(enchantedWeb), List.of(100D), List.of(1, 3));

        // mobs
        // constructor: name, type, elements, damage, level, defense, maxHealth
        CustomBaseMob spider1 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 10, 1, 100, 100, l1SpiderLoot, true){};
        CustomBaseMob spider10 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 100, 10, 200, 1000, l10SpiderLoot, true){};
        CustomBaseMob spider25 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 250, 25, 500, 5000, l25SpiderLoot, true){};
        CustomBaseMob spider50 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 500, 50, 750, 10000, l50SpiderLoot, true){};
        CustomBaseMob spider100 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 1000, 100, 1000, 50000, l100SpiderLoot, true){};
        CustomBaseMob spider0 = new CustomBaseMob("Spider", EntityType.SPIDER, List.of(Element.FIRE), 10000, 0, 0, 100000, null, true){};
        CustomBaseMob wither100 = new CustomBaseMob("Wither", EntityType.WITHER, List.of(Element.FIRE), 100, 100, 50000, 1000000000, null, true){};
        CustomBaseMob nadirTester = new CustomBaseMob("Nadir Tester", EntityType.WITHER, List.of(Element.FIRE), 100, 100, 1000000, 2147483647, null, true){};
        CustomBaseMob testDummy0 = new CustomBaseMob("Test Dummy", EntityType.WITHER, List.of(), 1000, 0, 0, 1000000000, null, true){};

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
