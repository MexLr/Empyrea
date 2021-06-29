package org.example.ataraxiawarmup.projectiletrail;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;

import java.util.Random;

public class SpiralTrail extends ProjectileTrail {

    private static final double SCALE = 0.125;
    private int ticks;
    private TrailType trailType;

    public SpiralTrail(TrailType type) {
        this.trailType = type.isSpiral() ? type : TrailType.DEFAULT;
    }

    public Runnable getRunnable() {
        return () -> {
            if (arrow.isInBlock() || arrow.isDead() || !arrow.isValid()) {
                arrow.remove();
                cancel();
            }

            World world = arrow.getWorld();
            Location arrowLocation = arrow.getLocation();

            Particle.DustOptions dust1;
            Particle.DustOptions dust2;

            if (trailType.equals(TrailType.RANDOM)) {
                Random random = new Random();
                dust1 = new Particle.DustOptions(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 1);
                dust2 = new Particle.DustOptions(Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 1);
            } else {
                dust1 = new Particle.DustOptions(trailType.getColor1(), 1);
                dust2 = new Particle.DustOptions(trailType.getColor2(), 1);
            }

            for (int i = 0; i < 3; i++) {
                double angle = ticks % 360;

                double x1 = getX(arrowLocation, angle, 0);
                double y1 = getY(arrowLocation, angle, 0);
                double z1 = getZ(arrowLocation, angle, 0);

                double x2 = getX(arrowLocation, angle, 180);
                double y2 = getY(arrowLocation, angle, 180);
                double z2 = getZ(arrowLocation, angle, 180);

                Location dustLocation1 = new Location(world, x1, y1, z1);
                Location dustLocation2 = new Location(world, x2, y2, z2);

                world.spawnParticle(Particle.REDSTONE, dustLocation1, 1, 0, 0, 0, 0, dust1);
                world.spawnParticle(Particle.REDSTONE, dustLocation2, 1, 0, 0, 0, 0, dust2);

                ticks += 10;
            }
        };
    }

    private double getX(Location arrowLocation, double angle, double offset) {
        return Math.cos(arrowLocation.getYaw() * Math.PI / 180) * Math.sin((angle + offset) * Math.PI / 180) * SCALE * getScaleFactor(arrowLocation) + arrowLocation.getX();
    }

    private double getY(Location arrowLocation, double angle, double offset) {
        return -Math.cos((angle + offset) * Math.PI / 180) * SCALE * getScaleFactor(arrowLocation) + arrowLocation.getY();
    }

    private double getZ(Location arrowLocation, double angle, double offset) {
        return -Math.sin(arrowLocation.getYaw() * Math.PI / 180) * Math.sin((angle + offset) * Math.PI / 180) * SCALE * getScaleFactor(arrowLocation) + arrowLocation.getZ();
    }

    private double getScaleFactor(Location arrowLocation) {
        return (2 - Math.abs(arrowLocation.getPitch()) / 90) * (2 - Math.abs(arrowLocation.getPitch()) / 90);
    }

}
