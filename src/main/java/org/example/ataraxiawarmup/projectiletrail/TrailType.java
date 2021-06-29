package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum TrailType {

    /**
     *
     * CHANGE ALL TRAIL TYPES TO ONE ENUM YOU BIG WEIRDO
     *
     */

    DEFAULT("Default", -1, false, ChatColor.MAGIC, Material.BARRIER, Color.fromRGB(255, 255, 255), Color.fromRGB(255, 255, 255)),
    AQUATIC("Aquatic", 1, true, ChatColor.DARK_AQUA, Material.HEART_OF_THE_SEA, Color.fromRGB(0, 31, 31), Color.fromRGB(0, 127, 127)),
    INCENDIARY("Incendiary", 2, true, ChatColor.RED, Material.FLINT_AND_STEEL, Color.fromRGB(63, 31, 0), Color.fromRGB(255, 127, 0)),
    NATURAL("Natural", 3, true, ChatColor.DARK_GREEN, Material.OAK_SAPLING, Color.fromRGB(0, 31, 0), Color.fromRGB(0, 127, 0)),
    AERO("Aero", 4, true, ChatColor.WHITE, Material.WHITE_CONCRETE, Color.fromRGB(255, 255, 255), Color.fromRGB(127, 127, 127)),
    ENDER("Ender", 5, true, ChatColor.DARK_PURPLE, Material.DRAGON_EGG, Color.fromRGB(31, 0, 31), Color.fromRGB(127, 0, 127)),
    NETHER("Nether", 6, true, ChatColor.DARK_RED, Material.NETHERRACK, Color.fromRGB(31, 0, 0), Color.fromRGB(127, 0, 0)),
    REDAQUA("RedAqua", 7, true, ChatColor.UNDERLINE, Material.RED_CONCRETE, Color.fromRGB(127, 0, 0), Color.fromRGB(0, 127, 127)),
    ORANGEPURPLE("OrangePurple", 8, true, ChatColor.UNDERLINE, Material.ORANGE_CONCRETE, Color.fromRGB(127, 63, 0), Color.fromRGB(63, 0, 127)),
    BASIC("Basic", 9, false, ChatColor.GRAY, Material.GRAY_CONCRETE, Color.fromRGB(255, 255, 255), Color.fromRGB(0, 0, 0)),
    END("End", 10, false, ChatColor.DARK_PURPLE, Material.ENDER_PEARL, Color.fromRGB(127, 0, 127), Color.fromRGB(31, 0, 31)),
    NETHERBASIC("Basic Nether", 11, false, ChatColor.DARK_RED, Material.NETHER_BRICK, Color.fromRGB(127, 0, 0), Color.fromRGB(31, 0, 0)),
    OVERWORLD("Overworld", 12, false, ChatColor.GREEN, Material.VILLAGER_SPAWN_EGG, Color.fromRGB(0, 127, 0), Color.fromRGB(0, 31, 0)),
    SUMMER("Summer", 13, true, ChatColor.YELLOW, Material.SUNFLOWER, Color.fromRGB(255, 255, 0), Color.fromRGB(127, 255, 255)),
    WINTER("Winter", 14, true, ChatColor.WHITE, Material.SNOWBALL, Color.fromRGB(255, 255, 255), Color.fromRGB(127,255, 255)),
    FESTIVAL("Festival", 15, true, ChatColor.LIGHT_PURPLE, Material.FIREWORK_ROCKET, Color.fromRGB(100, 0, 200), Color.fromRGB(200, 0, 100)),
    BLURPLE("Blurple", 16, true, ChatColor.BLUE, Material.BLUE_TERRACOTTA, Color.fromRGB(100, 123, 213), Color.fromRGB(91, 101, 234)),
    PLAINS("Plains", 17, true, ChatColor.GREEN, Material.GRASS_BLOCK, Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 0)),
    RANDOM("Random", 18, true, ChatColor.BLACK, Material.WARPED_FUNGUS_ON_A_STICK, null, null);

    private String name;
    private short typeId;
    private boolean variant; // type of trail, false = basic, true = spiral
    private ChatColor color; // color for the text to be displayed as
    private Material mat; // material for the trail to be displayed as
    private Color color1;
    private Color color2;

    private static final Map<String, TrailType> NAME_MAP = new HashMap<String, TrailType>();
    private static final Map<Short, TrailType> ID_MAP = new HashMap<Short, TrailType>();

    static {
        for (TrailType type : TrailType.values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(), type);
            }
            if (type.typeId > 0) {
                ID_MAP.put(type.typeId, type);
            }
        }
    }

    TrailType(String name, int typeId, boolean variant, ChatColor color, Material mat, Color color1, Color color2) {
        this.name = name;
        this.typeId = (short) typeId;
        this.variant = variant;
        this.color = color;
        this.mat = mat;
        this.color1 = color1;
        this.color2 = color2;
    }

    public static TrailType fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static TrailType fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public Material getMat() {
        return this.mat;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSpiral() {
        return variant;
    }

    public boolean isBasic() {
        return !variant;
    }

    public Color getColor1() {
        return this.color1;
    }

    public Color getColor2() {
        return this.color2;
    }

}
