package org.example.ataraxiawarmup.mob.pathfinder;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathfinderGoalShootWitherSkull extends PathfinderGoal {

    private final EntityInsentient a; // the entity shooting the skull
    private EntityLiving b; // the target

    private final List<EntityInsentient> c = new ArrayList<>(); // cooldowns
    private final float d; // target distance
    private final int e; // cooldown in ticks

    private final EntityType f; // the type of entity to target

    private final CustomMob g; // the CustomMob instance of the target

    public PathfinderGoalShootWitherSkull(EntityInsentient entity, EntityType entityToTarget, float distance, int cooldown) {
        this.a = entity;
        this.d = distance;
        this.e = cooldown;
        this.f = entityToTarget;
        this.g = CustomMob.fromEntity(a.getBukkitEntity());

        this.a(EnumSet.of(Type.b));
    }

    @Override
    public boolean a() {
        List<Entity> nearbyEntities = (List<Entity>) this.a.getWorld().getWorld().getNearbyEntities(new Location(this.a.getWorld().getWorld(), a.locX(), a.locY(), a.locZ()), d, d, d);
        for (Entity entity : nearbyEntities) {
            if (entity.getType().equals(this.f)) {
                this.b = ((CraftLivingEntity) entity).getHandle();
                break;
            } else {
                this.b = null;
            }
        }
        if (this.b == null) {
            return false;
        }

        if (g.isCasting()) {
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
        return false;
    }

    @Override
    public void d() {
        this.b = null;
    }

    @Override
    public void c() {
        Location thisLocation = new Location(this.a.getWorld().getWorld(), this.a.locX(), this.a.locY(), this.a.locZ());
        Location targetLocation = new Location(this.b.getWorld().getWorld(), this.b.locX(), this.b.locY(), this.b.locZ());
        Location offset = new Location(targetLocation.getWorld(), Math.random() * 2 - 1, 0, Math.random() * 2 - 1);
        targetLocation.add(offset);
        WitherSkull skull = ((ProjectileSource) this.a.getBukkitEntity()).launchProjectile(WitherSkull.class, targetLocation.clone().toVector().subtract(thisLocation.clone().toVector()).normalize().divide(new Vector(10.0, 10.0, 10.0)));
        c.add(a);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> c.remove(a), e);
    }
}
