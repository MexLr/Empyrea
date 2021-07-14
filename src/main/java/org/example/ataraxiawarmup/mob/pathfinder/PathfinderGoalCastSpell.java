package org.example.ataraxiawarmup.mob.pathfinder;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.mob.spell.MobSpell;
import org.example.ataraxiawarmup.mob.spell.Spell;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PathfinderGoalCastSpell extends PathfinderGoal {

    private final EntityInsentient a; // the mob casting the spell
    private EntityLiving b; // the target

    private final Spell c; // the spell being cast

    private List<EntityInsentient> d = new ArrayList<>(); // cooldown until the entity can use the spell again

    private final CustomMob e; // the CustomMob instance of the entity.
    private final World f; // the world of the entity

    private final boolean g; // whether or not the location should be offset

    private final int h; // the highest number the spell cooldown can be
    private final int i; // the lowest number the spell cooldown can be

    private final EntityType j; // EntityType to target

    private final boolean k; // whether or not the entity should be set to casting

    public PathfinderGoalCastSpell(EntityInsentient a, Spell spell, boolean offset, int lowerCooldown, int higherCooldown, EntityType type, boolean setCasting) {
        this.a = a;
        this.c = spell;
        this.e = CustomMob.fromEntity(a.getBukkitEntity());
        this.f = a.getWorld();
        this.g = offset;
        this.h = higherCooldown;
        this.i = lowerCooldown;
        this.j = type;
        this.k = setCasting;

        this.a(EnumSet.of(Type.d));
    }

    @Override
    public boolean a() {
        List<Entity> nearbyEntities = (List<Entity>) this.f.getWorld().getNearbyEntities(new Location(this.f.getWorld(), a.locX(), a.locY(), a.locZ()), 15D, 15D, 15D);
        for (Entity entity : nearbyEntities) {
            if (entity.getType().equals(this.j)) {
                this.b = ((CraftLivingEntity) entity).getHandle();
                break;
            } else {
                this.b = null;
            }
        }
        if (this.b == null) {
            return false;
        }

        if (e.isCasting()) {
            return false;
        }

        if (d != null) {
            if (d.contains(a)) {
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
        Location location = new Location(b.getWorld().getWorld(), b.locX(), b.locY(), b.locZ());
        if (g) {
            Location offset = new Location(location.getWorld(), Math.random() * 6 - 3, 0, Math.random() * 6 - 3);
            location.add(offset);
        }
        c.use(CustomMob.fromEntity(a.getBukkitEntity()), location, this.k, this.b.getBukkitEntity());
        d.add(a);
        int cooldown = (int) Math.random() * (h - i + 1) + i;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> d.remove(a), cooldown);
    }
}
