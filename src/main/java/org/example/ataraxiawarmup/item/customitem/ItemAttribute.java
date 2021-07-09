package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ItemAttribute {

    FIREPERCENT("% Fire Damage", ChatColor.RED),
    WATERPERCENT("% Water Damage", ChatColor.AQUA),
    EARTHPERCENT("% Earth Damage", ChatColor.DARK_GREEN),
    THUNDERPERCENT("% Thunder Damage", ChatColor.YELLOW),
    AIRPERCENT("% Air Damage", ChatColor.GRAY),
    CHAOSPERCENT("% Chaos Damage", ChatColor.DARK_PURPLE),
    FIREDEF(" Fire Defense", ChatColor.RED),
    WATERDEF(" Water Defense", ChatColor.AQUA),
    EARTHDEF(" Earth Defense", ChatColor.DARK_GREEN),
    THUNDERDEF(" Thunder Defense", ChatColor.YELLOW),
    AIRDEF(" Air Defense", ChatColor.GRAY),
    CHAOSDEF(" Chaos Defense", ChatColor.DARK_PURPLE),
    FIREDAMAGE("Fire Weapon Damage", ChatColor.RED),
    WATERDAMAGE("Water Weapon Damage", ChatColor.AQUA),
    EARTHDAMAGE("Earth Weapon Damage", ChatColor.DARK_GREEN),
    THUNDERDAMAGE("Thunder Weapon Damage", ChatColor.YELLOW),
    AIRDAMAGE("Air Weapon Damage", ChatColor.GRAY),
    CHAOSDAMAGE("Chaos Weapon Damage", ChatColor.DARK_PURPLE),
    ALLPERCENT("All", ChatColor.BOLD),
    ALLDEF("All", ChatColor.BOLD),
    ALLDAMAGE("All", ChatColor.BOLD);

    private static List<ItemAttribute> attributeOrder = new ArrayList<>();

    private String name;
    private ChatColor color;

    static {
        attributeOrder.add(FIREPERCENT);
        attributeOrder.add(FIREDEF);
        attributeOrder.add(WATERPERCENT);
        attributeOrder.add(WATERDEF);
        attributeOrder.add(EARTHPERCENT);
        attributeOrder.add(EARTHDEF);
        attributeOrder.add(THUNDERPERCENT);
        attributeOrder.add(THUNDERDEF);
        attributeOrder.add(AIRPERCENT);
        attributeOrder.add(AIRDEF);
        attributeOrder.add(CHAOSPERCENT);
        attributeOrder.add(CHAOSDEF);
        attributeOrder.add(ALLPERCENT);
        attributeOrder.add(ALLDEF);
        attributeOrder.add(FIREDAMAGE);
        attributeOrder.add(WATERDAMAGE);
        attributeOrder.add(EARTHDAMAGE);
        attributeOrder.add(THUNDERDAMAGE);
        attributeOrder.add(AIRDAMAGE);
        attributeOrder.add(CHAOSDAMAGE);
        attributeOrder.add(ALLDAMAGE);
    }

    ItemAttribute(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public static Map<ItemAttribute, Integer> getAttributeBonuses(Player player) {
        Map<ItemAttribute, Integer> bonuses = new HashMap<>();
        if (player.getInventory().getItemInMainHand().getItemMeta() == null) {
            return null;
        }
        CustomWeapon weapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
        if (weapon != null) {
            for (ItemAttribute attribute : weapon.getAttributes().keySet()) {
                bonuses.put(attribute, weapon.getAttributes().get(attribute));
            }
        }
        return bonuses;
    }

    public static List<ItemAttribute> getAttributeOrder() {
        return attributeOrder;
    }

}
