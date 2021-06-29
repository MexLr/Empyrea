package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.example.ataraxiawarmup.item.CraftingInventory;
import org.example.ataraxiawarmup.item.CustomItem;
import org.example.ataraxiawarmup.item.CustomItemStack;
import org.example.ataraxiawarmup.item.CustomItemType;
import org.example.ataraxiawarmup.mob.CustomSkeleton;
import org.example.ataraxiawarmup.mob.CustomZombie;
import org.example.ataraxiawarmup.projectiletrail.ProjectileTrailApplierInventory;
import org.example.ataraxiawarmup.spawner.SpawnerItem;

public class CommandListener implements CommandExecutor {

    private Main plugin;

    public CommandListener(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("item").setExecutor(this);
        plugin.getCommand("spawner").setExecutor(this);
        plugin.getCommand("spawnentity").setExecutor(this);
        plugin.getCommand("trailapplier").setExecutor(this);
        plugin.getCommand("craft").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("item"))
            return handleGiveItem(sender, args);
        if (cmd.getName().equalsIgnoreCase("spawner"))
            return handleGiveSpawner(sender, args);
        if (cmd.getName().equalsIgnoreCase("spawnentity"))
            return handleSpawnEntity(sender, args);
        if (cmd.getName().equalsIgnoreCase("trailapplier"))
            return handleOpenTrailApplyInventory(sender, args);
        if (cmd.getName().equalsIgnoreCase("craft"))
            return handleOpenCraftingInventory(sender, args);
        return false;
    }

    private boolean handleGiveItem(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid item ID!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            int itemAmount = 1;
            if (args.length > 1) {
                itemAmount = Integer.parseInt(args[1]);
            }

            switch (args[0]) {
                case "SPAWNER":
                    SpawnerItem spawnerItem = new SpawnerItem(EntityType.ZOMBIE, 5);
                    player.getInventory().addItem(spawnerItem.getItem());
                default:
                    break;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();
            if (CustomItem.itemFromName(input) != null) {
                if (args.length > 1) {
                    player.getInventory().addItem(new CustomItemStack(CustomItem.itemFromName(input), Integer.parseInt(args[1])).toItemStack());
                } else {
                    player.getInventory().addItem(new CustomItemStack(CustomItem.itemFromName(input)).toItemStack());
                }
            }
        }
        return true;
    }

    private boolean handleGiveSpawner(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid EntityType!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            if (args.length < 2) {
                player.sendMessage("§cPlease specify a valid interval!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            EntityType type = EntityType.valueOf(args[0].toUpperCase());
            double interval = Double.parseDouble(args[1]);

            player.getInventory().addItem(new SpawnerItem(type, interval).getItem());
        }
        return true;
    }

    private boolean handleSpawnEntity(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid entity to spawn!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            switch (args[0]) {
                default:
                    CustomSkeleton customSkeleton = new CustomSkeleton(player.getLocation());
                    ((CraftWorld)player.getWorld()).getHandle().addEntity(customSkeleton, CreatureSpawnEvent.SpawnReason.CUSTOM);
                    break;
            }
        }
        return true;
    }

    private boolean handleOpenTrailApplyInventory(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ProjectileTrailApplierInventory applyInv = new ProjectileTrailApplierInventory(player);

            player.openInventory(applyInv.getInv());
        }
        return true;
    }

    private boolean handleOpenCraftingInventory(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CraftingInventory inv = new CraftingInventory();

            player.openInventory(inv.getInv());
        }
        return true;
    }

}
