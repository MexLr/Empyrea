package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;

public class BasicTrail extends ProjectileTrail {

    private TrailType trailType;

    public BasicTrail(TrailType type) {
        this.trailType = type.isBasic() ? type : TrailType.DEFAULT;
    }

    public Runnable getRunnable() {
        return () -> {
            if (arrow.isInBlock() || arrow.isDead() || !arrow.isValid()) {
                arrow.remove();
                cancel();
            }

            World world = arrow.getWorld();

            Particle.DustOptions dust1 = new Particle.DustOptions(trailType.getColor1(), 1);
            Particle.DustOptions dust2 = new Particle.DustOptions(trailType.getColor2(), 1);

            world.spawnParticle(Particle.REDSTONE, arrow.getLocation(), 1, 0, 0, 0, 0, dust1);
            world.spawnParticle(Particle.REDSTONE, arrow.getLocation(), 1, 0, 0, 0, 0, dust2);
        };
    }

}
