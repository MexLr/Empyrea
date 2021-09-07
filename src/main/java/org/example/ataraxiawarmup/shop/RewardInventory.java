package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.player.CustomPlayer;

public class RewardInventory {

    private Inventory inv;

    public RewardInventory(Player player) {
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);

        inv = Bukkit.createInventory(null, 54, "Your Rewards");

        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.FILLER_ITEM);
        }
        inv.setItem(49, Main.CLOSE_BARRIER);
        inv.setItem(45, Main.BACK_ARROW);

        if (customPlayer.getPendingRewards().size() == 0) {
            return;
        }
        inv.setItem(10, customPlayer.getPendingRewards().get(0).getItem(player));
    }

    public Inventory getInventory() {
        return inv;
    }

}
