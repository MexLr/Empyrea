package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.menu.MenuInventory;

public class MenuItem extends CustomItem {
    public MenuItem() {
        super(Material.NETHER_STAR, "Menu", Rarity.TRINITY, null, false, null);
        CUSTOM_ITEMS.put("menu", this);
    }

    @Override
    public void onUseLeft(Player player) {

    }

    @Override
    public void onUseRight(Player player) {
        player.openInventory(new MenuInventory(player).getInv());
    }
}
