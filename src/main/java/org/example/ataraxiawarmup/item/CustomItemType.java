package org.example.ataraxiawarmup.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public enum CustomItemType {

    AIR("", Material.AIR, -1),
    BLAZE_ROD("Blaze Rod", Material.BLAZE_ROD, 1),
    BLAZE_POWDER("Blaze Powder", Material.BLAZE_POWDER, 2);

    private static final Map<String, CustomItemType> NAME_MAP = new HashMap<>();
    private static final Map<Short, CustomItemType> ID_MAP = new HashMap<>();

    static {
        for (CustomItemType type : CustomItemType.values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(), type);
            }
            if (type.id > 0) {
                ID_MAP.put(type.id, type);
            }
        }
    }

    private String name;
    private Material mat;
    private short id;

    CustomItemType(String name, Material mat, int id) {
        this.name = name;
        this.mat = mat;
        this.id = (short) id;
    }

    public static CustomItemType fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static CustomItemType fromId(int id) {
        if (id > Short.MAX_VALUE) {
            return null;
        }
        return ID_MAP.get((short) id);
    }

    /**
     * Get the name of the item type.
     *
     * @return - The name of the item type
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Minecraft material this item type is represented by.
     *
     * @return - Minecraft material representation of the item type
     */
    public Material getMaterial() {
        return mat;
    }

    /**
     * Get the id of this item type.
     *
     * @return - The id of the item type
     */
    public int getId() {
        return id;
    }
}
