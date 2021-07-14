package org.example.ataraxiawarmup.item.customitem.ability;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.item.customitem.Rarity;
import org.example.ataraxiawarmup.item.customitem.ability.Ability;

import java.util.List;

public class CustomArtifact extends CustomItem {

    private Ability ability;
    private int level;

    public CustomArtifact(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, Ability ability, int level) {
        super(material, name, rarity, recipeMatrix, false);
        this.ability = ability;
        this.level = level;
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add("");
        lore.add(ChatColor.BLUE + "Adds the " + ability.getColor() + ability.getPrefix(level - 1) + ChatColor.BLUE + " reforge to a weapon.");
        lore.add(ChatColor.GRAY + "Also gives the weapon level " + level + " of the " + ChatColor.STRIKETHROUGH + "" + ability.getColor() + ability.getName() + ChatColor.GRAY + " ability.");
        lore.add(ChatColor.GRAY + "Abilities are not yet in the game! The reforge will still work.");
        itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    public Ability getAbility() {
        return ability;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {

    }
}
