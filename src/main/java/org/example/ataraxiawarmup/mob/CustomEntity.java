package org.example.ataraxiawarmup.mob;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.example.ataraxiawarmup.CustomWorld;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public interface CustomEntity {

    /**
     * Gets the entity's current position
     *
     * @return - a copy of the entity's location
     */
    public Location getLocation();

    /**
     * Stores the entity's location into the provided Location object.
     *
     * @param loc - The location object being modified.
     * @return - the Location object provided or null
     */
    public Location getLocation(Location loc);

    /**
     * Sets the entity's velocity.
     *
     * @param velocity - new velocity to use
     */
    public void setVelocity(Vector velocity);

    /**
     * Gets the entity's velocity.
     *
     * @return
     */
    public Vector getVelocity();

    /**
     * Gets the current world the entity is in.
     *
     * @return - World
     */
    public World getWorld();

    /**
     * Gets the current CustomWorld the entity is in.
     * <p>
     *     Used for housing all CustomEntities
     * </p>
     * @return - The custom world the entity is in
     */
    public CustomWorld getCustomWorld();

    /**
     * Teleports the entity to a given location.
     *
     * @param location - the location to teleport the entity to
     * @return - <code>true</code> if the teleport was successful
     */
    public boolean teleport(Location location);

    /**
     * Teleports this entity to the given CustomEntity.
     *
     * @param teleportTo - destination CustomEntity to teleport to
     * @return - <code>true</code> if the teleport was successful
     */
    public boolean teleport(CustomEntity teleportTo);

    /**
     * Gets a list of entities within a bounding box centered on this entity.
     *
     * @param x - 1/2 the size of the box on the x axis
     * @param y - 1/2 the size of the box on the y axis
     * @param z - 1/2 the size of the box on the z axis
     * @return - a List<CustomEntity> of nearby CustomEntities
     */
    public List<CustomEntity> getNearbyEntities(double x, double y, double z);

    /**
     * Gets a list of entities within a bounding box (sphere) centered on this entity.
     *
     * @param radius - 1/2 the size of the sphere on all axes
     * @return
     */
    public List<CustomEntity> getNearbyEntities(double radius);

    /**
     * Returns a unique id for this entity.
     *
     * @return Entity id
     */
    public int getEntityId();

    /**
     * Remove the entity.
     */
    public void remove();

    /**
     * Returns true if the entity has been removed/marked for removal.
     *
     * @return - True if the entity is dead.
     */
    public boolean isDead();

    /**
     * Returns false if the entity is dead/has been despawned.
     *
     * @return - True if valid.
     */
    public boolean isValid();

    /**
     * Gets the Server that contains this entity.
     *
     * @return - The Server instance housing this entity.
     */
    public Server getServer();

    /**
     * Returns this distance that this entity has fallen.
     *
     * @return - The distance
     */
    public float getFallDistance();

    /**
     * Sets the fall distance for this entity
     *
     * @param distance - The new distance
     */
    public void setFallDistance(float distance);

    /**
     * Gets a unique and persistent id for this entity.
     *
     * @return - Unique id
     */
    public UUID getUniqueId();

    /**
     * Get the type of entity.
     *
     * @return The entity type.
     */
    public CustomEntityType getType();
}
