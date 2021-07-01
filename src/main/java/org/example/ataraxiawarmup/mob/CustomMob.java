package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomMob implements Cloneable {

    private static final Map<String, CustomMob> CUSTOM_MOB_TEMPLATES = new HashMap<>(); // for spawning in new CustomMobs
    private static final Map<Entity, CustomMob> CUSTOM_MOBS = new HashMap<>(); // for updating an existing entity with ease

    private final String name;
    private final EntityType entityType;
    private final List<Element> elements;
    private final int damage;
    private final int level;
    private final int defense;
    private final int maxHealth;
    private final CustomLootTable lootTable;
    private int health;
    private Entity entity;

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, CustomLootTable lootTable) {
        this(name, entityType, elements, damage, level, defense, maxHealth, lootTable, false);
    }

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, CustomLootTable lootTable, boolean template) {
        this.name = name;
        this.entityType = entityType;
        this.elements = elements;
        this.damage = damage;
        this.level = level;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.lootTable = lootTable;
        if (template) {
            CUSTOM_MOB_TEMPLATES.put(ChatColor.stripColor(level + name.toLowerCase()), this);
        }
    }

    /**
     * Executes when the mob attacks a player.
     *
     * @param player - The player being attacked
     */
    public abstract void onAttackPlayer(Player player);

    /**
     * Executes when the mob damages a player.
     *
     * @param player - The player being damaged
     */
    public abstract void onDamagePlayer(Player player);

    /**
     * Spawns the mob at the given location.
     *
     * @param location - Location to spawn the mob at
     */
    public void spawn(Location location) {
        Entity spawnedEntity = location.getWorld().spawnEntity(location, entityType);
        spawnedEntity.setCustomName(getCustomName());
        spawnedEntity.setCustomNameVisible(true);
        ((LivingEntity) spawnedEntity).setMaximumNoDamageTicks(0);
        ((LivingEntity) spawnedEntity).setAI(false);
        this.entity = spawnedEntity;
        CUSTOM_MOBS.put(this.entity, this);
    }

    /**
     * Gets the entity representing the CustomMob.
     *
     * @return - The entity representing the CustomMob, or null if there isn't any
     */
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Gets the name of the mob/mob type.
     *
     * @return - The name of the mob's type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the EntityType of the mob.
     *
     * @return - The EntityType of the mob
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Gets all of the mob's elements.
     *
     * @return - The mob's elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Gets the damage the mob deals.
     *
     * @return - The damage that the mob deals
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Gets the level of the mob.
     *
     * @return - The level of the mob
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the defense of the mob.
     *
     * @return - The defense of the mob
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Gets the max health of the mob.
     *
     * @return - The mob's max health
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Gets the current health of the mob.
     *
     * @return - The current health of the mob
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the current health of the mob.
     *
     * @param health - The new health of the mob
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Deals the specified amount of damage tawo the mob.
     *
     * @param amount - The amount of damage that should be dealt
     */
    public void damage(int amount) {
        int newHealth = getHealth() - amount;
        if (newHealth < 0) {
            newHealth = 0;
        }
        setHealth(newHealth);
        updateCustomName();
        if (newHealth == 0) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                remove();
            });
        }
    }

    /**
     * Kills the entity and removes it from the CUSTOM_MOBS map.
     */
    protected void remove() {
        ((Damageable) this.entity).setHealth(0);
        CUSTOM_MOBS.remove(this.entity);
        if (lootTable != null) {
            lootTable.dropItems(this.entity.getLocation());
        }
    }

    /**
     * Get the loot table for this mob.
     *
     * @return - The mob's loot table
     */
    public CustomLootTable getLootTable() {
        return lootTable;
    }

    /**
     * Gets the mob's custom name, which is a combination of the level, name, types, and health.
     *
     * @return The mob's custom/display name
     */
    public String getCustomName() {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.WHITE + "Lv. ").append(level).append(" ").append(name).append(" ");
        for (Element element : elements) {
            builder.append(element.getColoredChar());
        }
        builder.append(" " + ChatColor.RED).append((int) health).append('♥');
        return builder.toString();
    }

    /**
     * Updates the mob's custom name.
     */
    protected void updateCustomName() {
        entity.setCustomName(getCustomName());
    }

    @Override
    public CustomMob clone() {
        try {
            CustomMob customMob = (CustomMob) super.clone();

            return customMob;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /**
     * Gets a template for a CustomMob by name and level
     *
     * @param name - The name of the CustomMob that is being searched for, preceded by the mob's level
     * @return - The CustomMob, or null if there is no CustomMob with the name
     */
    public static CustomMob fromName(String name) {
        if (CUSTOM_MOB_TEMPLATES.get(ChatColor.stripColor(name).toLowerCase()) == null) {
            return null;
        }
        return CUSTOM_MOB_TEMPLATES.get(ChatColor.stripColor(name).toLowerCase()).clone();
    }

    /**
     * Gets an existing CustomMob by its entity
     *
     * @param entity - The entity of the CustomMob that is being searched for
     * @return - The CustomMob, or null if there is no existing CustomMob with the entity
     */
    public static CustomMob fromEntity(Entity entity) {
        return CUSTOM_MOBS.get(entity);
    }
}
