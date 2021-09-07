package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ItemAttribute {

    FIREPERCENT("% Fire Damage", ChatColor.RED, 1),
    WATERPERCENT("% Water Damage", ChatColor.AQUA, 2),
    EARTHPERCENT("% Earth Damage", ChatColor.DARK_GREEN, 3),
    THUNDERPERCENT("% Thunder Damage", ChatColor.YELLOW, 4),
    AIRPERCENT("% Air Damage", ChatColor.GRAY, 5),
    CHAOSPERCENT("% Chaos Damage", ChatColor.DARK_PURPLE, 6),
    FIREDEF(" Fire Defense", ChatColor.RED, 7),
    WATERDEF(" Water Defense", ChatColor.AQUA, 8),
    EARTHDEF(" Earth Defense", ChatColor.DARK_GREEN, 9),
    THUNDERDEF(" Thunder Defense", ChatColor.YELLOW, 10),
    AIRDEF(" Air Defense", ChatColor.GRAY, 11),
    CHAOSDEF(" Chaos Defense", ChatColor.DARK_PURPLE, 12),
    FIREDAMAGE("Fire Weapon Damage", ChatColor.RED),
    WATERDAMAGE("Water Weapon Damage", ChatColor.AQUA),
    EARTHDAMAGE("Earth Weapon Damage", ChatColor.DARK_GREEN),
    THUNDERDAMAGE("Thunder Weapon Damage", ChatColor.YELLOW),
    AIRDAMAGE("Air Weapon Damage", ChatColor.GRAY),
    CHAOSDAMAGE("Chaos Weapon Damage", ChatColor.DARK_PURPLE),
    HEALTH("♥", ChatColor.DARK_RED, 13),
    LOOTBONUS("% Loot Bonus", ChatColor.WHITE, 14),
    XPBONUS("% XP Bonus", ChatColor.GREEN, 15),
    LIFESTEAL(" Life Steal", ChatColor.RED, 16),
    ABILITYREGEN("% Ability Regen (Additive)", ChatColor.DARK_AQUA, 17),
    ALLPERCENT("All", ChatColor.BOLD),
    ALLDEF("All", ChatColor.BOLD),
    ALLDAMAGE("All", ChatColor.BOLD);

    private static List<ItemAttribute> ATTRIBUTE_ORDER = new ArrayList<>();

    private String name;
    private ChatColor color;
    private int id;

    static {
        ATTRIBUTE_ORDER.add(HEALTH);
        ATTRIBUTE_ORDER.add(FIREPERCENT);
        ATTRIBUTE_ORDER.add(FIREDEF);
        ATTRIBUTE_ORDER.add(WATERPERCENT);
        ATTRIBUTE_ORDER.add(WATERDEF);
        ATTRIBUTE_ORDER.add(EARTHPERCENT);
        ATTRIBUTE_ORDER.add(EARTHDEF);
        ATTRIBUTE_ORDER.add(THUNDERPERCENT);
        ATTRIBUTE_ORDER.add(THUNDERDEF);
        ATTRIBUTE_ORDER.add(AIRPERCENT);
        ATTRIBUTE_ORDER.add(AIRDEF);
        ATTRIBUTE_ORDER.add(CHAOSPERCENT);
        ATTRIBUTE_ORDER.add(CHAOSDEF);
        ATTRIBUTE_ORDER.add(ALLPERCENT);
        ATTRIBUTE_ORDER.add(ALLDEF);
        ATTRIBUTE_ORDER.add(LOOTBONUS);
        ATTRIBUTE_ORDER.add(XPBONUS);
        ATTRIBUTE_ORDER.add(LIFESTEAL);
        ATTRIBUTE_ORDER.add(ABILITYREGEN);
        ATTRIBUTE_ORDER.add(FIREDAMAGE);
        ATTRIBUTE_ORDER.add(WATERDAMAGE);
        ATTRIBUTE_ORDER.add(EARTHDAMAGE);
        ATTRIBUTE_ORDER.add(THUNDERDAMAGE);
        ATTRIBUTE_ORDER.add(AIRDAMAGE);
        ATTRIBUTE_ORDER.add(CHAOSDAMAGE);
        ATTRIBUTE_ORDER.add(ALLDAMAGE);
    }

    ItemAttribute(String name, ChatColor color) {
        this(name, color, -1);
    }

    ItemAttribute(String name, ChatColor color, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public static Map<ItemAttribute, Integer> getAttributeBonuses(Player player) {
        Map<ItemAttribute, Integer> bonuses = new HashMap<>();
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            CustomWeapon weapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
            if (weapon != null) {
                for (ItemAttribute attribute : weapon.getAttributes().keySet()) {
                    bonuses.put(attribute, weapon.getAttributes().get(attribute));
                }
            }
            ItemStack[] armorPieces = player.getInventory().getArmorContents();
            for (ItemStack item : armorPieces) {
                if (item != null) {
                    CustomArmor armor = (CustomArmor) CustomItem.fromName(item.getItemMeta().getDisplayName());
                    if (armor != null) {
                        for (ItemAttribute attribute : armor.getAttributes().keySet()) {
                            if (bonuses.containsKey(attribute)) { // if the bonus is already there, add to it, otherwise directly set the bonus value
                                bonuses.replace(attribute, armor.getAttributes().get(attribute) + bonuses.get(attribute));
                            } else {
                                bonuses.put(attribute, armor.getAttributes().get(attribute));
                            }
                        }
                    }
                }
            }
        }
        return bonuses;
    }

    public static List<ItemAttribute> getAttributeOrder() {
        return ATTRIBUTE_ORDER;
    }

}
