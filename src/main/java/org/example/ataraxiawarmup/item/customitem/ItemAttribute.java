package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    HEALTH("♥", ChatColor.DARK_RED),
    LOOTBONUS("% Loot Bonus", ChatColor.WHITE),
    LIFESTEAL(" Life Steal", ChatColor.RED),
    ABILITYREGEN("% Ability Regen (Additive)", ChatColor.DARK_AQUA),
    ALLPERCENT("All", ChatColor.BOLD),
    ALLDEF("All", ChatColor.BOLD),
    ALLDAMAGE("All", ChatColor.BOLD);

    private static List<ItemAttribute> ATTRIBUTE_ORDER = new ArrayList<>();

    private String name;
    private ChatColor color;

    static {
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
        ATTRIBUTE_ORDER.add(ABILITYREGEN);
        ATTRIBUTE_ORDER.add(LIFESTEAL);
        ATTRIBUTE_ORDER.add(FIREDAMAGE);
        ATTRIBUTE_ORDER.add(WATERDAMAGE);
        ATTRIBUTE_ORDER.add(EARTHDAMAGE);
        ATTRIBUTE_ORDER.add(THUNDERDAMAGE);
        ATTRIBUTE_ORDER.add(AIRDAMAGE);
        ATTRIBUTE_ORDER.add(CHAOSDAMAGE);
        ATTRIBUTE_ORDER.add(ALLDAMAGE);
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
