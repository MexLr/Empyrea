package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomWeapon;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.item.customitem.ItemAttribute;
import org.example.ataraxiawarmup.mob.boss.BossType;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CustomMob implements Cloneable {

    private static final Map<String, CustomMob> CUSTOM_MOB_TEMPLATES = new HashMap<>(); // for spawning in new CustomMobs
    private static final Map<Entity, CustomMob> CUSTOM_MOBS = new HashMap<>(); // for updating an existing entity with ease

    private final String name;
    private final EntityType entityType;
    private BossType bossType;
    private final List<Element> elements;
    private final int damage;
    private final int level;
    private int defense;
    private final int maxHealth;
    private final List<CustomLootTable> lootTables;
    private int health;
    private Entity entity;
    private boolean isCharged = false;
    private boolean isGripped = false;
    private boolean isCasting = false;

    private Player lastDamager;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables) {
        this(name, entityType, elements, damage, level, defense, maxHealth, lootTables, false);
    }

    public CustomMob(String name, BossType bossType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables, boolean template) {
        this(name, bossType.getEntityType(), elements, damage, level, defense, maxHealth, lootTables, template);
        this.bossType = bossType;
    }

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables, boolean template) {
        this.name = name;
        this.entityType = entityType;
        this.elements = elements;
        this.damage = damage;
        this.level = level;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.lootTables = lootTables;
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
        Entity spawnedEntity;
        if (bossType != null) {
            spawnedEntity = bossType.spawn(location);
        } else {
            spawnedEntity = location.getWorld().spawnEntity(location, entityType);
        }

        spawnedEntity.setCustomName(getCustomName());
        spawnedEntity.setCustomNameVisible(true);
        ((LivingEntity) spawnedEntity).setMaximumNoDamageTicks(0);
        if (spawnedEntity instanceof Slime || spawnedEntity instanceof MagmaCube) {
            ((Slime) spawnedEntity).setSize((int) Math.round(Math.random() + 2));
        }
        // ((LivingEntity) spawnedEntity).setAI(false);
        if (this.helmet != null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                ((LivingEntity) spawnedEntity).getEquipment().setHelmet(this.helmet);
            });
        }
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
     * Sets the defense of the mob.
     *
     * @param defense - the new defense for the mob to have
     */
    public void setDefense(int defense) {
        this.defense = defense;
    }

    /**
     * Removes defense from the mob.
     *
     * @param amount - The amount of defense to remove from the mob.
     */
    public void removeDefense(double amount) {
        this.defense -= amount;
        if (this.defense < 0) {
            this.defense = 0;
        }
    }

    /**
     * Removes a percentage of the mob's defense.
     *
     * @param percent - The percentage of the mob's defense to remove.
     */
    public void removeDefensePercentage(double percent) {
        removeDefense(this.defense * percent / 100);
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
    public void damage(int amount, Player player) {
        this.lastDamager = player;
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
        if (lootTables != null) {
            CustomPlayer lastPlayerToHit = CustomPlayer.fromPlayer(this.lastDamager);
            for (CustomLootTable lootTable : lootTables) {
                if (lootTable != null) {
                    lootTable.dropItems(this.entity.getLocation(), lastPlayerToHit.getValueOfAttribute(ItemAttribute.LOOTBONUS));
                }
            }
        }
    }

    /**
     * Get the loot table for this mob.
     *
     * @return - The mob's loot table
     */
    public List<CustomLootTable> getLootTables() {
        return lootTables;
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
     * Sets the entity's helmet.
     *
     * @param item - the item to set the helmet to
     */
    public void setHelmet(ItemStack item) {
        this.helmet = item;
    }

    /**
     * Returns if this mob is charged or not. A mob is charged from the Thunder ability.
     *
     * @return - if the mob is charged
     */
    public boolean isCharged() {
        return isCharged;
    }

    /**
     * Sets the charged state of the mob.
     *
     * @param charged - This mob's new charged status.
     */
    public void setCharged(boolean charged) {
        this.isCharged = charged;
    }

    /**
     * Returns if this mob is affected by the Grips of Chaos ability.
     *
     * @return - if the mob is gripped
     */
    public boolean isGripped() {
        return isGripped;
    }

    /**
     * Sets whether or not the mob is affected by the Grips of Chaos ability.
     *
     * @param gripped - The mob's new gripped status.
     */
    public void setGripped(boolean gripped) {
        this.isGripped = gripped;
    }

    /**
     * Returns if this mob is currently casting a spell.
     *
     * @return - If the mob is casting a spell
     */
    public boolean isCasting() {
        return isCasting;
    }

    /**
     * Sets whether or not the mob is casting a spell
     *
     * @param casting - If the mob should be casting a spell.
     */
    public void setCasting(boolean casting) {
        this.isCasting = casting;
    }

    /** Checks if the entity is invulnerable. Used for bosses/minibosses that require you to kill weaker entities first.
     *
     * @return - If the entity is invunerable
     */
    public boolean isInvulnerable() {
        if (this.bossType != null) {
            switch (this.bossType) {
                case LEADMINION:
                    List<Entity> nearbyEntities = this.entity.getNearbyEntities(20, 20, 20);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof WitherSkeleton) {
                            return true;
                        }
                    }
                    return false;
                case WITHER:
                    nearbyEntities = this.entity.getNearbyEntities(100, 20, 100);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof WitherSkeleton) {
                            return true;
                        }
                    }
                    return false;
            }
        }
        return false;
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
