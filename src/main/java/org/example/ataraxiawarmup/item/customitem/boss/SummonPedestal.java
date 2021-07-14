package org.example.ataraxiawarmup.item.customitem.boss;

import com.mojang.datafixers.types.templates.Sum;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.mob.boss.BossType;

import java.util.*;

public class SummonPedestal {

    public static final Map<ArmorStand, SummonPedestal> PEDESTAL_MAP = new HashMap<>();
    public static final Map<BossType, List<SummonPedestal>> BOSS_MAP =  new HashMap<>();

    private Location location;
    private UUID uuid;
    private ArmorStand stand;
    private BossType boss;

    public SummonPedestal(Location location, UUID uuid, int orientation, BossType type) {
        this.location = location;
        this.uuid = uuid;
        this.boss = type;

        List<SummonPedestal> pedestalList;
        if (BOSS_MAP.containsKey(type)) {
            pedestalList = BOSS_MAP.get(type);
        } else {
            pedestalList = new ArrayList<>();
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location offset;
                Location yOffset = new Location(location.getWorld(), 0, -0.32 * j, 0);

                switch (orientation) {
                    case 1:
                        offset = new Location(location.getWorld(), -0.5 * i, 0, -0.5 * j);
                        break;
                    case 2:
                        offset = new Location(location.getWorld(), 0.5 * j, 0, -0.5 * i);
                        break;
                    case 3:
                        offset = new Location(location.getWorld(), 0.5 * i, 0, 0.5 * j);
                        break;
                    case 4:
                        offset = new Location(location.getWorld(), -0.5 * j, 0, 0.5 * i);
                        break;
                    default:
                        offset = new Location(location.getWorld(), -0.5 * i, 0, -0.5, 30, 30);
                        break;
                }
                ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(offset).add(yOffset), EntityType.ARMOR_STAND);
                armorStand.setGravity(false);
                armorStand.setInvisible(true);
                armorStand.setCustomName("Pedestal");
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Stone"));
                playerHead.setItemMeta(skullMeta);
                switch (orientation) {
                    case 1:
                        armorStand.setHeadPose(new EulerAngle(1, 0, 0));
                        break;
                    case 2:
                        armorStand.setHeadPose(new EulerAngle(0, 0, -1));
                        break;
                    case 3:
                        armorStand.setHeadPose(new EulerAngle(-1, 0, 0));
                        break;
                    case 4:
                        armorStand.setHeadPose(new EulerAngle(0, 0, 1));
                        break;
                    default:
                        break;
                }
                armorStand.getEquipment().setHelmet(playerHead);
                PEDESTAL_MAP.put(armorStand, this);
            }
        }
        for (int y = 0; y < 4; y++) {
            Location offset = null;
            switch (orientation) {
                case 1:
                    offset = new Location(location.getWorld(), -0.5, -0.47 * (y + 1), 0.3 * y - 0.5);
                    break;
                case 2:
                    offset = new Location(location.getWorld(), -0.3 * y + 0.5, -0.47 * (y + 1), -0.5);
                    break;
                case 3:
                    offset = new Location(location.getWorld(), 0.5, -0.47 * (y + 1), -0.3 * y + 0.5);
                    break;
                case 4:
                    offset = new Location(location.getWorld(), 0.3 * y - 0.5, -0.47 * (y + 1), 0.5);
                    break;
                default:
                    break;
            }

            ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ()).add(offset), EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setInvisible(true);
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer("Stone"));
            playerHead.setItemMeta(skullMeta);
            switch (orientation) {
                case 1:
                    armorStand.setHeadPose(new EulerAngle(1, 0, 0));
                    break;
                case 2:
                    armorStand.setHeadPose(new EulerAngle(0, 0, -1));
                    break;
                case 3:
                    armorStand.setHeadPose(new EulerAngle(-1, 0, 0));
                    break;
                case 4:
                    armorStand.setHeadPose(new EulerAngle(0, 0, 1));
                    break;
                default:
                    break;
            }
            armorStand.getEquipment().setHelmet(playerHead);
            PEDESTAL_MAP.put(armorStand, this);
        }
        Location itemOffset; // the location for the ArmorStand that holds the item in the pedestal
        switch (orientation) {
            case 1:
                itemOffset = new Location(location.getWorld(), -0.1, 0.6, -0.9, 0, 0);
                break;
            case 2:
                itemOffset = new Location(location.getWorld(), 0.9, 0.6, -0.1, 90, 0);
                break;
            case 3:
                itemOffset = new Location(location.getWorld(), 0.1, 0.6, 0.9, 180, 0);
                break;
            case 4:
                itemOffset = new Location(location.getWorld(), -0.9, 0.6, 0.1, -90, 0);
                break;
            default:
                itemOffset = new Location(location.getWorld(), -0.5, 1, 0.5);
                break;
        }
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(new Location(location.getWorld(), location.getX() + itemOffset.getX(), location.getY() + itemOffset.getY(), location.getZ() + itemOffset.getZ(), itemOffset.getYaw(), itemOffset.getPitch()), EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setInvisible(true);
        armorStand.setCustomName("Pedestal");
        armorStand.setArms(true);
        EulerAngle angle;
        angle = new EulerAngle(-0.5, 0, 0);
        armorStand.setRightArmPose(angle);
        this.stand = armorStand;
        PEDESTAL_MAP.put(armorStand, this);
        pedestalList.add(this);
        if (BOSS_MAP.containsKey(type)) {
            BOSS_MAP.replace(type, pedestalList);
        } else {
            BOSS_MAP.put(type, pedestalList);
        }
    }

    public ItemStack getItem() {
        return this.stand.getEquipment().getItemInMainHand();
    }

    public void setItem(ItemStack item) {
        this.stand.getEquipment().setItemInMainHand(item);
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            if (canSummonBoss()) {
                spawnParticles();
            }
        });
    }

    public BossType getBoss() {
        return boss;
    }

    public Location getLocation() {
        return this.stand.getLocation();
    }

    public boolean canSummonBoss() {
        List<CustomItemStack> requiredToSummon = this.boss.getItemsToSummon();
        List<CustomItemStack> requiredItems = new ArrayList<>();
        requiredItems.addAll(requiredToSummon);
        int successes = 0;
        for (SummonPedestal pedestal : BOSS_MAP.get(this.boss)) {
            if (pedestal.getItem() == null || pedestal.getItem().getType().equals(Material.AIR)) {
                return false;
            }
            for (CustomItemStack customItemStack : requiredItems) {
                if (pedestal.getItem() == null) {
                    return false;
                }
                CustomItemStack pedestalItem = new CustomItemStack(CustomItem.fromName(pedestal.getItem().getItemMeta().getDisplayName()), pedestal.getItem().getAmount());
                if (customItemStack.isSimilar(pedestalItem)) {
                    requiredItems.remove(CustomItem.fromName(pedestal.getItem().getItemMeta().getDisplayName()).toCustomItemStack(pedestal.getItem().getAmount()));
                    successes++;
                    break;
                }
            }
        }
        if (successes >= requiredToSummon.size()) {
            return true;
        }
        return false;
    }

    private void spawnParticles() {
        final boolean[] canSpawnBoss = new boolean[1];
        Location centralLocation = new Location(this.stand.getWorld(), 3470.5, 93, -205);
        for (SummonPedestal pedestal : BOSS_MAP.get(this.boss)) {
            pedestal.setItem(new ItemStack(Material.AIR));
            new BukkitRunnable() {
                Location pedestalLocation = pedestal.getLocation();
                Location difference = pedestalLocation.clone().subtract(centralLocation.clone());
                Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
                int ticks = 0;
                @Override
                public void run() {
                    if (ticks > 30) {
                        new BukkitRunnable() {
                            int angle = 0;
                            int particles = BOSS_MAP.get(getBoss()).size();
                            @Override
                            public void run() {
                                if (angle >= 180) {
                                    canSpawnBoss[0] = true;
                                    cancel();
                                }
                                for (int i = 0; i < BOSS_MAP.get(getBoss()).size(); i++) {
                                    Location particleLocation = centralLocation.clone().add(Math.sin(angle + i * 360 / particles * Math.PI / 180), 0, Math.cos(angle + i * 360 / particles * Math.PI / 180));
                                    particleLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLocation, 1);
                                    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, dust);
                                }
                                angle += 5;
                            }
                        }.runTaskTimer(Main.getInstance(), 0, 1);
                        cancel();
                    }

                    Location particleLocation = pedestalLocation.clone().subtract(new Location(centralLocation.getWorld(), difference.getX() / 30, difference.getY() / 30, difference.getZ() / 30).multiply(ticks));
                    particleLocation.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, particleLocation, 1);
                    particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, dust);

                    ticks++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 1);
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                if (canSpawnBoss[0] == true) {
                    List<Entity> nearbyEntities = (List<Entity>) getLocation().getWorld().getNearbyEntities(centralLocation, 10, 10, 10);
                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof Player) {
                            entity.teleport(new Location(getLocation().getWorld(), 2477, 91, -197, 90, 0));
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> CustomMob.fromName(getBoss().getLevel() + getBoss().getName()).spawn(new Location(getLocation().getWorld(), 2469, 93, -197)), 10);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 10);
    }

    public static SummonPedestal fromStand(ArmorStand stand) {
        return PEDESTAL_MAP.get(stand);
    }
}
