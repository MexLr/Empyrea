package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomIngredient extends CustomItem {

    public CustomIngredient(Material material, String name, Rarity rarity, CustomItemStack[] recipe, boolean isEnchanted) {
        this(material, name, rarity, recipe, isEnchanted, false);
    }

    public CustomIngredient(Material material, String name, Rarity rarity, CustomItemStack[] recipe, boolean isEnchanted, boolean shapeless) {
        super(material, name, rarity, recipe, shapeless);
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add("");
        lore.add(ChatColor.YELLOW + "Right click to view recipes!");
        itemMeta.setLore(lore);
        if (isEnchanted) {
            itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        setItemMeta(itemMeta);
        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    @Override
    public void onUseRight(Player player) {
        List<CustomRecipe> usedIn = CustomRecipe.usesItem(this);
        RecipeInventory recipeInventory = new RecipeInventory(usedIn, this);
        player.openInventory(recipeInventory.getInventories().get(0));
    }

    @Override
    public void onUseLeft(Player player) {

    }
}
