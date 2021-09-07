package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.item.customitem.*;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.*;

public class Reward {

    private static final Map<Integer, Reward> REWARDS = new HashMap<>();

    private static int numOfRewards = 0;

    private RewardCause rewardCause;

    private final Map<UUID, Integer> receivers = new HashMap<>(); // map is player uuid, value contributed
    private final int seed;
    private final int totalValue;
    private final int id;
    private final int maxItemTypes;

    private final Rarity maxRarity; // max rarity of item that can be included in the reward
    private final Rarity minRarity; // minimum rarity, so things don't break too much (like giving 10000 of an item)

    private GeneratedReward generatedReward;

    private Order order; // if the reward originated from an order

    public Reward(int totalValue, Rarity minRarity, Rarity maxRarity, RewardCause cause, int maxItemTypes) {
        Random random = new Random();
        this.seed = random.nextInt(30);
        this.totalValue = totalValue;
        this.maxRarity = maxRarity;
        this.minRarity = minRarity;
        this.maxItemTypes = maxItemTypes;
        this.rewardCause = cause;
        this.id = ++numOfRewards - 1;
        REWARDS.put(id, this);
    }

    public RewardCause getCause() {
        return rewardCause;
    }

    public void setCause(RewardCause cause) {
        this.rewardCause = cause;
    }

    public void setOrder(Order order) {
        this.order = order;
        setCause(RewardCause.ORDER);
    }

    public Collection<UUID> getReceivers() {
        return receivers.keySet();
    }

    public void removeReceiver(CustomPlayer customPlayer) {
        if (receivers.containsKey(customPlayer.getPlayer().getUniqueId())) {
            receivers.remove(customPlayer.getPlayer().getUniqueId());
            customPlayer.removeReward(this);
        }
    }

    public void addReceiver(Player player, int contribution) {
        if (!receivers.containsKey(player.getUniqueId())) {
            receivers.put(player.getUniqueId(), contribution);
        } else {
            receivers.replace(player.getUniqueId(), receivers.get(player.getUniqueId()) + contribution);
        }
    }

    public void setReceiver(Player player, int percentage) {
        if (receivers.containsKey(player.getUniqueId())) {
            receivers.replace(player.getUniqueId(), percentage);
        }
    }

    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.AIR);
        switch (rewardCause) {
            case ORDER:
                ItemStack orderItem = order.getItem();
                item.setType(orderItem.getType());
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(orderItem.getItemMeta().getDisplayName());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.DARK_GRAY + "ID " + id);
                lore.add(ChatColor.AQUA + "Rewards:");
                lore.add(ChatColor.AQUA + "Up to " + maxItemTypes + " different item types of any rarity from ");
                lore.add(minRarity.getLore() + ChatColor.AQUA + " to " + maxRarity.getLore() + ChatColor.AQUA + ", and " + ChatColor.DARK_AQUA + "combat experience,");
                lore.add(ChatColor.RED + "ALL" + ChatColor.AQUA + " totaling " + this.totalValue + " value!");
                lore.add("");
                lore.add(ChatColor.YELLOW + "You contributed towards " + receivers.get(player.getUniqueId()) + "% of this order!");
                lore.add(ChatColor.GOLD + "That means you get " + receivers.get(player.getUniqueId()) + "% of the rewards!");

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
                break;
        }
        return item;
    }

    public void generateReward(Player player) {
        // do generate reward thing with the seed here
        List<CustomItem> customItems = new ArrayList<>();
        customItems.addAll(CustomItem.CUSTOM_ITEMS.values());

        CustomItemStack[] itemsRewarded = new CustomItemStack[maxItemTypes];

        double valueLeft = totalValue * (double) receivers.get(player.getUniqueId()) / 100;

        for (int i = 0; i < maxItemTypes; i++) {
            // first, find an item
            CustomItem itemGiven = null;
            boolean found = false;

            // randomly grab an item (seed) times, until the criteria (rarity, mainly) match up
            Random random = new Random();
            while (!found) {
                for (int s = 0; s < seed; s++) {
                    itemGiven = customItems.get(random.nextInt(CustomItem.CUSTOM_ITEMS.values().size() - 1));
                    if (itemGiven instanceof CustomWeapon) {
                        if (((CustomWeapon) itemGiven).hasAbility()) {
                            itemGiven = null;
                        }
                        if (random.nextInt(seed) > random.nextInt(seed * seed)) {
                            itemGiven = null;
                        }
                    }
                    if (itemGiven instanceof CustomIngredient) {
                        if (itemGiven.getRarity().getId() > 1 && itemGiven.getRecipe() == null) {
                            if (random.nextInt(4) > random.nextInt(16)) {
                                itemGiven = null;
                            }
                        }
                    }
                }
                if (itemGiven == null) {
                    continue;
                }
                if (itemGiven.getRarity().getId() <= maxRarity.getId() && itemGiven.getRarity().getId() >= minRarity.getId()) {
                    found = true; // item has been found
                    if (valueLeft <= 0) {
                        break;
                    }
                    Bukkit.getPlayer("MexLr").sendMessage(itemGiven.getItemMeta().getDisplayName());
                    // randomly generate a value for the item
                    // step 1: get the item's value
                    double itemValue = itemGiven.getValue();
                    Bukkit.getPlayer("MexLr").sendMessage("" + itemValue);
                    // step 2: get the maximum amount of this item that can be rewarded (valueLeft / itemValue, rounded down)
                    int maxAmount = (int) Math.floor((valueLeft / itemValue));
                    if (maxAmount < 1) {
                        break;
                    }
                    Bukkit.getPlayer("MexLr").sendMessage("" + maxAmount);
                    // step 3: get a random number for the amount of items in the item stack
                    int itemAmount;
                    if (maxAmount == 1 || itemGiven instanceof CustomWeapon || itemGiven instanceof CustomArmor) {
                        itemAmount = 1;
                    } else {
                        itemAmount = random.nextInt(maxAmount - 1) + 1;
                    }
                    Bukkit.getPlayer("MexLr").sendMessage("" + itemAmount);
                    // step 4: subtract value from valueLeft
                    valueLeft -= itemAmount * itemValue;
                    // step 5: add the rewarded item to the itemsRewarded array
                    itemsRewarded[i] = new CustomItemStack(itemGiven, itemAmount);
                }
            }
        }
        Bukkit.getPlayer("MexLr").sendMessage("Value left: " + valueLeft);

        this.generatedReward = new GeneratedReward((int) (10 * valueLeft), itemsRewarded);
        switch (rewardCause) {
            case ORDER:
                this.generatedReward.setOrder(order);
                break;
        }
    }

    public GeneratedReward getReward() {
        return generatedReward;
    }

    public Order getOrder() {
        return order;
    }

    public enum RewardCause {
        ORDER
    }

    public static Collection<Reward> getRewards() {
        return REWARDS.values();
    }

    public static Reward getReward(int id) {
        return REWARDS.get(id);
    }

}
