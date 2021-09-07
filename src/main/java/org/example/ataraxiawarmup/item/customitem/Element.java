package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;

import java.util.*;

public enum Element {

    NEUTRAL("Neutral", "Chaos", 0.5, "All", 1.0, ChatColor.GOLD, "✤", 7),
    FIRE("Fire", "Water", 0.75, "Earth", 1.5, ChatColor.RED, "✹", 1),
    WATER("Water", "Earth", 0.75, "Fire", 1.5, ChatColor.AQUA, "❉", 2),
    EARTH("Earth", "Fire", 0.75, "Water", 1.5, ChatColor.DARK_GREEN, "✤", 3),
    THUNDER("Thunder", "All", 1.0, "All", 1.0, ChatColor.YELLOW, "✦", 4), // randomly does from 50-200% damage
    AIR("Air", "All", 1.0, "All", 1.0, ChatColor.GRAY, "❋", 5), // bypasses enemy defense
    CHAOS("Chaos", "All", 1.0, "All", 2.0, ChatColor.DARK_PURPLE, "✯", 6);

    private String name;
    private String weakAgainst;
    private String strongAgainst;
    private double damageMultiWeak;
    private double damageMultiStrong;
    private ChatColor color;
    private String representingChar;
    private final int id;

    private static final Map<String, Element> CHAR_MAP = new HashMap<>();
    private static final Map<String, Element> NAME_MAP = new HashMap<>();

    private static final ArrayList<Element> ELEMENT_ORDER = new ArrayList<>();
    private static final ArrayList<Element> ELEMENT_REVERSE_ORDER = new ArrayList<>();

    static {
        for (Element type : values()) {
            CHAR_MAP.put(type.getColor() + type.representingChar, type);
            NAME_MAP.put(type.getName().toLowerCase(), type);
        }
        ELEMENT_ORDER.add(Element.FIRE);
        ELEMENT_ORDER.add(Element.WATER);
        ELEMENT_ORDER.add(Element.EARTH);
        ELEMENT_ORDER.add(Element.THUNDER);
        ELEMENT_ORDER.add(Element.AIR);
        ELEMENT_ORDER.add(Element.CHAOS);
        ELEMENT_ORDER.add(Element.NEUTRAL);

        ELEMENT_REVERSE_ORDER.add(Element.NEUTRAL);
        ELEMENT_REVERSE_ORDER.add(Element.CHAOS);
        ELEMENT_REVERSE_ORDER.add(Element.AIR);
        ELEMENT_REVERSE_ORDER.add(Element.THUNDER);
        ELEMENT_REVERSE_ORDER.add(Element.EARTH);
        ELEMENT_REVERSE_ORDER.add(Element.WATER);
        ELEMENT_REVERSE_ORDER.add(Element.FIRE);
    }

    Element(String name, String weakAgainst, double damageMulti, String strongAgainst, double damageMulti2, ChatColor color, String representingChar, int id) {
        this.name = name;
        this.weakAgainst = weakAgainst;
        this.damageMultiWeak = damageMulti;
        this.strongAgainst = strongAgainst;
        this.damageMultiStrong = damageMulti2;
        this.color = color;
        this.representingChar = representingChar;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    };

    public String getWeakAgainst() {
        return weakAgainst;
    }

    public double getDamageMultiWeak() {
        return damageMultiWeak;
    }

    public String getStrongAgainst() {
        return strongAgainst;
    }

    public double getDamageMultiStrong() {
        return damageMultiStrong;
    }

    public double getDamageMultiAgainst(Element otherElement) {
        if (this.strongAgainst == otherElement.getName() || this.strongAgainst == "All") {
            return this.damageMultiStrong;
        }
        if (this.weakAgainst == otherElement.getName()) {
            return this.damageMultiWeak;
        }
        return 1.0;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getRepresentingChar() {
        return representingChar;
    }

    public String getColoredChar() {
        return getColor() + "" + getRepresentingChar();
    }

    public static Element fromChar(String representingChar) {
        return CHAR_MAP.get(representingChar);
    }

    public static Element fromName(String name) {
        return NAME_MAP.get(name.toLowerCase());
    }

    public static List<Element> getElementOrder() {
        return ELEMENT_ORDER;
    }

    public static List<Element> getReverseElementOrder() {
        return ELEMENT_REVERSE_ORDER;
    }
}
