package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.example.ataraxiawarmup.item.customitem.CustomArmor;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.item.customitem.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomLootTable {

    private List<CustomItem> items = new ArrayList<>();
    private List<Double> probabilities = new ArrayList<>();
    private List<Integer> quantities = new ArrayList<>();
    private Rarity maxRarity;

    public CustomLootTable(List<CustomItem> items, List<Double> probabilities, List<Integer> quantities, Rarity maxRarity, double rarityFactor) {
        this.items.addAll(items);
        this.probabilities.addAll(probabilities);
        this.quantities.addAll(quantities);
        this.maxRarity = maxRarity;
        for (CustomItem item : CustomItem.CUSTOM_ITEMS.values()) {
            if (item instanceof CustomArmor) {
                if (item.getRarity().getId() < this.maxRarity.getId()) {
                    this.items.add(item);
                    this.probabilities.add(item.getRarity().getDropProbability() * rarityFactor);
                    this.quantities.add(1);
                    this.quantities.add(1);
                }
            }
        }
    }

    public List<CustomItemStack> generateItems(double lootBonus) {
        List<CustomItemStack> droppedItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            double min = 0;
            double max = 101;
            double diff = max - min;
            double random = min + Math.random() * diff;
            double probability = probabilities.get(i);
            if (random <= probability) {
                Random rand = new Random();
                int quantity = rand.nextInt(quantities.get(i * 2 + 1) + 1 - quantities.get(i * 2)) + quantities.get(i * 2);
                double multi = 1 + lootBonus / 100;
                if (items.get(i) instanceof CustomArmor) {
                    int totalDropped = (int) (quantity * multi);
                    while (totalDropped > 0) {
                        droppedItems.add(new CustomItemStack(items.get(i), 1));
                        totalDropped--;
                    }
                } else {
                    droppedItems.add(new CustomItemStack(items.get(i), (int) (quantity * multi)));
                }
            }
        }
        return droppedItems;
    }

    public void dropItems(Location location, double lootBonus) {
        List<CustomItemStack> itemsToDrop = generateItems(lootBonus);
        for (CustomItemStack item : itemsToDrop) {
            location.getWorld().dropItemNaturally(location, item.toItemStack());
        }
    }
}
