package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.sql.SqlSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvisibleSpawner extends Spawner {

    public InvisibleSpawner(CustomMob mob, int level, Location location, Main plugin, boolean isBlock) {
        super(mob, level, location, plugin, true);

    }

    public InvisibleSpawner(CustomMob mob, int level, Location location, Main plugin, int id) {
        super(mob, level, location, plugin, id);
    }
}
