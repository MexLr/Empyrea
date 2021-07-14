package org.example.ataraxiawarmup.mob;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalArrowAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CustomSkeleton extends EntitySkeleton {

    public CustomSkeleton(World world) {
        super(EntityTypes.aB, world);
    }

    public CustomSkeleton(Location loc) {
        super(EntityTypes.aB, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        super.initPathfinder();

        this.bP.a(1, new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F));
    }
}
