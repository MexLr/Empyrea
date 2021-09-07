package org.example.ataraxiawarmup.player;

import org.bukkit.ChatColor;

public enum Chat {
    ALL(""),
    PARTY(ChatColor.GOLD + "[Party] "),
    INPUT("");

    private final String prefix;

    Chat(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
