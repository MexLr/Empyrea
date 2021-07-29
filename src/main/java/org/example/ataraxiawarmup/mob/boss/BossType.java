package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.song.NBSPlayer;

import java.lang.reflect.Constructor;
import java.util.List;

public enum BossType {

    SPELLTESTER("Spell Tester", 100, EntityType.WITHER, SpellTester.class, List.of()),
    WITHER("The Wither", 75, EntityType.WITHER, CustomWither.class, List.of()),
    WITHERMINION("The Wither's Minion", 35, EntityType.WITHER_SKELETON, WitherMinion.class, List.of()),
    LEADMINION("The Wither's Lead Minion", 35, EntityType.WITHER_SKELETON, LeadWitherMinion.class, List.of()),
    GOLEM("The Flaming Golem", 20, EntityType.IRON_GOLEM, CustomGolem.class, List.of());

    private String name;
    private int level;
    private EntityType entityType;
    private Class<? extends EntityInsentient> clazz;
    private List<CustomItemStack> itemsToSummon;

    BossType(String name, int level, EntityType entityType, Class<? extends EntityInsentient> clazz, List<CustomItemStack> toSummon) {
        this.name = name;
        this.level = level;
        this.entityType = entityType;
        this.clazz = clazz;
        this.itemsToSummon = toSummon;
    }

    public Entity spawn(Location location) {
        try {
            Constructor constructor;
            constructor = this.clazz.getConstructor(new Class[]{Location.class});
            EntityInsentient entity = (EntityInsentient) constructor.newInstance(location);
            ((CraftWorld) location.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
            // start boss song
            for (Entity nearbyEntity : entity.getBukkitEntity().getNearbyEntities(50, 50, 50)) {
                if (nearbyEntity instanceof Player) {
                    NBSPlayer nbsPlayer = new NBSPlayer("eidolonboss1");
                    nbsPlayer.startSong((Player) nearbyEntity);
                }
            }
            return entity.getBukkitEntity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public List<CustomItemStack> getItemsToSummon() {
        return itemsToSummon;
    }

    public void setItemsToSummon(List<CustomItemStack> itemsToSummon) {
        this.itemsToSummon = itemsToSummon;
    }
}
