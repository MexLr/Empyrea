package org.example.ataraxiawarmup;

import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.item.customitem.*;
import org.example.ataraxiawarmup.item.customitem.ability.AbilityApplyingInventory;
import org.example.ataraxiawarmup.menu.MenuInventory;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.mob.spell.Spell;
import org.example.ataraxiawarmup.party.Party;
import org.example.ataraxiawarmup.player.Chat;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.player.PlayerHomeGenerator;
import org.example.ataraxiawarmup.projectiletrail.ProjectileTrailApplierInventory;
import org.example.ataraxiawarmup.shop.OrderRotator;
import org.example.ataraxiawarmup.shop.ShopInventory;
import org.example.ataraxiawarmup.song.NBSPlayer;
import org.example.ataraxiawarmup.spawner.InvisibleSpawner;
import org.example.ataraxiawarmup.spawner.PlaceableSpawner;
import org.example.ataraxiawarmup.spawner.SpawnerItem;

import java.util.UUID;

public class CommandListener implements CommandExecutor {

    private Main plugin;

    public CommandListener(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("item").setExecutor(this);
        plugin.getCommand("spawner").setExecutor(this);
        plugin.getCommand("spawnentity").setExecutor(this);
        plugin.getCommand("trailapplier").setExecutor(this);
        plugin.getCommand("craft").setExecutor(this);
        plugin.getCommand("ingredientsfor").setExecutor(this);
        plugin.getCommand("viewrecipe").setExecutor(this);
        plugin.getCommand("applyability").setExecutor(this);
        plugin.getCommand("spell").setExecutor(this);
        plugin.getCommand("warp").setExecutor(this);
        plugin.getCommand("startsong").setExecutor(this);
        plugin.getCommand("createspawner").setExecutor(this);
        plugin.getCommand("calccombat").setExecutor(this);
        plugin.getCommand("menu").setExecutor(this);
        plugin.getCommand("combatlevel").setExecutor(this);
        plugin.getCommand("home").setExecutor(this);
        plugin.getCommand("shop").setExecutor(this);
        plugin.getCommand("playerhead").setExecutor(this);
        plugin.getCommand("party").setExecutor(this);
        plugin.getCommand("chat").setExecutor(this);
        plugin.getCommand("rotateorders").setExecutor(this);
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
        if (cmd.getName().equalsIgnoreCase("ingredientsfor"))
            return handleListIngredientsForRecipe(sender, args);
        if (cmd.getName().equalsIgnoreCase("viewrecipe"))
            return handleViewRecipe(sender, args);
        if (cmd.getName().equalsIgnoreCase("applyability"))
            return handleOpenApplyAbilityInventory(sender, args);
        if (cmd.getName().equalsIgnoreCase("spell"))
            return handleUseSpell(sender, args);
        if (cmd.getName().equalsIgnoreCase("warp"))
            return handleWarpLocation(sender, args);
        if (cmd.getName().equalsIgnoreCase("startsong"))
            return handleStartSong(sender, args);
        if (cmd.getName().equalsIgnoreCase("createspawner"))
            return handleCreateSpawner(sender, args);
        if (cmd.getName().equalsIgnoreCase("calccombat"))
            return handleCalcCombat(sender, args);
        if (cmd.getName().equalsIgnoreCase("menu"))
            return handleOpenMenu(sender, args);
        if (cmd.getName().equalsIgnoreCase("combatlevel"))
            return handleSetCombatLevel(sender, args);
        if (cmd.getName().equalsIgnoreCase("home"))
            return handleHomeCommand(sender, args);
        if (cmd.getName().equalsIgnoreCase("shop"))
            return handleOpenShop(sender, args);
        if (cmd.getName().equalsIgnoreCase("playerhead"))
            return handleGivePlayerHead(sender, args);
        if (cmd.getName().equalsIgnoreCase("party"))
            return handleParty(sender, args);
        if (cmd.getName().equalsIgnoreCase("chat"))
            return handleChangeChat(sender, args);
        if (cmd.getName().equalsIgnoreCase("rotateorders"))
            return handleRotateOrders(sender, args);
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
            if (args.length > 1 && !args[0].equalsIgnoreCase("all")) {
                itemAmount = Integer.parseInt(args[1]);
            }

            switch (args[0]) {
                case "SPAWNER":
                    SpawnerItem spawnerItem = new SpawnerItem(CustomMob.fromName("1Spider"), 5, 1);
                    player.getInventory().addItem(spawnerItem.toItemStack());
                    break;
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

            if (CustomItem.fromName(input) != null) {
                if (args.length > 1) {
                    player.getInventory().addItem(new CustomItemStack(CustomItem.fromName(input), Integer.parseInt(args[1])).toItemStack());
                } else {
                    player.getInventory().addItem(new CustomItemStack(CustomItem.fromName(input)).toItemStack());
                }
            }

            if (input.equalsIgnoreCase("all")) {
                String input2 = ".";
                if (args.length > 1) {
                    input2 = args[1];
                    String[] splitInput2 = input2.split("_");
                    input2 = "";
                    for (String word : splitInput2) {
                        input2 += " " + word;
                    }
                    input2 = input2.trim();
                }
                for (CustomItem item : CustomItem.CUSTOM_ITEMS.values()) {
                    if (input2 == ".") {
                        if (item instanceof CustomWeapon) {
                            if (!((CustomWeapon) item).hasAbility()) {
                                player.getWorld().dropItemNaturally(player.getLocation(), new CustomItemStack(item).toItemStack());
                            }
                        } else {
                            player.getWorld().dropItemNaturally(player.getLocation(), new CustomItemStack(item).toItemStack());
                        }
                    } else {
                        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).contains(input2)) {
                            if (item instanceof CustomWeapon) {
                                if (!((CustomWeapon) item).hasAbility()) {
                                    player.getWorld().dropItemNaturally(player.getLocation(), new CustomItemStack(item).toItemStack());
                                }
                            } else {
                                player.getWorld().dropItemNaturally(player.getLocation(), new CustomItemStack(item).toItemStack());
                            }
                        }
                    }

                }
            }
        }
        return true;
    }

    private boolean handleGiveSpawner(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid CustomEntityType!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            if (args.length < 2) {
                player.sendMessage("§cPlease specify a valid interval!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();

            CustomMob type = CustomMob.fromName(input);
            if (type == null) {
                player.sendMessage("§cPlease specify a valid CustomMob!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            int level = Integer.parseInt(args[1]);
            if (level <= 0 || level > 7) {
                player.sendMessage("§cThat's not a valid level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            PlaceableSpawner spawner = new PlaceableSpawner(type, level, null, Main.getInstance(), 0);
            player.getInventory().addItem(spawner.toItem());
        }
        return true;
    }

    private boolean handleSpawnEntity(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid mob to spawn!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            if (args.length < 2) {
                player.sendMessage("§cPlease specify a valid level of the mob!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();

            CustomMob spawnedMob = CustomMob.fromName(args[1] + input);
            Location location = player.getLocation();

            if (input.equalsIgnoreCase("The Wither's Minions")) {
                CustomMob minion1 = CustomMob.fromName("35The Wither's Minion");
                CustomMob minion2 = CustomMob.fromName("35The Wither's Lead Minion");
                CustomMob minion3 = CustomMob.fromName("35The Wither's Minion");

                Vector vector1 = location.getDirection().clone().subtract(new Vector(-location.getDirection().getZ(), 0, location.getDirection().getX())).multiply(3).setY(0);
                Vector vector2 = location.getDirection().clone().subtract(new Vector(location.getDirection().getZ(), 0, -location.getDirection().getX())).multiply(3).setY(0);
                Location location1 = location.clone().subtract(vector1);
                Location location2 = location.clone().subtract(vector2);
                minion1.spawn(location1);
                minion2.spawn(location);
                minion3.spawn(location2);
                return false;
            }

            if (spawnedMob == null) {
                player.sendMessage("§cThat's not a valid entity and/or level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            spawnedMob.spawn(location);
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

    private boolean handleListIngredientsForRecipe(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage("§cPlease specify the name of an item to view the ingredients for!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();

            CustomItem itemFor = CustomItem.fromName(input);
            if (itemFor == null) {
                player.sendMessage("§cPlease specify a valid item! Item names with spaces are separated by underscores");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            if (itemFor.getRecipe() == null) {
                player.sendMessage("§cPlease specify an item that has a recipe!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            CustomRecipe recipe = itemFor.getRecipe();

            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("total")) {
                    player.sendMessage(recipe.getTotalIngredientsAsString());
                } else {
                    player.sendMessage(recipe.getIngredientsAsString());
                }
            } else {
                player.sendMessage(recipe.getIngredientsAsString());
            }
        }
        return true;
    }

    private boolean handleViewRecipe(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage("§cPlease specify the name of an item to view the recipe for!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();

            CustomItem itemFor = CustomItem.fromName(input);
            if (itemFor == null) {
                player.sendMessage("§cPlease specify a valid item! Item names with spaces are separated by underscores");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            if (itemFor.getRecipe() == null) {
                player.sendMessage("§cPlease specify an item that has a recipe!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            RecipeInventory recipeInventory = new RecipeInventory(itemFor);
            player.openInventory(recipeInventory.getViewRecipeInventory());
        }
        return true;
    }

    private boolean handleOpenApplyAbilityInventory(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        AbilityApplyingInventory inventory = new AbilityApplyingInventory();
        player.openInventory(inventory.getInventory());
        return true;
    }

    private boolean handleUseSpell(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Spell.CHAOSVEXSPAWN.use(CustomMob.fromName("1Spider"), player.getLocation(), true, player);
        return true;
    }
    private boolean handleWarpLocation(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage("§cPlease specify a location to warp to!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            switch (args[0].toUpperCase()) {
                case "BOSS_ARENA":
                    player.teleport(new Location(player.getWorld(), 2477, 91, -197, 90, 0));
                    break;
                case "WITHER_SPAWN":
                    player.teleport(new Location(player.getWorld(), 3470, 91, -201, 180, 0));
                    break;
                case "GOLEM_SPAWN":
                    player.teleport(new Location(player.getWorld(), 4481, 91, -1217, 180, 0));
                    break;
                case "HOME":
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (customPlayer != null) {
                        player.teleport(customPlayer.getPrivateAreaLocation());
                    }
                    break;
                case "FIRE":
                    player.teleport(new Location(player.getWorld(), -20000, 69, 10000, 90, 0));
                    break;
                case "WATER":
                    player.teleport(new Location(player.getWorld(), -10000, 69, 10000, 90, 0));
                    break;
                case "EARTH":
                    player.teleport(new Location(player.getWorld(), 0, 69, 10000, 90, 0));
                    break;
                case "THUNDER":
                    player.teleport(new Location(player.getWorld(), 10000, 69, 10000, 90, 0));
                    break;
                case "AIR":
                    player.teleport(new Location(player.getWorld(), 20000, 69, 10000, 90, 0));
                    break;
                case "CHAOS":
                    player.teleport(new Location(player.getWorld(), 30000, 69, 10000, 90, 0));
                    break;
                case "ELEMENTAL":
                    player.teleport(new Location(player.getWorld(), 50000, 69, 50000, 90, 0));
                    break;
            }
        }
        return true;
    }

    private boolean handleStartSong(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        NBSPlayer nbsPlayer = new NBSPlayer("eidolonboss2");
        nbsPlayer.startSong(player);
        return true;
    }

    private boolean handleCreateSpawner(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length < 1) {
                player.sendMessage("§cPlease specify a valid mob to spawn!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            if (args.length < 2) {
                player.sendMessage("§cPlease specify a valid level of the mob!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            if (args.length < 3) {
                player.sendMessage("§cPlease specify a valid level of the spawner!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            String input = args[0];
            String[] splitInput = input.split("_");
            input = "";
            for (String word : splitInput) {
                input += " " + word;
            }
            input = input.trim();

            CustomMob spawnerMob = CustomMob.fromName(args[1] + input);

            if (spawnerMob == null) {
                player.sendMessage("§cThat's not a valid entity and/or level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }

            int level = Integer.parseInt(args[2]);
            if (level <= 0 || level > 7) {
                player.sendMessage("§cThat's not a valid level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            InvisibleSpawner spawner = new InvisibleSpawner(spawnerMob, level, player.getLocation(), plugin, false);
            spawner.startSpawning();
        }
        return true;
    }

    private boolean handleCalcCombat(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
            if (args.length < 1) {
                player.sendMessage("§cPlease specify a combat level to calculate the exp of from the previous level! (Not total exp!)");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            int level = Integer.parseInt(args[0]);
            if (level < 1) {
                player.sendMessage("§cPlease specify a valid combat level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            player.sendMessage("§3Combat exp from level §9" + (level - 1) + " §3to combat level §9" + level + "§3:" + customPlayer.calcCombatToLevel(level));
        }
        return true;
    }

    private boolean handleOpenMenu(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(new MenuInventory(player).getInv());
        }
        return true;
    }

    private boolean handleSetCombatLevel(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
            if (args.length < 1) {
                player.sendMessage("§cPlease specify a combat level to set yours to!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            int level = Integer.parseInt(args[0]);
            if (level < 0) {
                player.sendMessage("§cPlease specify a valid combat level!");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                return false;
            }
            customPlayer.setCombatLevel(level);
        }
        return true;
    }

    private boolean handleHomeCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
            Location startingLocation = customPlayer.getPrivateAreaLocation();
            PlayerHomeGenerator generator = customPlayer.getGenerator();
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("Generate")) {
                    generator.generate();
                }
                if (args[0].equalsIgnoreCase("Reset")) {
                    generator.reset();
                }
            }
        }
        return true;
    }

    private boolean handleOpenShop(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (args.length < 1) {
                Inventory inventory = ShopInventory.getInventory("Orders");
                ((Player) sender).openInventory(inventory);
                return false;
            }
            String inventoryName = args[0].toUpperCase();
            Inventory inventory = ShopInventory.getInventory(inventoryName);
            ((Player) sender).openInventory(inventory);
        }
        return true;
    }

    private boolean handleGivePlayerHead(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            UUID uuid = Bukkit.getOfflinePlayer("TrustMeNotAnAlt").getUniqueId();
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            head.setItemMeta(meta);
            ((Player) sender).getInventory().addItem(head);
        }
        return true;
    }

    private boolean handleParty(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if (customPlayer.getInviteFrom() != null) {
                        if (customPlayer.getInviteFrom().getParty().getMembers().contains(customPlayer)) {
                            player.sendMessage(ChatColor.RED + "You are already in that player's party!");
                            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                            return false;
                        }
                        customPlayer.getInviteFrom().getParty().addPlayer(customPlayer);
                        player.sendMessage(ChatColor.GOLD + "You joined " + customPlayer.getInviteFrom().getPlayer().getName() + "'s party!");
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    Party party = customPlayer.getParty();
                    if (party == null) {
                        party = new Party(customPlayer);
                    }
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Please specify a player to invite to the party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    CustomPlayer invitedPlayer = CustomPlayer.fromPlayer(Bukkit.getPlayer(args[1]));
                    if (customPlayer.getParty().getMembers().contains(invitedPlayer)) {
                        player.sendMessage(ChatColor.RED + "That player is already in your party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    party.invitePlayer(invitedPlayer);
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (customPlayer.getParty() == null) {
                        player.sendMessage(ChatColor.RED + "You are not currently in a party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    String leaderName = customPlayer.getParty().getLeader().getPlayer().getName();
                    player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "-------------------------");
                    player.sendMessage(ChatColor.BLUE + "Party Leader: " + ChatColor.YELLOW + leaderName);
                    String memberString = ChatColor.BLUE + "Members: ";
                    for (CustomPlayer customPlayer1 : customPlayer.getParty().getMembers()) {
                        memberString += customPlayer1.getPlayer().getName() + "  ";
                    }
                    player.sendMessage(memberString);
                    player.sendMessage(ChatColor.BLACK + "" + ChatColor.BOLD + "-------------------------");
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (customPlayer.getParty() == null) {
                        player.sendMessage(ChatColor.RED + "You are not currently in a party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    customPlayer.getParty().kickPlayer(customPlayer);
                } else if (args[0].equalsIgnoreCase("transfer")) {
                    if (customPlayer.getParty() == null) {
                        player.sendMessage(ChatColor.RED + "You are not currently in a party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (customPlayer.getParty().getLeader() != customPlayer) {
                        player.sendMessage(ChatColor.RED + "You are not the leader of this party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Please specify a player to transfer leadership of this party to!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    CustomPlayer transferredPlayer = CustomPlayer.fromPlayer(Bukkit.getPlayer(args[1]));
                    if (!customPlayer.getParty().getMembers().contains(transferredPlayer)) {
                        player.sendMessage(ChatColor.RED + "That player is not in your party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    customPlayer.getParty().transferLeader(transferredPlayer);
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (customPlayer.getParty() == null) {
                        player.sendMessage(ChatColor.RED + "You are not currently in a party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (customPlayer.getParty().getLeader() != customPlayer) {
                        player.sendMessage(ChatColor.RED + "You are not the leader of this party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (args.length < 2) {
                        player.sendMessage(ChatColor.RED + "Please specify a player to kick from the party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (Bukkit.getPlayer(args[1]) == null) {
                        player.sendMessage(ChatColor.RED + "That player is not online!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    CustomPlayer kickedPlayer = CustomPlayer.fromPlayer(Bukkit.getPlayer(args[1]));
                    if (!customPlayer.getParty().getMembers().contains(kickedPlayer)) {
                        player.sendMessage(ChatColor.RED + "That player is not in your party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    customPlayer.getParty().kickPlayer(kickedPlayer);
                } else if (args[0].equalsIgnoreCase("disband")) {
                    if (customPlayer.getParty() == null) {
                        player.sendMessage(ChatColor.RED + "You are not currently in a party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    if (customPlayer.getParty().getLeader() != customPlayer) {
                        player.sendMessage(ChatColor.RED + "You are not the leader of this party!");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    customPlayer.getParty().disband();
                }
            }
        }
        return true;
    }

    private boolean handleChangeChat(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);

            if (args.length > 0) {
                switch (args[0].toUpperCase()) {
                    case "ALL":
                    case "A":
                        if (customPlayer.getChat().equals(Chat.ALL)) {
                            player.sendMessage("§cYou are already in this chat.");
                            return false;
                        }
                        player.sendMessage(ChatColor.GOLD + "You are now in " + ChatColor.AQUA + "ALL" + ChatColor.GOLD + " chat.");
                        customPlayer.setChat(Chat.ALL);
                        break;
                    case "PARTY":
                    case "P":
                        if (customPlayer.getParty() == null) {
                            player.sendMessage("§cYou are not in a party!");
                            return false;
                        }
                        if (customPlayer.getChat().equals(Chat.PARTY)) {
                            player.sendMessage("§cYou are already in this chat.");
                            return false;
                        }
                        player.sendMessage(ChatColor.GOLD + "You are now in " + ChatColor.AQUA + "PARTY" + ChatColor.GOLD + " chat.");
                        customPlayer.setChat(Chat.PARTY);
                        break;
                }
            }

        }
        return true;
    }

    private boolean handleRotateOrders(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            OrderRotator rotator = new OrderRotator();
            rotator.rotate();
        }
        return true;
    }

}
