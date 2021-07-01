package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.ArrayList;
import java.util.List;

public class SpawnerItem {

    private String name;
    private List<String> lore;
    private CustomMob spawnType;
    private double interval;

    private ItemStack item;

    public SpawnerItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        CustomMob type = CustomMob.fromName("1Spider");
        double interval = 5;

        Bukkit.getPlayer("MexLr").sendMessage("" + lore.size());

        StringBuilder str = new StringBuilder();
        str.append(lore.get(0).split(" ")[lore.get(0).split(" ").length - 2]).append(lore.get(0).split(" ")[lore.get(0).split(" ").length - 1]);
        type = CustomMob.fromName(ChatColor.stripColor(str.toString()));
        interval = Double.parseDouble(ChatColor.stripColor(lore.get(1).split(" ")[1]));
        this.spawnType = type;
        this.interval = interval;
    }

    public SpawnerItem(CustomMob spawnType, double interval) {
        this.spawnType = spawnType;
        this.interval = interval;
    }

    /**
     * Get the name of the spawner.
     *
     * @return - The name of the spawner
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the type of mob the spawner spawns.
     *
     * @return - The spawner's spawn type
     */
    public CustomMob getSpawnType() {
        return this.spawnType;
    }

    /**
     * Get the interval between mob spawns.
     *
     * @return - The interval between the spawner's mob spawns, in seconds
     */
    public double getInterval() {
        return this.interval;
    }

    /**
     * Get the item form of the spawner.
     *
     * @return - The item form of the spawner.
     */
    public ItemStack toItemStack() {
        item = new ItemStack(Material.SPAWNER);
        lore = new ArrayList<String>();
        ItemMeta im = item.getItemMeta();

        name = "§9Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName() + " Spawner";

        im.setDisplayName(name);

        lore.add("§3Spawns a " + "§6Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName());
        lore.add("§3every " + "§6" + (int) this.interval + "§3 seconds.");

        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }
}
