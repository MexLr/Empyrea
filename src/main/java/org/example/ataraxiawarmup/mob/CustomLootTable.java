package org.example.ataraxiawarmup.mob;

import org.bukkit.Location;
import org.example.ataraxiawarmup.item.CustomItem;
import org.example.ataraxiawarmup.item.CustomItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomLootTable {

    private List<CustomItem> items;
    private List<Double> probabilities;
    private List<Integer> quantities;

    public CustomLootTable(List<CustomItem> items, List<Double> probabilities, List<Integer> quantities) {
        this.items = items;
        this.probabilities = probabilities;
        this.quantities = quantities;
    }

    public List<CustomItemStack> generateItems() {
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
                droppedItems.add(new CustomItemStack(items.get(i), quantity));
            }
        }
        return droppedItems;
    }

    public void dropItems(Location location) {
        List<CustomItemStack> itemsToDrop = generateItems();
        for (CustomItemStack item : itemsToDrop) {
            location.getWorld().dropItemNaturally(location, item.toItemStack());
        }
    }
}
