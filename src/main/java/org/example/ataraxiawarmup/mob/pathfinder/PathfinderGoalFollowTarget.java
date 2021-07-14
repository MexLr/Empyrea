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
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathfinderGoalFollowTarget extends PathfinderGoal {

    private final EntityInsentient a; // the wither
    private EntityLiving b; // the target

    private final double f; // the speed
    private final double g; // maintained distance between wither and target
    private final CustomMob h; // the CustomMob instance of the entity
    private final int i; // the cooldown before following again
    private final List<EntityInsentient> j = new ArrayList<>(); // the entities on cooldown

    private double c; // x
    private double d; // y
    private double e; // z

    public PathfinderGoalFollowTarget(EntityInsentient a, double speed, float distance, int cooldown) {
        this.a = a;
        this.f = speed;
        this.g = distance;
        this.h = CustomMob.fromEntity(a.getBukkitEntity());
        this.i = cooldown;
        this.a(EnumSet.of(Type.d));
    }

    @Override
    public boolean a() {
        List<Entity> nearbyEntities = (List<Entity>) this.a.getWorld().getWorld().getNearbyEntities(new Location(this.a.getWorld().getWorld(), a.locX(), a.locY(), a.locZ()), 50D, 50D, 50D);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                this.b = ((CraftPlayer) entity).getHandle();
                break;
            }
        }
        if (this.b == null) { // if the target is null
            return false;
        }
        if (this.h == null) {
            return false;
        }
        if (this.h.isCasting()) {
            return false;
        }
        if (this.j != null) {
            if (this.j.contains(a)) {
                return false;
            }
        }
        this.c = this.b.locX();
        this.d = this.b.locY();
        this.e = this.b.locZ();
        return true;
    }

    @Override
    public boolean b() {
        return (this.b.f(this.a) > (this.g * this.g)) && !h.isCasting() && !this.a.getNavigation().m();
    }

    @Override
    public void c() {
        this.a.getNavigation().a(this.b.locX(), this.b.locY(), this.b.locZ(), this.f);
    }

    @Override
    public void d() {
        if (i > 0) {
            j.add(a);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                j.remove(a);
            }, i);
        }
        this.b = null;
    }
}
