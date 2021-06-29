package org.example.ataraxiawarmup.spawner;


import org.example.ataraxiawarmup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Spawner {

    private EntityType mobType;
    private double interval;
    private boolean isActive = true;
    private Location location;

    private Main plugin;

    public Spawner(Main plugin, Location location) {
        this.mobType = EntityType.ZOMBIE;
        this.interval = 10.0;
        this.location = location;
        this.plugin = plugin;
    }

    /**
     * Constructor that takes a mob type and interval in seconds
     *
     * @param type - The type of mob the org.example.ataraxiawarmup.spawner spawns
     * @param interval - The interval between each spawn cycle, in seconds
     * @param location - The location of the org.example.ataraxiawarmup.spawner.
     */
    public Spawner(EntityType type, double interval, Location location, Main plugin) {
        this.mobType = type;
        this.interval = interval;
        this.location = location;
        this.plugin = plugin;
    }

    public void spawnMob() {
        if (!isActive)
            return;
        // Get a randomized location around the org.example.ataraxiawarmup.spawner's location
        Location spawnLocation = location.clone();

        double min = -5;
        double max = 5;
        double xRandom = Math.random() * (max - min + 1) + min;
        xRandom = Math.floor(xRandom * 100) / 100;
        double zRandom = Math.random() * (max - min + 1) + min;
        zRandom = Math.floor(zRandom * 100 / 100);

        spawnLocation.add(xRandom, 0, zRandom);

        // Spawn the entity at the new location
        location.getWorld().spawnEntity(spawnLocation, mobType);
    }

    public void startSpawning() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                spawnMob();
            }
        }, 0L, (long) interval * 20);
    }

    /**
     * Get the type of mob the org.example.ataraxiawarmup.spawner is spawning.
     *
     * @return - the org.example.ataraxiawarmup.spawner's mobType.
     */

    public EntityType getMobType() {
        return this.mobType;
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
    public void setMobType(EntityType type) {
        if (type != null)
            this.mobType = type;
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
        return this.isActive();
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

}
