package org.example.ataraxiawarmup.spawner;


import org.example.ataraxiawarmup.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.example.ataraxiawarmup.mob.CustomMob;

public class Spawner {

    private CustomMob mob;
    private double interval;
    private boolean isActive = true;
    private Location location;

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
     * @param interval - The interval between each spawn cycle, in seconds
     * @param location - The location of the spawner.
     */
    public Spawner(CustomMob mob, double interval, Location location, Main plugin) {
        this.mob = mob;
        this.interval = interval;
        this.location = location;
        this.plugin = plugin;
    }

    public void spawnMob() {
        if (!isActive)
            return;
        // Get a randomized location around the spawner's location
        Location spawnLocation = location.clone();

        double min = -5;
        double max = 5;
        double xRandom = Math.random() * (max - min + 1) + min;
        xRandom = Math.floor(xRandom * 100) / 100;
        double zRandom = Math.random() * (max - min + 1) + min;
        zRandom = Math.floor(zRandom * 100 / 100);

        spawnLocation.add(xRandom, 0, zRandom);

        // Spawn the entity at the new location
        this.mob.clone().spawn(spawnLocation);
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
        return this.isActive();
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

}
