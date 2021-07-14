package org.example.ataraxiawarmup.mob.pathfinder;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.mob.boss.BossType;

import java.util.ArrayList;
import java.util.List;

public class PathfinderGoalSummonMinion extends PathfinderGoal {
    private final EntityInsentient a; // the entity

    private final List<EntityInsentient> b = new ArrayList<>(); // entities on cooldown
    private final int c; // cooldown of using this ability in ticks

    private final CustomMob d; // CustomMob instance of the entity
    private final BossType e; // the minion to summon

    private final BossType f; // the BossType of this boss specifically

    private boolean y = false; // if the boss should spawn its 50% health minions
    private boolean z = false; // if the boss has spawned its 50% health minions

    public PathfinderGoalSummonMinion(EntityInsentient entity, BossType type, int cooldown, BossType thisType) {
        this.a = entity;
        this.c = cooldown;
        this.d = CustomMob.fromEntity(a.getBukkitEntity());
        this.e = type;
        this.f = thisType;
    }

    @Override
    public boolean a() {
        if (this.a.getHealth() <= this.a.getMaxHealth() / 2 && !z && !y) {
            this.y = true;
            this.z = true;
            return true;
        }
        if (this.b != null) {
            if (this.b.contains(a)) {
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

    }

    @Override
    public void c() {
        Location location = new Location(this.a.getWorld().getWorld(), this.a.locX(), this.a.locY(), this.a.locZ());
        Location offset = new Location(location.getWorld(), Math.random() * 6 - 3, 0, Math.random() * 6 - 3);
        location.add(offset);
        Location newLocation = location.clone();
        while (newLocation.getBlock().getType().isAir() && newLocation.getY() > 0) {
            newLocation.subtract(0, 1, 0);
        }
        newLocation.setY(Math.floor(newLocation.getY() + 1));
        if (y) {
            switch (this.f) {
                case WITHER:
                    CustomMob minion1 = CustomMob.fromName("35The Wither's Minion");
                    CustomMob minion2 = CustomMob.fromName("35The Wither's Lead Minion");
                    CustomMob minion3 = CustomMob.fromName("35The Wither's Minion");

                    minion1.spawn(newLocation);
                    minion2.spawn(newLocation);
                    minion3.spawn(newLocation);
                    break;
            }
            this.y = false;
        } else {
            String mobName = this.e.getLevel() + this.e.getName();
            CustomMob mob = CustomMob.fromName(mobName);
            if (mob != null) {
                mob.spawn(newLocation);
                b.add(a);
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    b.remove(a);
                }, c);
            }
        }
    }
}
