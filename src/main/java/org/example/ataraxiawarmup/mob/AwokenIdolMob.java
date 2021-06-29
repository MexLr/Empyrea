package org.example.ataraxiawarmup.mob;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.CustomWorld;
import org.bukkit.util.Vector;

import java.util.*;

public class AwokenIdolMob implements AwokenIdol {
    private Mob target;
    private Location location;
    private Vector velocity;
    private World world;
    private CustomEntityType entityType;
    private int entityId;
    private CustomWorld customWorld;

    public void draw() {
        // Head
        Location offsetLocation = new Location(world, 0D, 1.7D, 0D);
        Location headLocation = location.add(offsetLocation);
        BlockData blockData = Bukkit.createBlockData(Material.CHISELED_STONE_BRICKS);
        FallingBlock head = world.spawnFallingBlock(headLocation, blockData);
        head.setVelocity(new Vector());
    }

    @Override
    public void setTarget(Mob target) {
        this.target = target;
    }

    @Override
    public Mob getTarget() {
        return this.target;
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public Location getLocation(Location loc) {
        return loc == null ? null : loc.zero().add(loc);
    }

    @Override
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    @Override
    public Vector getVelocity() {
        return this.velocity;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public CustomWorld getCustomWorld() {
        return this.customWorld;
    }

    @Override
    public boolean teleport(Location location) {
        this.location = location;
        return true;
    }

    @Override
    public boolean teleport(CustomEntity teleportTo) {
        this.location = teleportTo.getLocation();
        return true;
    }

    @Override
    public List<CustomEntity> getNearbyEntities(double x, double y, double z) {
        List<CustomEntity> entityList = new ArrayList<CustomEntity>();

        int smallX = (int) Math.floor((location.getX() - x) / 16.0D);
        int bigX = (int) Math.floor((location.getX() - x) / 16.0D);
        int smallZ = (int) Math.floor((location.getZ() - z) / 16.0D);
        int bigZ = (int) Math.floor((location.getZ() - z) / 16.0D);

        for (int x1 = smallX; x1 < bigX; x1++) {
            for (int z1 = smallZ; x1 < bigZ; z1++) {
                if (world.isChunkLoaded(x1, z1))
                    entityList.addAll(customWorld.getEntitiesInChunk(x1, z1));
            }
        }

        Iterator<CustomEntity> customEntityIterator = entityList.iterator();
        while (customEntityIterator.hasNext()) {
            if (customEntityIterator.next().getLocation().distanceSquared(location) > y * y)
                customEntityIterator.remove();
        }
        return entityList;
    }

    @Override
    public List<CustomEntity> getNearbyEntities(double radius) {
        List<CustomEntity> entityList = new ArrayList<CustomEntity>();

        int smallX = (int) Math.floor((location.getX() - radius) / 16.0D);
        int bigX = (int) Math.floor((location.getX() - radius) / 16.0D);
        int smallZ = (int) Math.floor((location.getZ() - radius) / 16.0D);
        int bigZ = (int) Math.floor((location.getZ() - radius) / 16.0D);

        for (int x = smallX; x < bigX; x++) {
            for (int z = smallZ; x < bigZ; z++) {
                if (world.isChunkLoaded(x, z))
                    entityList.addAll(customWorld.getEntitiesInChunk(x, z));
            }
        }

        Iterator<CustomEntity> customEntityIterator = entityList.iterator();
        while (customEntityIterator.hasNext()) {
            if (customEntityIterator.next().getLocation().distanceSquared(location) > radius * radius)
                customEntityIterator.remove();
        }
        return entityList;
    }

    @Override
    public int getEntityId() {
        return 0;
    }

    @Override
    public void remove() {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public float getFallDistance() {
        return 0;
    }

    @Override
    public void setFallDistance(float distance) {

    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public CustomEntityType getType() {
        return null;
    }

    @Override
    public void damage(double amount) {

    }

    @Override
    public void damage(double amount, CustomEntity source) {

    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public void setHealth(double health) {

    }

    @Override
    public double getMaxHealth() {
        return 0;
    }

    @Override
    public void setMaxHealth(double maxHealth) {

    }

    @Override
    public void resetMaxHealth() {

    }

    @Override
    public double getEyeHeight() {
        return 0;
    }

    @Override
    public Location getEyeLocation() {
        return null;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        return 0;
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {

    }

    @Override
    public double getLastDamage() {
        return 0;
    }

    @Override
    public void setLastDamage(double damage) {

    }

    @Override
    public int getNoDamageTicks() {
        return 0;
    }

    @Override
    public void setNoDamageTicks(int ticks) {

    }

    @Override
    public Player getKiller() {
        return null;
    }

    @Override
    public boolean hasLineOfSight(CustomEntity other) {
        return false;
    }

    @Override
    public boolean hasLineOfSight(Player player) {
        return false;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        return false;
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {

    }

    @Override
    public void setCustomName(String name) {

    }

    @Override
    public String getCustomName() {
        return null;
    }

    @Override
    public void setCustomNameVisible(boolean flag) {

    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }
}
