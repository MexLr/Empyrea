package org.example.ataraxiawarmup.spawner;


import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.spawner.menu.SpawnerMenuInventory;
import org.example.ataraxiawarmup.sql.SqlSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spawner {

    private static final Map<String, List<Spawner>> CHUNK_MAP = new HashMap<>();
    private static final Map<Inventory, Spawner> INVENTORY_SPAWNER_MAP = new HashMap<>();

    private CustomMob mob;
    private double interval;
    private boolean isActive = true;
    private Location location;
    private Entity entity;

    private int id;

    private static int numberOfSpawners = 0;

    private int level;

    private Main plugin;

    public Spawner(Main plugin, Location location) {
        this.mob = CustomMob.fromName("1Spider");
        this.interval = 10.0;
        this.location = location;
        this.plugin = plugin;
    }

    /**
     * Constructor that takes a mob type and interval in seconds
     *
     * @param mob - The CustomMob the spawner spawns
     * @param level - The level of the spawner
     * @param location - The location of the spawner.
     */
    public Spawner(CustomMob mob, int level, Location location, Main plugin, boolean addSpawner) {
        this.mob = mob;
        this.interval = calcIntervalFromLevel(level);
        this.level = level;
        this.location = location;
        this.plugin = plugin;

        if (location == null) {
            return;
        }

        if (addSpawner) {
            SqlSetter setter = new SqlSetter();
            id = ++numberOfSpawners;
            setter.addSpawner(this);
        }

        String key = location.getChunk().getX() + ":" + location.getChunk().getZ();
        if (CHUNK_MAP.containsKey(key)) {
            CHUNK_MAP.get(key).add(this);
        } else {
            List<Spawner> list = new ArrayList<>();
            list.add(this);
            CHUNK_MAP.put(key, list);
        }
    }

    public Spawner(CustomMob mob, int level, Location location, Main plugin, int id) {
        this(mob, level, location, plugin, false);
        this.id = id;
        if (id > numberOfSpawners) {
            numberOfSpawners = id;
        }
    }

    public void spawnMob() {
        if (playerNearby()) {
            if (this.entity != null) {
                if (this.entity.isValid())
                    return;
                if (!this.entity.isValid()) {
                    this.entity = null;
                }
            }

            if (!isActive()) {
                return;
            }

            // Spawn the entity at the new location
            CustomMob mob = getMobType().clone();
            mob.spawn(getLocation());
            this.entity = mob.getEntity();
        }
    }

    public void startSpawning() {
        if (location != null) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    spawnMob();
                }
            }, 0L, (long) interval * 20);
        }
    }

    /**
     * Get the type of mob the org.example.ataraxiawarmup.spawner is spawning.
     *
     * @return - the org.example.ataraxiawarmup.spawner's mobType.
     */

    public CustomMob getMobType() {
        return this.mob;
    }

    /**
     * Get the interval of mob spawns.
     *
     * @return - the interval between the org.example.ataraxiawarmup.spawner's spawn cycle in seconds
     */
    public double getInterval() {
        return this.interval;
    }

    /**
     * Set the type of mob the org.example.ataraxiawarmup.spawner spawns.
     *
     * @param type - The mob type the org.example.ataraxiawarmup.spawner's spawn type is set to.
     */
    public void setMobType(CustomMob type) {
        if (type != null)
            this.mob = type;
    }

    /**
     * Set the interval between mob spawns.
     *
     * @param interval - the interval the org.example.ataraxiawarmup.spawner's interval is being changed to.
     */
    public void setInterval(double interval) {
        this.interval = interval;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public Location getLocation() {
        return location;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public int getId() {
        return id;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean playerNearby() {
        for (Entity entity : this.location.getWorld().getNearbyEntities(this.location, 20, 20, 20)) {
            if (entity instanceof Player) {
                return true;
            }
        }
        return false;
    }

    public void unload() {
        if (this.entity != null) {
            this.entity.remove();
            this.entity = null;
        }
        setActive(false);
    }

    public void load() {
        setActive(true);
    }

    public void readdToDatabase() {
        SqlSetter setter = new SqlSetter();
        setter.removeSpawner(this);
        setter.addSpawner(this);
    }

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
    }

    public ItemStack toItem() {
        return new SpawnerItem(mob, interval, level).toItemStack();
    }

    public ItemStack toItem(int level) {
        return new SpawnerItem(mob, calcIntervalFromLevel(level), level).toItemStack(level);
    }

    public static List<Spawner> fromChunk(Chunk chunk) {
        return CHUNK_MAP.get(chunk.getX() + ":" + chunk.getZ());
    }

    public static int calcIntervalFromLevel(int level) {
        int returnVal = 0;
        switch (level) {
            case 1:
                returnVal = 60;
                break;
            case 2:
                returnVal = 30;
                break;
            case 3:
                returnVal = 15;
                break;
            case 4:
                returnVal = 10;
                break;
            case 5:
                returnVal = 8;
                break;
            case 6:
                returnVal = 5;
                break;
            case 7:
                returnVal = 3;
                break;
            default:
                break;
        }
        return returnVal;
    }
}
