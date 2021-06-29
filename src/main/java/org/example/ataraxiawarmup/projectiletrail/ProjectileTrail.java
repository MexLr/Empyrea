package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;

abstract class ProjectileTrail {

    AbstractArrow arrow;

    int id;

    /**
     * Set the arrow the projectile trail follows.
     *
     * @param arrow - The arrow the projectile trail follows
     */
    void setArrow(AbstractArrow arrow) {
        this.arrow = arrow;
    }

    /**
     * Set the task id of the ProjectileTrail runnable.
     *
     * @param id - the id of the runnable
     */
    void setTaskID(int id) {
        this.id = id;
    }

    /**
     * Cancel the runnable.
     */
    void cancel() {
        Bukkit.getServer().getScheduler().cancelTask(id);
    };

    /**
     * Get the runnable used for displaying the projectile trail.
     *
     * @return - Runnable that displays particles following the path of the projectile
     */
    abstract Runnable getRunnable();

}
