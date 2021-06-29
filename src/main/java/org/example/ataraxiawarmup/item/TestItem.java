package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;

import java.util.*;

public class TestItem extends CustomItem {

    @Override
    protected ItemDefinition getItemDefinition() {
        return new ItemDefinition("Test Item", getCustomItemType().getMaterial(), Rarity.LEGENDARY);
    }

    @Override
    public CustomRecipe getRecipe() {
        return null;
    }

    @Override
    public CustomItemType getCustomItemType() {
        return CustomItemType.BLAZE_ROD;
    }

    @Override
    public void onUseLeft(Player player) {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
    }

    @Override
    public void onUseRight(Player player) {
        player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON);
    }

    @Override
    public void initialize() {
        createRecipe(new CustomItemStack[]
                {new CustomItemStack(itemFromName("Test Ingredient"), 3), new CustomItemStack(itemFromName("Test Ingredient"), 3), new CustomItemStack(itemFromName("Test Ingredient"), 3),
                null, new CustomItemStack(itemFromName("Test Ingredient"), 3), null,
                new CustomItemStack(itemFromName("Test Ingredient"), 3), null, null}
        );
    }
}
