package org.example.ataraxiawarmup.mob;

public interface Damageable extends CustomEntity {

    /**
     * Deals the amount of damage given to this entity.
     *
     * @param amount - The amount of damage to deal
     */
    void damage(double amount);

    /**
     * Deals the amount of damage given to this entity, from another specified entity.
     *
     * @param amount - The amount of damage to deal
     * @param source - Entity that caused this damage
     */
    void damage(double amount, CustomEntity source);

    /**
     * Gets the entity's current health from 0 to {@link #getMaxHealth()}, 0 being dead.
     *
     * @return - Health represented from 0 to max
     */
    double getHealth();

    /**
     * Sets the entity's current health fro 0 to {@link #getMaxHealth()}, 0 being dead.
     *
     * @param health - New health represented from 0 to max
     * @throws IllegalArgumentException - Thrown if the health is < 0 or > {@link #getMaxHealth()}
     */
    void setHealth(double health);

    /**
     * Gets the maximum health of this entity.
     *
     * @return - Maximum health
     */
    double getMaxHealth();

    /**
     * Sets the maximum health this entity can have.
     * <p>
     *     If the health of the entity is above the value provided, it will be set to that value.
     *
     * @param maxHealth - Amount to set the maximum health to
     */
    void setMaxHealth(double maxHealth);

    /**
     * Resets this entity's max health to its original value.
     */
    void resetMaxHealth();
}
