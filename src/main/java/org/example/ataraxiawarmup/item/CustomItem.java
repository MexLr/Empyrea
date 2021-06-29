package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.example.ataraxiawarmup.Main;

import java.util.*;

public abstract class CustomItem {

    public static final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();
    public static final Map<String, CustomRecipe> CUSTOM_RECIPES = new HashMap<>();

    private ItemMeta meta = new ItemStack(Material.BARRIER).getItemMeta();

    public static CustomItem itemFromName(String name) {
        return CUSTOM_ITEMS.get(ChatColor.stripColor(name).toLowerCase());
    }

    public static CustomRecipe recipeFromName(String name) {
        return CUSTOM_RECIPES.get(name);
    }

    public CustomItem() {
        this.CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
        this.CUSTOM_RECIPES.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), getRecipe());
    }

    public ItemStack toItemStack() {
        ItemStack item = new ItemStack(getItemDefinition().mat);
        ItemDefinition def = getItemDefinition();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(def.rarity.getColor() + def.name);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(def.rarity.getLore());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(getItemDefinition().mat, amount);
        ItemDefinition def = getItemDefinition();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(def.rarity.getColor() + def.name);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(def.rarity.getLore());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public CustomItemStack toCustomItemStack() {
        return new CustomItemStack(this, 1);
    };

    public CustomItemStack toCustomItemStack(int amount) {
        return new CustomItemStack(this, amount);
    };

    protected abstract ItemDefinition getItemDefinition();

    public abstract CustomRecipe getRecipe();

    public void createRecipe(CustomItemStack[] recipe) {
        if (recipe != null) {
            CustomRecipe customRecipe = new CustomRecipe(recipe, toCustomItemStack());
        }
    }

    public abstract CustomItemType getCustomItemType();

    public abstract void onUseLeft(Player player);

    public abstract void onUseRight(Player player);

    public ItemMeta getItemMeta() {
        ItemDefinition def = getItemDefinition();
        this.meta.setDisplayName(def.rarity.getColor() + def.name);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(def.rarity.getLore());
        this.meta.setLore(lore);
        return this.meta;
    };

    public abstract void initialize();

    protected class ItemDefinition {
        public final String name;
        public final Material mat;
        public final Rarity rarity;

        public ItemDefinition(String name, Material mat, Rarity rarity) {
            this.name = name;
            this.mat = mat;
            this.rarity = rarity;
        }
    }
}

/**
public enum CustomItem {

    AIR("", Material.AIR, new ArrayList<>(), -1, null, true, Rarity.NULL, false),
    STRING("String", Material.STRING, new ArrayList<>(), 1, null, true, Rarity.COMMON, false),
    ENCHANTED_STRING("Enchanted String", Material.STRING, new ArrayList<>(), 2, new CustomItemStack[]{null, new CustomItemStack(STRING, 32), null, new CustomItemStack(STRING, 32), new CustomItemStack(STRING, 32), new CustomItemStack(STRING, 32), null, new CustomItemStack(STRING, 32), null}, true, Rarity.UNCOMMON, true),
    ENCHANTED_COBWEB("Enchanted Web", Material.COBWEB, new ArrayList<>(), 3, new CustomItemStack[]{null, new CustomItemStack(ENCHANTED_STRING, 32), null, new CustomItemStack(ENCHANTED_STRING, 32), new CustomItemStack(ENCHANTED_STRING, 32), new CustomItemStack(ENCHANTED_STRING, 32), null, new CustomItemStack(ENCHANTED_STRING, 32), null}, true, Rarity.RARE, true),
    STICK("Stick", Material.STICK, new ArrayList<>(), 4, null, true, Rarity.COMMON, false),
    COMPACT_STICK("Compact Stick", Material.STICK, new ArrayList<>(), 5, new CustomItemStack[]{null, new CustomItemStack(STICK, 64), null, new CustomItemStack(STICK, 64), new CustomItemStack(STICK, 64),new CustomItemStack(STICK, 64), null, new CustomItemStack(STICK, 64), null}, true, Rarity.RARE, true),
    ROTTEN_FLESH("Rotten Flesh", Material.ROTTEN_FLESH, new ArrayList<>(), 6, null, true, Rarity.COMMON, false),
    ENCHANTED_ROTTEN_FLESH("Enchanted Rotten Flesh", Material.ROTTEN_FLESH, new ArrayList<>(), 7, new CustomItemStack[]{null, new CustomItemStack(ROTTEN_FLESH, 32), null, new CustomItemStack(ROTTEN_FLESH, 32), new CustomItemStack(ROTTEN_FLESH, 32), new CustomItemStack(ROTTEN_FLESH, 32), null, new CustomItemStack(ROTTEN_FLESH, 32), null}, true, Rarity.UNCOMMON, true),
    ENDER_PEARL("Ender Pearl", Material.ENDER_PEARL, new ArrayList<>(), 7, null, true, Rarity.COMMON, false),
    ENCHANTED_ENDER_PEARL("Enchanted Ender Pearl", Material.ENDER_PEARL, new ArrayList<>(), 8, new CustomItemStack[]{null, new CustomItemStack(ENDER_PEARL, 16), null, new CustomItemStack(ENDER_PEARL, 16), new CustomItemStack(ENDER_PEARL, 16), new CustomItemStack(ENDER_PEARL, 16), null, new CustomItemStack(ENDER_PEARL, 16), null}, true, Rarity.UNCOMMON, true),
    CATALYST("Catalyst", Material.PLAYER_HEAD, new ArrayList<>(), 9, new CustomItemStack[]{null, new CustomItemStack(ENCHANTED_ROTTEN_FLESH, 64), null, new CustomItemStack(ENCHANTED_ROTTEN_FLESH, 64), new CustomItemStack(ENCHANTED_ENDER_PEARL, 16), new CustomItemStack(ENCHANTED_ROTTEN_FLESH, 64), null, new CustomItemStack(ENCHANTED_ROTTEN_FLESH, 64), null}, true, Rarity.RARE, "fe51e0c8-dba0-4540-a21d-4c0b19ef6c19"),
    SHORTBOW("Shortbow", Material.BOW, new ArrayList<>(), 10, new CustomItemStack[]{null, new CustomItemStack(COMPACT_STICK, 64), new CustomItemStack(ENCHANTED_COBWEB, 64), new CustomItemStack(COMPACT_STICK, 64), new CustomItemStack(CATALYST, 1), new CustomItemStack(ENCHANTED_COBWEB, 64), null, new CustomItemStack(COMPACT_STICK, 64), new CustomItemStack(ENCHANTED_COBWEB, 64)}, true, Rarity.EPIC, false);

    private String name;
    private Material mat;
    private List<String> lore;
    private short id;
    private ItemMeta meta;
    private SkullMeta skullMeta;
    private CustomItemStack[] recipe;
    private boolean shaped; // if the recipe is shaped or not
    private Rarity rarity;
    private boolean enchanted = false;
    private UUID skullOwner;
    private boolean isHead = false;

    private static final Map<String, CustomItem> NAME_MAP = new HashMap<String, CustomItem>();
    private static final Map<Short, CustomItem> ID_MAP = new HashMap<Short, CustomItem>();

    static {
        for (CustomItem type : CustomItem.values()) {
            if (type.name != null) {
                NAME_MAP.put(ChatColor.stripColor(addUnderscores(type.name).toLowerCase()), type);
            }
            if (type.id > 0) {
                ID_MAP.put(type.id, type);
            }
        }
    }

    CustomItem(String name, Material mat, List<String> lore, int id, CustomItemStack[] recipe, boolean shaped, Rarity rarity, boolean enchanted) {
        this.name = rarity.getColor() + name;
        this.mat = mat;
        this.lore = lore;

        if (rarity == Rarity.LOVECRAFTIAN) {
            this.lore.add("§l" + ChatColor.RED + "LOVE" + ChatColor.AQUA + "CRAFTIAN");
        } else {
            this.lore.add("§l" + rarity.getColor() + rarity.getName().toUpperCase());
        }

        this.id = (short) id;
        this.recipe = recipe;
        this.meta = (new ItemStack(Material.BARRIER)).getItemMeta();
        this.meta.setDisplayName(this.name);
        this.meta.setLore(this.lore);
        this.shaped = shaped;
        this.rarity = rarity;
        this.enchanted = enchanted;
        if (this.enchanted == true) {
            this.meta.addEnchant(Enchantment.DURABILITY, 1, true);
            this.meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
    }

    CustomItem(String name, Material mat, List<String> lore, int id, CustomItemStack[] recipe, boolean shaped, Rarity rarity, String skullOwnerUUID) {
        this.name = rarity.getColor() + name;
        this.mat = mat;
        this.lore = lore;

        if (rarity == Rarity.LOVECRAFTIAN) {
            this.lore.add("§l" + ChatColor.RED + "LOVE" + ChatColor.AQUA + "CRAFTIAN");
        } else {
            this.lore.add("§l" + rarity.getColor() + rarity.getName().toUpperCase());
        }

        this.id = (short) id;
        this.recipe = recipe;
        this.skullMeta = (SkullMeta) (new ItemStack(Material.PLAYER_HEAD)).getItemMeta();
        this.skullMeta.setDisplayName(this.name);
        this.skullMeta.setLore(this.lore);
        this.shaped = shaped;
        this.rarity = rarity;
        this.enchanted = enchanted;
        this.skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(skullOwnerUUID)));
        this.isHead = true;
    }

    public static CustomItem fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static CustomItem fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.mat;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public int getId() {
        return this.id;
    }

    public CustomItemStack[] getRecipe() {
        return this.recipe;
    }

    public ItemMeta getItemMeta() {
        return this.meta;
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.mat);
        if (this.skullOwner != null) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(skullOwner));
            meta.setDisplayName(this.name);
            meta.setLore(this.lore);
            item.setItemMeta(meta);
        } else {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(this.name);
            meta.setLore(this.lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public CustomItemStack toCustomItemStack() {
        if (this.isHead) {
            return new CustomItemStack(this, this.skullMeta);
        }
        return new CustomItemStack(this, this.meta);
    }

    public CustomItemStack toCustomItemStack(int amount) {
        return new CustomItemStack(this, this.meta, amount);
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public static String addUnderscores(String input) {
        return input.replaceAll(" ", "_");
    }

}
*/