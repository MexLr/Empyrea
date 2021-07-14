package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class CustomArmor extends CustomAttributableItem {

    public CustomArmor(Material material, String name, Rarity rarity, Map<ItemAttribute, Integer> attributeMap, String extraLore) {
        super(material, name, rarity, null, attributeMap, false);
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore();

        if (attributeMap.containsKey(ItemAttribute.HEALTH)) {
            lore.add(ChatColor.DARK_RED + "+" + attributeMap.get(ItemAttribute.HEALTH) + "♥");
            lore.add("");
        }

        for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
            if (getAttributes().keySet().contains(attribute) && attribute.getName() == "All") {
                this.addAllAttributes(attribute, getAttributes().get(attribute));
            }
        }

        for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
            if (getAttributes().keySet().contains(attribute) && attribute.getName() != "All") {
                lore.add(attribute.getColor() + "+" + getAttributeValue(attribute) + attribute.getName());
            }
        }

        lore.add("");
        String loreString = "";
        int totalCharacters = 0;
        for (int c = 0; c < extraLore.length(); c++) {
            loreString += extraLore.charAt(c);
            if (c > 20 + totalCharacters) {
                if (extraLore.charAt(c) == ' ') {
                    lore.add(ChatColor.GRAY + loreString);
                    totalCharacters += c;
                    loreString = "";
                }
            }
        }
        lore.add(ChatColor.GRAY + loreString);

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        setItemMeta(itemMeta);
        CUSTOM_ITEMS.put(ChatColor.stripColor(itemMeta.getDisplayName()).toLowerCase(), this);
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {

    }
}
