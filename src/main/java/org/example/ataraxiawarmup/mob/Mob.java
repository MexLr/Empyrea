package org.example.ataraxiawarmup.mob;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Mob extends CustomEntity, Damageable {

    /**
     * Gets the height of the mob's eyes above its location.
     *
     * @return - height of mob's eyes above its location
     */
    public double getEyeHeight();

    /**
     * Get the location of the mob's current eye position.
     *
     * @return - The location of the mob's eyes
     */
    public Location getEyeLocation();

    /**
     * Returns the mob's current maximum invincibility ticks.
     *
     * @return - Maximum no damage ticks
     */
    public int getMaximumNoDamageTicks();

    /**
     * Sets the living entity's current maximum invincibility ticks.
     *
     * @param ticks - Maximum amount of no damage ticks.
     */
    public void setMaximumNoDamageTicks(int ticks);

    /**
     * Returns the mob's last damage taken in the current invincibility time.
     *
     * @return - Damage taken since the last no damage ticks time period
     */
    public double getLastDamage();

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     *
     * @param damage - Amount of damage
     */
    public void setLastDamage(double damage);

    /**
     * Returns the mob's current no damage ticks.
     *
     * @return - Amount of no damage ticks
     */
    public int getNoDamageTicks();

    /**
     * Sets the mob's current no damage ticks.
     *
     * @param ticks - Amount of no damage ticks
     */
    public void setNoDamageTicks(int ticks);

    /**
     * Get the player identified as the killer of the mob.
     *
     * @return - Player killer, or null if none found
     */
    public Player getKiller();

    // Custom Potion Effects Next

    /**
     * Checks whether the mob has a line of sight to another.
     *
     * @param other - Entity to determine whether or not it has a line of sight to
     * @return - True if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(CustomEntity other);

    /**
     * Checks whether the mob has a line of sight to another.
     *
     * @param player - Player to determine whether or not it has a line of sight to
     * @return - True if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(Player player);

    /**
     * Returns if the mob despawns when far away from other players or not.
     *
     * @return - True if the mob is removed when far away from players
     */
    public boolean getRemoveWhenFarAway();

    /**
     * Sets whether or not the mob despawns when away from players.
     *
     * @param remove - The removal status
     */
    public void setRemoveWhenFarAway(boolean remove);

    /**
     * Sets a custom name for a mob, used in death messages and can be sent
     * to the client as a nametag for the mob.
     *
     * @param name - The name to set
     */
    public void setCustomName(String name);

    /**
     * Gets the custom name on a mob. If there is not a custom name, this will return null.
     *
     * @return - Name of the mob or null.
     */
    public String getCustomName();

    /**
     * Sets whether or not to display the mob's custom name client side. The name will be
     * displayed like a player's.
     *
     * @param flag - Show custom name or not
     */
    public void setCustomNameVisible(boolean flag);

    /**
     * Gets whether or not the mob's name is displayed client side.
     *
     * @return - If the custom name is displayed.
     */
    public boolean isCustomNameVisible();
}
