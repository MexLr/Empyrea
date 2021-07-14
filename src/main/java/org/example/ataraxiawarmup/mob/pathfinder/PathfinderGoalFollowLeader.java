package org.example.ataraxiawarmup.mob.pathfinder;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.example.ataraxiawarmup.Main;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathfinderGoalFollowLeader extends PathfinderGoal {
    private final EntityInsentient a; // the entity
    private EntityLiving b; // the entity to target (the leader)

    private final List<EntityInsentient> c = new ArrayList<>(); // the cooldowns
    private final int d; // the cooldown duration in ticks

    private final EntityType e; // the EntityType to target

    private final float f; // speed of the entity
    private final double g; // maximum distance from the leader

    public PathfinderGoalFollowLeader(EntityInsentient entity, EntityType type, int cooldown, float maxSpeed, double maxDistance) {
        this.a = entity;
        this.d = cooldown;
        this.e = type;
        this.f = maxSpeed;
        this.g = maxDistance;
        this.a(EnumSet.of(Type.a));
    }

    @Override
    public boolean a() {
        List<Entity> nearbyEntities = (List<Entity>) this.a.getWorld().getWorld().getNearbyEntities(new Location(this.a.getWorld().getWorld(), a.locX(), a.locY(), a.locZ()), 500D, 500D, 500D);
        for (Entity entity : nearbyEntities) {
            if (entity.getType().equals(this.e)) {
                if (entity.getCustomName().contains("Lead")) {
                    this.b = ((CraftLivingEntity) entity).getHandle();
                }
                break;
            } else {
                this.b = null;
            }
        }
        if (this.b == null) {
            return false;
        }

        if (c != null) {
            if (c.contains(a)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean b() {
        return (this.b.f(this.a) > (this.g * this.g)) && !this.a.getNavigation().m();
    }

    @Override
    public void d() {
        c.add(a);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> c.remove(a), d);
        this.b = null;
    }

    @Override
    public void c() {
        this.a.getNavigation().a(this.b.locX(), this.b.locY(), this.b.locZ(), this.f);
    }
}
