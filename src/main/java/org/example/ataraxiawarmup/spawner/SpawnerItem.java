package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.StringNormalizer;

import java.util.ArrayList;
import java.util.List;

public class SpawnerItem {

    private String name;
    private List<String> lore;
    private EntityType spawnType;
    private double interval;

    private ItemStack item;

    public SpawnerItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        EntityType type = EntityType.ZOMBIE;
        double interval = 5;

        Bukkit.getPlayer("MexLr").sendMessage("" + lore.size());

        if (lore.get(0).split(" ").length == 3) {
            type = EntityType.valueOf(ChatColor.stripColor(lore.get(0).split(" ")[2]).toUpperCase());
            interval = Double.parseDouble(ChatColor.stripColor(lore.get(1).split(" ")[1]));
        }
        this.spawnType = type;
        this.interval = interval;
    }

    public SpawnerItem(EntityType spawnType, double interval) {
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
    public EntityType getSpawnType() {
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
    public ItemStack getItem() {
        item = new ItemStack(Material.SPAWNER);
        lore = new ArrayList<String>();
        ItemMeta im = item.getItemMeta();
        StringNormalizer stringNormalizer = new StringNormalizer();

        name = "§9" + stringNormalizer.normalizeString(this.spawnType.toString().toLowerCase() + " Spawner");

        im.setDisplayName(name);

        lore.add("§3Spawns a " + "§6" + stringNormalizer.normalizeString(this.spawnType.toString()));
        lore.add("§3every " + "§6" + (int) this.interval + "§3 seconds.");

        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }
}
