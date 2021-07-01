package org.example.ataraxiawarmup.mob;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CustomCreeper extends EntityCreeper {

    public CustomCreeper(World world) {
        super(EntityTypes.o, world);
    }

    public CustomCreeper(Location loc) {
        super(EntityTypes.o, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        super.initPathfinder();
    }
}
