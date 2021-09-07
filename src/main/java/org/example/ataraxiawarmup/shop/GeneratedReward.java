package org.example.ataraxiawarmup.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneratedReward {

    private final int expAmount;
    private final CustomItemStack[] itemRewards;
    private final ItemStack[] itemStackRewards;

    private Order order;

    public GeneratedReward(int expAmount, CustomItemStack... itemRewards) {
        this.expAmount = expAmount;
        this.itemRewards = itemRewards;
        this.itemStackRewards = new ItemStack[itemRewards.length];
        for (int i = 0; i < itemRewards.length; i++) {
            if (itemRewards[i] == null) {
                itemStackRewards[i] = null;
                continue;
            }
            itemStackRewards[i] = itemRewards[i].toItemStack();
        }
    }

    public int getExpAmount() {
        return expAmount;
    }

    public CustomItemStack[] getItemRewards() {
        return itemRewards;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void rewardPlayer(CustomPlayer customPlayer) {
        customPlayer.addCombatExp(expAmount);
        Player player = customPlayer.getPlayer();
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Order complete! " + order.getColoredName());
        player.sendMessage(ChatColor.GOLD + "Rewards Claimed:");
        player.sendMessage(ChatColor.DARK_AQUA + "+" + expAmount + " Combat Experience");

        for (CustomItemStack customItemStack : itemRewards) {
            if (customItemStack == null) {
                continue;
            }
            player.sendMessage("+" + customItemStack.getItemMeta().getDisplayName() + " x " + customItemStack.getAmount());
        }

        List<ItemStack> rewardList = new ArrayList<>();

        for (ItemStack itemStack : itemStackRewards) {
            if (itemStack != null) {
                rewardList.add(itemStack);
            }
        }

        HashMap<Integer, ItemStack> extraItems = player.getInventory().addItem(rewardList.toArray(ItemStack[]::new));
        for (ItemStack itemStack : extraItems.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }
    }

}
