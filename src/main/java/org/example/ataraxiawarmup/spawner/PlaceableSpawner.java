package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.mob.CustomLootTable;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.spawner.menu.SpawnerMenuInventory;
import org.example.ataraxiawarmup.sql.SqlGetter;
import org.example.ataraxiawarmup.sql.SqlSetter;

import java.util.*;

public class PlaceableSpawner extends Spawner {
    private static final Map<ArmorStand, PlaceableSpawner> ARMOR_STAND_MAP = new HashMap<>();

    private Location blockLocation;
    private ArmorStand armorStand;

    public PlaceableSpawner(CustomMob mob, int level, Location location, Main plugin) {
        super(mob, level, location, plugin, true);
        if (location == null) {
            return;
        }
        this.blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 2, location.getBlockZ());
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 2, 0), EntityType.ARMOR_STAND);
        //armorStand.setInvisible(true);
        armorStand.setCustomName("Spawner");
        armorStand.setGravity(false);
        ARMOR_STAND_MAP.put(armorStand, this);
    }

    public PlaceableSpawner(CustomMob mob, int level, Location location, Main plugin, int id) {
        super(mob, level, location, plugin, id);
        if (location == null) {
            return;
        }
        this.blockLocation = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 2, location.getBlockZ());
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 2, 0), EntityType.ARMOR_STAND);
        //armorStand.setInvisible(true);
        armorStand.setCustomName("Spawner");
        armorStand.setGravity(false);
        ARMOR_STAND_MAP.put(armorStand, this);
    }

    public void remove(boolean breakBlock) {
        SqlSetter setter = new SqlSetter();
        // remove from the chunk map
        Chunk chunkIsIn = getLocation().getChunk();
        List<Spawner> spawnersInChunk = fromChunk(chunkIsIn);
        spawnersInChunk.remove(this);
        // turn off the spawner
        setActive(false);
        setter.removeSpawner(this);
        armorStand.remove();
        if (breakBlock) {
            // break the block of the spawner and drop the spawner's item
            Block block = this.blockLocation.getBlock();
            block.breakNaturally(new ItemStack(Material.AIR));
            ItemStack droppedItem = toItem();
            this.blockLocation.getWorld().dropItemNaturally(getLocation().clone().add(0, 2, 0), droppedItem);
        }
    }

    @Override
    public void levelUp() {
        remove(false);
        PlaceableSpawner newSpawner = new PlaceableSpawner(getMobType(), getLevel() + 1, getLocation(), Main.getInstance(), getId());
        newSpawner.startSpawning();
        newSpawner.readdToDatabase();
    }

    public void onDisable() {
        this.armorStand.remove();
    }

    @Override
    public void unload() {
        if (getEntity() != null) {
            getEntity().remove();
            setEntity(null);
        }
        ARMOR_STAND_MAP.remove(armorStand);
        armorStand.remove();
        setActive(false);
    }

    @Override
    public void load() {
        setActive(true);
        armorStand = (ArmorStand) getLocation().getWorld().spawnEntity(getLocation().clone().add(0, 2, 0), EntityType.ARMOR_STAND);
        //armorStand.setInvisible(true);
        armorStand.setCustomName("Spawner");
        armorStand.setGravity(false);
        ARMOR_STAND_MAP.put(armorStand, this);
    }

    public SpawnerMenuInventory getGUI(Player player) {
        return new SpawnerMenuInventory(this, player);
    }

    public List<CustomItemStack> getItemsToLevelUp() {
        List<CustomItemStack> returnedItems = new ArrayList<>();
        List<CustomItemStack> mobDropItems = new ArrayList<>();
        for (CustomLootTable lootTable : getMobType().getLootTables()) {
            mobDropItems.addAll(lootTable.getItems());
        }
        for (CustomItemStack item : mobDropItems) {
            int itemAmount = 0;
            itemAmount = item.getAmount() * (getLevel() * 5 + 1) == 0 ? 1 : item.getAmount() * (getLevel() * 5 + 1);
            returnedItems.add(new CustomItemStack(item.getItem(), itemAmount));
        }
        return returnedItems;
    }

    public ItemStack[] getLevelUpItemstacks() {
        List<ItemStack> returnedItems = new ArrayList<>();
        List<CustomItemStack> levelUpItems = getItemsToLevelUp();
        for (CustomItemStack item : levelUpItems) {
            returnedItems.add(item.toItemStack());
        }
        return returnedItems.toArray(new ItemStack[0]);
    }

    public Location getBlockLocation() {
        return blockLocation;
    }

    public static PlaceableSpawner fromArmorStand(ArmorStand stand) {
        return ARMOR_STAND_MAP.get(stand);
    }

    public static Collection<PlaceableSpawner> getSpawners() {
        return ARMOR_STAND_MAP.values();
    }
}
