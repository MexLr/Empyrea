package org.example.ataraxiawarmup.mob;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.level.World;

public class CustomCreeper extends EntityCreeper {

    public CustomCreeper(World world) {
        super(EntityTypes.o, world);
    }
}
