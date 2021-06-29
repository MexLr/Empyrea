package org.example.ataraxiawarmup;

import org.bukkit.Location;
import org.example.ataraxiawarmup.mob.CustomEntity;
import org.example.ataraxiawarmup.mob.CustomEntityType;

import java.util.Collection;
import java.util.List;

public interface CustomWorld {

    public CustomEntity spawnCustomEntity(Location loc, CustomEntityType type);

    public List<CustomEntity> getCustomEntities();

    public Collection<? extends CustomEntity> getEntitiesInChunk(int x1, int z1);
}
