package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CustomRecipe {

    public static final Map<String, CustomItemStack[]> NAME_MAP = new HashMap<>();
    public static final Map<CustomItemStack[], CustomItemStack> RECIPE_MAP = new HashMap<>();

    private CustomItemStack[] recipeMatrix = new CustomItemStack[9];
    private CustomItemStack result;

    public CustomRecipe() {}

    public CustomRecipe(CustomItemStack[] matrix, CustomItemStack result) {
        this.recipeMatrix = matrix;
        this.result = result;
        NAME_MAP.put(ChatColor.stripColor(result.getItemMeta().getDisplayName()).toLowerCase(), matrix);
        RECIPE_MAP.put(matrix, result);
        Bukkit.getPlayer("MexLr").sendMessage(ChatColor.stripColor(result.getItemMeta().getDisplayName()).toLowerCase());
    }

    public static CustomItemStack[] fromName(String name) {
        if (name == null || !NAME_MAP.containsKey(ChatColor.stripColor(name.toLowerCase()))) {
            return null;
        }
        return NAME_MAP.get(ChatColor.stripColor(name.toLowerCase()));
    }

    public static CustomItemStack getResult(CustomItemStack[] recipe) {
        if (recipe == null) {
            return null;
        }
        for (CustomItemStack[] key : RECIPE_MAP.keySet()) {
            if (key == null) {
                continue;
            }
            int successes = 0;
            for (int i = 0; i < key.length; i++) {
                if (key[i] == null && recipe[i] == null) {
                    successes++;
                    continue;
                }
                if (key[i] == null || recipe[i] == null) {
                    break;
                }
                if (key[i].isLess(recipe[i])) {
                    Bukkit.getPlayer("MexLr").sendMessage("§6test");
                    successes++;
                } else {
                    break;
                }
            }
            if (successes == 9) {
                return RECIPE_MAP.get(key);
            }
        }
        return null;
    }

    public CustomItemStack[] getMatrix() {
        return this.recipeMatrix;
    }

    public CustomItemStack getResult() {
        return this.result;
    }
}