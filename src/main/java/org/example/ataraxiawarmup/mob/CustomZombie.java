package org.example.ataraxiawarmup.mob;

import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CustomZombie extends EntityZombie {

    public CustomZombie(World world) {
        super(world);
    }
    public CustomZombie(Location loc) {
        super(((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        super.initPathfinder();

        this.bO.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0f));
    }

}
