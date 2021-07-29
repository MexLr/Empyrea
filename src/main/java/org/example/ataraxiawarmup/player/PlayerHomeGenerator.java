package org.example.ataraxiawarmup.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerHomeGenerator {

    private Location location;
    private List<Block> blocks = new ArrayList<>();

    public PlayerHomeGenerator(Location location) {
        this.location = location;
    }

    public void generate() {
        for (int x = 0; x < 6; x++) {
            for (int z = 0; z < 6; z++) {
                Location blockLocation = location.clone().add(x - 3, -1, z - 3);
                Block block = blockLocation.getBlock();
                block.setType(Material.GRASS_BLOCK);
                blocks.add(block);
            }
        }
        Location chestLocation = location.clone().add(2, 0, 2);
        Location chestLocation2 = location.clone().add(1, 0, 2);

        Block block = chestLocation.getBlock();
        block.setType(Material.CHEST);
        Chest left = (Chest) block.getBlockData();
        left.setFacing(BlockFace.NORTH);
        left.setType(Chest.Type.RIGHT);
        block.setBlockData(left);

        Block block2 = chestLocation2.getBlock();
        block2.setType(Material.CHEST);
        Chest right = (Chest) block2.getBlockData();
        right.setFacing(BlockFace.NORTH);
        right.setType(Chest.Type.LEFT);
        block2.setBlockData(right);

        blocks.add(block);
        blocks.add(block2);

        Inventory chestInventory = ((org.bukkit.block.Chest) block.getState()).getInventory();
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i, new ItemStack(Material.GRASS_BLOCK, 64));
        }
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i + 9, new ItemStack(Material.CHEST, 64));
        }
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i + 18, new ItemStack(Material.HOPPER, 64));
        }
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i + 27, new ItemStack(Material.GRASS_BLOCK, 64));
        }
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i + 36, new ItemStack(Material.CHEST, 64));
        }
        for (int i = 0; i < 9; i++) {
            chestInventory.setItem(i + 45, new ItemStack(Material.HOPPER, 64));
        }
    }

    public void reset() {
        for (Block block : blocks) {
            block.breakNaturally(null);
        }
    }

}
