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
    private final CustomMob spawnType;
    private final double interval;
    private final int level;

    private ItemStack item;

    public SpawnerItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        List<String> lore = itemMeta.getLore();

        CustomMob type;
        double interval;

        // getting the level
        assert lore != null;
        String mobLevel = ChatColor.stripColor(lore.get(0).split(" ")[3]);

        // getting the mob's name
        StringBuilder mobName = new StringBuilder();
        // all of the strings after the level, in an array
        String[] mobNameString = lore.get(0).split(mobLevel)[1].trim().split(" ");
        for (String string : mobNameString) {
            mobName.append(" ").append(string);
        }
        // trim the mobName, as there will be a space at the very beginning of the string
        mobName = new StringBuilder(mobName.toString().trim());
        type = CustomMob.fromName(ChatColor.stripColor(mobLevel + mobName));
        interval = Double.parseDouble(ChatColor.stripColor(lore.get(1).split(" ")[1]));

        // get the level of the spawner (the last number in the name)

        this.level = Integer.parseInt(ChatColor.stripColor(itemMeta.getDisplayName().split(" ")[itemMeta.getDisplayName().split(" ").length - 1]));
        this.spawnType = type;
        this.interval = interval;
    }

    public SpawnerItem(CustomMob spawnType, double interval, int level) {
        this.spawnType = spawnType;
        this.interval = interval;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    /**
     * Get the item form of the spawner.
     *
     * @return - The item form of the spawner.
     */
    public ItemStack toItemStack() {
        item = new ItemStack(Material.SPAWNER);
        lore = new ArrayList<>();
        ItemMeta im = item.getItemMeta();

        name = "§9Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName() + " Spawner " + "§b" + this.level;

        assert im != null;
        im.setDisplayName(name);

        lore.add("§3Spawns a " + "§6Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName());
        lore.add("§3every " + "§6" + (int) this.interval + "§3 seconds.");
        lore.add("§eNOTE: The mob spawns directly under the spawner.");

        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    /**
     * Get the item form of the spawner, including its level.
     *
     * @return - The item form of the spawner.
     */
    public ItemStack toItemStack(int level) {
        item = new ItemStack(Material.SPAWNER);
        lore = new ArrayList<>();
        ItemMeta im = item.getItemMeta();

        name = "§9Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName() + " Spawner " + "§b" + level;

        assert im != null;
        im.setDisplayName(name);

        lore.add("§3Spawns a " + "§6Lv. " + this.spawnType.getLevel() + " " + this.spawnType.getName());
        lore.add("§3every " + "§6" + (int) this.interval + "§3 seconds.");
        lore.add("§eNOTE: The mob spawns directly under the spawner.");

        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }
}
