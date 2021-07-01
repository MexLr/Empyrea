package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Cooldowns;
import org.example.ataraxiawarmup.Main;

import java.util.*;

public abstract class CustomCooldownableItem extends CustomItem {

    public CustomCooldownableItem(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix) {
        super(material, name, rarity, recipeMatrix);
    }

    public boolean addToCooldown(Player player, String name, long length) {
        return Cooldowns.tryCooldown(player, name, length);
    }
}
