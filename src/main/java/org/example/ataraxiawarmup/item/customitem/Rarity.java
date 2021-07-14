package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public enum Rarity {
    NULL("Null", 0, ChatColor.MAGIC, 0),
    COMMON("Common", 1, ChatColor.WHITE, 5),
    UNCOMMON("Uncommon", 2, ChatColor.GREEN, 1),
    RARE("Rare", 3, ChatColor.BLUE, 0.5),
    EPIC("Epic", 4, ChatColor.DARK_PURPLE, 0.1),
    LEGENDARY("Legendary", 5, ChatColor.GOLD, 0.01),
    GODLIKE("Godlike", 6, ChatColor.LIGHT_PURPLE, 0.001),
    TRINITY("TRINITY", 7, ChatColor.DARK_AQUA, 0),
    DUALITY("Duality", 8, ChatColor.WHITE, 0);

    private String name;
    private short id;
    private ChatColor color;
    private double dropProbability;

    private static final Map<String, Rarity> NAME_MAP = new HashMap<String, Rarity>();
    private static final Map<Short, Rarity> ID_MAP = new HashMap<Short, Rarity>();

    static {
        for (Rarity type : Rarity.values()) {
            if (type.name != null) {
                NAME_MAP.put(ChatColor.stripColor(type.name.toLowerCase()), type);
            }
            if (type.id > 0) {
                ID_MAP.put(type.id, type);
            }
        }
    }

    Rarity(String name, int id, ChatColor color, double dropProbability) {
        this.name = name;
        this.id = (short) id;
        this.color = color;
        this.dropProbability = dropProbability;
    }

    public static Rarity fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static Rarity fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ChatColor getColor() {
        return color;
    }

    public double getDropProbability() {
        return dropProbability;
    }

    public String getLore() {
        if (this == DUALITY) {
            return ChatColor.BLACK + "" + ChatColor.BOLD + "DUA" + ChatColor.WHITE + ChatColor.BOLD + "LITY";
        }
        return getColor() + "" + ChatColor.BOLD + getName().toUpperCase();
    }
}
