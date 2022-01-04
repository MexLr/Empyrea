package org.example.ataraxiawarmup.mob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.item.customitem.ItemAttribute;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.*;

public abstract class CustomMob implements Cloneable {

    private static final Map<String, CustomMob> CUSTOM_MOB_TEMPLATES = new HashMap<>(); // for spawning in new CustomMobs
    private static final Map<Entity, CustomMob> CUSTOM_MOBS = new HashMap<>(); // for updating an existing entity with ease
    private static final List<CustomMob> CUSTOM_MOB_LIST = new ArrayList<>(); // for spawners

    private final String name;
    private final EntityType entityType;
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

    private double experience;

    private Player lastDamager;
    private List<UUID> damagers = new ArrayList<>();

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables, double experience) {
        this(name, entityType, elements, damage, level, defense, maxHealth, lootTables, false, experience);
    }

    public CustomMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables, boolean template, double experience) {
        this.name = name;
        this.entityType = entityType;
        this.elements = elements;
        this.damage = damage;
        this.level = level;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.lootTables = lootTables;
        this.experience = experience;
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
        spawnedEntity = location.getWorld().spawnEntity(location, entityType);

        spawnedEntity.setCustomName(getCustomName());
        spawnedEntity.setCustomNameVisible(true);
        ((LivingEntity) spawnedEntity).setMaximumNoDamageTicks(0);
        if (spawnedEntity instanceof Ageable) {
            ((Ageable) spawnedEntity).setAdult();
        }
        if (spawnedEntity instanceof PiglinAbstract) {
            ((PiglinAbstract) spawnedEntity).setImmuneToZombification(true);
        }
        if (spawnedEntity instanceof Slime || spawnedEntity instanceof MagmaCube) {
            ((Slime) spawnedEntity).setSize((int) Math.round(Math.random() + 2));
        }
        // ((LivingEntity) spawnedEntity).setAI(false);
        if (this.helmet != null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                ((LivingEntity) spawnedEntity).getEquipment().setHelmet(this.helmet);
            });
        }
        if (this.chestplate != null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                ((LivingEntity) spawnedEntity).getEquipment().setChestplate(this.chestplate);
            });
        }
        if (this.leggings != null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                ((LivingEntity) spawnedEntity).getEquipment().setLeggings(this.leggings);
            });
        }
        if (this.boots != null) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                ((LivingEntity) spawnedEntity).getEquipment().setBoots(this.boots);
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
     * Gets the combat experience that this mob gives when killed.
     *
     * @return - The combat experience that this mob gives on death
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Deals the specified amount of damage to the mob.
     *
     * @param amount - The amount of damage that should be dealt
     */
    public void damage(int amount, Player player) {
        if (player != null) {
            this.lastDamager = player;
            if (!this.damagers.contains(player.getUniqueId())) {
                this.damagers.add(player.getUniqueId());
            }
        }
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
            for (UUID uuid : damagers) {
                for (CustomLootTable lootTable : lootTables) {
                    if (lootTable != null) {
                        lootTable.dropItems(this.entity.getLocation(), CustomPlayer.fromPlayer(Bukkit.getPlayer(uuid)).getValueOfAttribute(ItemAttribute.LOOTBONUS), uuid);
                    }
                }
                if (Bukkit.getPlayer(uuid) != null) {
                    if (CustomPlayer.fromPlayer(Bukkit.getPlayer(uuid)) != null) {
                        CustomPlayer.fromPlayer(Bukkit.getPlayer(uuid)).getCombatExpFromMob(this);
                    }
                }
            }
            if (damagers.size() == 0) {
                List<Entity> nearbyPlayers = this.entity.getNearbyEntities(10, 10, 10);
                Player player = null;
                for (Entity entity : nearbyPlayers) {
                    if (entity instanceof Player) {
                        player = (Player) entity;
                        break;
                    }
                }
                if (player != null) {
                    if (CustomPlayer.fromPlayer(player) != null) {
                        for (CustomLootTable lootTable : lootTables) {
                            if (lootTable != null) {
                                lootTable.dropItems(this.entity.getLocation(), CustomPlayer.fromPlayer(player).getValueOfAttribute(ItemAttribute.LOOTBONUS), player.getUniqueId());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the loot table(s) for this mob.
     *
     * @return - The mob's loot table(s)
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
     * Sets the entity's chestplate.
     *
     * @param item - the item to set the chestplate to
     */
    public void setChestplate(ItemStack item) {
        this.chestplate = item;
    }

    /**
     * Sets the entity's leggings.
     *
     * @param item - the item to set the leggings to
     */
    public void setLeggings(ItemStack item) {
        this.leggings = item;
    }

    /**
     * Sets the entity's boots.
     *
     * @param item - the item to set the boots to
     */
    public void setBoots(ItemStack item) {
        this.boots = item;
    }

    public void setEquipment(ItemStack... items) {
        if (items.length == 4) {
            for (int i = 0; i < 4; i++) {
                ItemStack itemStack = items[i];
                if (items[i] != null) {
                    if (i == 0) {
                        setHelmet(itemStack);
                    }
                    if (i == 1) {
                        setChestplate(itemStack);
                    }
                    if (i == 2) {
                        setLeggings(itemStack);
                    }
                    if (i == 3) {
                        setBoots(itemStack);
                    }
                }
            }
        }
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

    public static List<CustomMob> getCustomMobs() {
        return CUSTOM_MOB_LIST;
    }
}
