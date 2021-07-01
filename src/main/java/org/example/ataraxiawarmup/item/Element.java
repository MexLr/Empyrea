package org.example.ataraxiawarmup.item;

import org.bukkit.ChatColor;

import java.util.*;

public enum Element {

    NEUTRAL("Neutral", "Chaos", 0.5, "All", 1.0, ChatColor.GOLD, "✤"),
    FIRE("Fire", "Water", 0.75, "Earth", 1.5, ChatColor.RED, "✹"),
    WATER("Water", "Earth", 0.75, "Fire", 1.5, ChatColor.AQUA, "❉"),
    EARTH("Earth", "Fire", 0.75, "Water", 1.5, ChatColor.DARK_GREEN, "✤"),
    CHAOS("Chaos", "All", 1.0, "All", 2.0, ChatColor.DARK_PURPLE, "✯");

    private String name;
    private String weakAgainst;
    private String strongAgainst;
    private double damageMultiWeak;
    private double damageMultiStrong;
    private ChatColor color;
    private String representingChar;

    private static final Map<String, Element> CHAR_MAP = new HashMap<>();

    static {
        for (Element type : values()) {
            CHAR_MAP.put(type.getColor() + type.representingChar, type);
        }
    }

    Element(String name, String weakAgainst, double damageMulti, String strongAgainst, double damageMulti2, ChatColor color, String representingChar) {
        this.name = name;
        this.weakAgainst = weakAgainst;
        this.damageMultiWeak = damageMulti;
        this.strongAgainst = strongAgainst;
        this.damageMultiStrong = damageMulti2;
        this.color = color;
        this.representingChar = representingChar;
    }

    public String getName() {
        return name;
    }

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
}
