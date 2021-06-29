package org.example.ataraxiawarmup.item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public enum Rarity {
    NULL("Null", 0, ChatColor.MAGIC),
    COMMON("Common", 1, ChatColor.WHITE),
    UNCOMMON("Uncommon", 2, ChatColor.GREEN),
    RARE("Rare", 3, ChatColor.BLUE),
    EPIC("Epic", 4, ChatColor.DARK_PURPLE),
    LEGENDARY("Legendary", 5, ChatColor.GOLD),
    GODLIKE("Godlike", 6, ChatColor.LIGHT_PURPLE),
    LOVECRAFTIAN("Lovecraftian", 7, ChatColor.DARK_AQUA);

    private String name;
    private short id;
    private ChatColor color;

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

    Rarity(String name, int id, ChatColor color) {
        this.name = name;
        this.id = (short) id;
        this.color = color;
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

    public String getLore() {
        if (this == LOVECRAFTIAN) {
            return ChatColor.RED + "" + ChatColor.BOLD + "LOVE" + ChatColor.AQUA + "" + ChatColor.BOLD + "CRAFTIAN";
        }
        return getColor() + "" + ChatColor.BOLD + getName().toUpperCase();
    }
}
