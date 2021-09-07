package org.example.ataraxiawarmup.shop;

import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.item.customitem.Rarity;

public class OrderCreator {

    public void initializeOrders() {
        Reward reward0 = new Reward(1000, Rarity.COMMON, Rarity.UNCOMMON, Reward.RewardCause.ORDER, 3);
        Order order0 = new Order(new CustomItemStack[]{new CustomItemStack(CustomItem.fromName("Fire Shard"), 5), new CustomItemStack(CustomItem.fromName("Rotten Flesh"), 64)}, reward0, Rarity.COMMON, 0, "The First One");

        Reward reward1 = new Reward(100000, Rarity.LEGENDARY, Rarity.GODLIKE, Reward.RewardCause.ORDER, 3);
        Order order1 = new Order(new CustomItemStack[]{new CustomItemStack(CustomItem.fromName("Rotten Flesh"))}, reward1, Rarity.GODLIKE, 1, "ez");

        Reward reward2 = new Reward(10000, Rarity.UNCOMMON, Rarity.RARE, Reward.RewardCause.ORDER, 3);
        Order order2 = new Order(new CustomItemStack[]{new CustomItemStack(CustomItem.fromName("Water Core")), new CustomItemStack(CustomItem.fromName("Water Shard"), 4)}, reward2, Rarity.UNCOMMON, 2, "The Priestess' Request");
    }

}
