package org.example.ataraxiawarmup.item.customitem.ability;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Cooldowns;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomWeapon;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.item.customitem.ItemAttribute;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.*;

public enum Ability {
    NONE("None", List.of("", "", ""), ChatColor.MAGIC, new HashMap<>()),
    METEOR("Meteor", List.of("Incendiary", "Infernal", "Hellish"), ChatColor.RED, Map.of(ItemAttribute.FIREDEF, 10, ItemAttribute.FIREPERCENT, 5, ItemAttribute.FIREDAMAGE, 10)),
    WATER("Water", List.of("Aquatic", "Oceanic", "Nautical"), ChatColor.AQUA, Map.of(ItemAttribute.WATERDEF, 10, ItemAttribute.WATERPERCENT, 5, ItemAttribute.WATERDAMAGE, 10)),
    EARTH("Earth", List.of("Natural", "Druidic", "Planetary"), ChatColor.DARK_GREEN, Map.of(ItemAttribute.EARTHDEF, 10, ItemAttribute.EARTHPERCENT, 5, ItemAttribute.EARTHDAMAGE, 10)),
    THUNDER("Thunder", List.of("Charged", "Electric", "Voltaic"), ChatColor.YELLOW, Map.of(ItemAttribute.THUNDERDEF, 10, ItemAttribute.THUNDERPERCENT, 5, ItemAttribute.THUNDERDAMAGE, 10)),
    AIR("Air", List.of("Aerial", "Pneumatic", "Heavenly"), ChatColor.GRAY, Map.of(ItemAttribute.AIRDEF, 10, ItemAttribute.AIRPERCENT, 5, ItemAttribute.AIRDAMAGE, 10)),
    CHAOS("Grips of Chaos", List.of("Abyssal", "Chaotic", "Apocalyptic"), ChatColor.DARK_PURPLE, Map.of(ItemAttribute.CHAOSDEF, 7, ItemAttribute.CHAOSPERCENT, 3, ItemAttribute.CHAOSDAMAGE, 7)),
    ELEMENTAL("Elemental Hex", List.of("Elemental", "Supernatural", "Climactic"), ChatColor.DARK_AQUA, Map.of(ItemAttribute.ALLDEF, 2, ItemAttribute.ALLPERCENT, 1, ItemAttribute.ALLDAMAGE, 2));

    private static final List<ChatColor> NAME_COLORS = List.of(ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.AQUA, ChatColor.BLUE, ChatColor.DARK_BLUE, ChatColor.DARK_PURPLE, ChatColor.LIGHT_PURPLE);
    private static final Map<String, Ability> NAME_MAP = new HashMap<>();

    private String name;
    private List<String> prefixes;
    private Map<ItemAttribute, Integer> attributeMap;
    private ChatColor color;

    static {
        for (Ability ability : Ability.values()) {
            NAME_MAP.put(ability.name, ability);
        }
    }

    Ability(String name, List<String> prefixes, ChatColor color, Map<ItemAttribute, Integer> attributeMap) {
        this.name = name;
        this.prefixes = prefixes;
        this.attributeMap = attributeMap;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public List<String> getPrefixes() {
        return prefixes;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPrefix(int at) {
        if (prefixes.size() > at) {
            if (this.equals(ELEMENTAL)) {
                String prefix = "";
                for (int i = 0; i < prefixes.get(at).length(); i++) {
                    prefix += NAME_COLORS.get(i % NAME_COLORS.size()) + "" + prefixes.get(at).charAt(i);
                }
                return prefix;
            }
            return prefixes.get(at);
        }
        return null;
    }

    public Map<ItemAttribute, Integer> getAttributes() {
        return attributeMap;
    }

    public void performAbility(Player player, int level) {
        switch (this) {
            case METEOR:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    CustomWeapon heldWeapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    Entity target = null;

                    List<Entity> nearbyEntities = player.getNearbyEntities(15.0, 15.0, 15.0);
                    for (int i = 0; i < nearbyEntities.size(); i++) {
                        if (nearbyEntities.get(i) instanceof LivingEntity) {
                            if (nearbyEntities.get(i) instanceof Projectile || nearbyEntities.get(i) instanceof Player || nearbyEntities.get(i) instanceof ArmorStand) {
                                continue;
                            }
                            target = nearbyEntities.get(i);
                        }
                    }

                    if (target != null) {
                        Random random = new Random();
                        int randomValue = random.nextInt(6) - 3;
                        Location fireballLocation = target.getLocation().add(new Location(player.getWorld(), randomValue, 0, randomValue));
                        Fireball fireball = (Fireball) player.getWorld().spawnEntity(fireballLocation.add(new Location(player.getWorld(), 0.0, 10.0, 0.0)), EntityType.FIREBALL);
                        Vector vector = target.getLocation().toVector().subtract(fireball.getLocation().toVector());
                        fireball.setDirection(vector);
                        fireball.setShooter(player);

                        Entity runnableTarget = target;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!fireball.isValid()) {
                                    runnableTarget.getWorld().spawnParticle(Particle.CRIMSON_SPORE, runnableTarget.getLocation(), 150, 0.5, 0.5, 0.5, 1D);
                                    runnableTarget.getWorld().spawnParticle(Particle.WARPED_SPORE, runnableTarget.getLocation(), 150, 0.5, 0.5, 0.5, 1D);
                                    runnableTarget.getWorld().spawnParticle(Particle.DRAGON_BREATH, runnableTarget.getLocation(), 150, 0.5, 0.5, 0.5, 0.25D);

                                    List<Entity> entitiesToExplode = (List<Entity>) runnableTarget.getWorld().getNearbyEntities(runnableTarget.getLocation(), 3, 3, 3);

                                    for (Entity entity : entitiesToExplode) {
                                        if (entity instanceof LivingEntity) {
                                            if (entity instanceof Player || entity instanceof ArmorStand) {
                                                continue;
                                            }
                                            player.sendMessage(entity.getCustomName());
                                            ((LivingEntity) entity).damage(0);
                                            heldWeapon.onDamageMob(player, entity, 10 * Math.pow(1.5, level));
                                        }
                                    }
                                    cancel();
                                }
                                Vector vector = runnableTarget.getLocation().toVector().subtract(fireball.getLocation().toVector());
                                fireball.setDirection(vector);
                            }
                        }.runTaskTimer(Main.getInstance(), 0, 1);
                    }
                }
                break;
            case WATER:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    Location location = player.getLocation();
                    CustomWeapon heldWeapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    List<Entity> damagedEntities = new ArrayList<>();
                    new BukkitRunnable() {
                        int radius = 2 + level;
                        int currentRadius = 1;
                        @Override
                        public void run() {
                            if (currentRadius == radius) {
                                this.cancel();
                            }
                            List<Entity> entities = (List<Entity>) player.getWorld().getNearbyEntities(location, currentRadius, currentRadius, currentRadius);
                            for (Entity entity : entities) {
                                if (damagedEntities.contains(entity)) {
                                    continue;
                                }
                                if (entity instanceof Player) {
                                    continue;
                                }
                                heldWeapon.onDamageMob(player, entity, 3 * Math.pow(1.5, level));
                                if (entity instanceof LivingEntity) {
                                    ((LivingEntity) entity).damage(0);
                                }
                                damagedEntities.add(entity);
                            }

                            // particles
                            for (int i = 0; i < 24; i++) {
                                double angle = i * 360 / 24;
                                double xOffset = Math.sin(angle * Math.PI / 180) * currentRadius;
                                double zOffset = Math.cos(angle * Math.PI / 180) * currentRadius;
                                Location particleLocation = location.clone().add(xOffset, 0, zOffset);
                                player.getWorld().spawnParticle(Particle.WATER_SPLASH, particleLocation, 3);
                                player.getWorld().spawnParticle(Particle.CRIT_MAGIC, particleLocation, 3);
                                player.getWorld().spawnParticle(Particle.WATER_DROP, particleLocation, 3);
                                player.getWorld().spawnParticle(Particle.WATER_WAKE, particleLocation, 3);
                                player.playSound(location.clone(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 0.05f, 1f);
                                player.playSound(location.clone(), Sound.ENTITY_GENERIC_SPLASH, 0.10f, 1f);
                            }

                            currentRadius++;
                        }
                    }.runTaskTimer(Main.getInstance(), 0, 5);
                }
                break;
            case EARTH:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    Location location = player.getLocation();
                    Location newLocation = location.clone();
                    while (newLocation.getBlock().getType().isAir() && newLocation.getY() > 0) {
                        newLocation.subtract(0, 1, 0);
                    }
                    newLocation.setY(Math.floor(newLocation.getY()));
                    new BukkitRunnable() {
                        int total = level * 2; // total iterations of healing, minus 1
                        int current = 0; // current iterations of healing
                        @Override
                        public void run() {
                            if (current >= total) {
                                cancel();
                            }

                            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, (int) ((double) current / total * 200), (int) ((double) current / total * 200)), 1);
                            int angle = 0;
                            for (int i = 0; i < 90; i++) {
                                angle += 4;
                                Location particleLocation = new Location(newLocation.getWorld(), newLocation.getX() + Math.sin(angle * Math.PI / 180) * 2, newLocation.getY() + 1, newLocation.getZ() + Math.cos(angle * Math.PI / 180) * 2);
                                particleLocation.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, 0, dust, true);

                            }
                            List<Entity> entities = (List<Entity>) newLocation.getWorld().getNearbyEntities(newLocation.clone().add(0, 2, 0), 2, 2, 2);
                            for (Entity entity : entities) {
                                if (entity instanceof Player) {
                                    CustomPlayer customPlayer = CustomPlayer.fromPlayer((Player) entity);
                                    customPlayer.regenHealth(1D * level);
                                }
                            }

                            current++;
                        }
                    }.runTaskTimer(Main.getInstance(), 0, 20);
                }
                break;
            case THUNDER:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    CustomWeapon heldWeapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    new BukkitRunnable() {
                        int totalEntities = 2 + level;
                        int currentEntities = 0;
                        Location location = player.getLocation();
                        Entity lastEntity = player;
                        @Override
                        public void run() {
                            boolean foundEntity = false;
                            if (currentEntities >= totalEntities) {
                                cancel();
                            }
                            List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 3 * (currentEntities + 1), 3 * (currentEntities + 1), 3 * (currentEntities + 1));
                            player.sendMessage("" + 3 * (currentEntities + 1));

                            for (Entity entity : nearbyEntities) {
                                if (entity instanceof LivingEntity) {
                                    if (entity instanceof Player || entity instanceof ArmorStand) {
                                        continue;
                                    }
                                    if (CustomMob.fromEntity(entity) != null) {
                                        if (CustomMob.fromEntity(entity).isCharged()) {
                                            continue;
                                        }
                                    } else {
                                        continue;
                                    }
                                    // an entity is found - should be the nearest one that isn't charged, but a random one works just fine, as long as it also isn't charged
                                    CustomMob.fromEntity(entity).setCharged(true); // the entity is now charged

                                    // show particles from the last entity to the next.
                                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1);
                                    Location location1 = ((LivingEntity) entity).getEyeLocation().clone();
                                    Location location2 = ((LivingEntity) lastEntity).getEyeLocation().clone();
                                    Location difference = location2.clone().subtract(location1.clone());
                                    for (int i = 0; i < 100; i++) {
                                        Location newLocation = location2.subtract(new Location(difference.getWorld(), difference.getX() / 100, difference.getY() / 100, difference.getZ() / 100));
                                        newLocation.getWorld().spawnParticle(Particle.REDSTONE, newLocation, 1, 0, 0, 0, dust);
                                    }
                                    lastEntity = entity;

                                    // create and start the particle runnable on the entity's eye location. this runnable will also cause an explosion when the entity dies.
                                    new BukkitRunnable() {
                                        int ticks = 0;
                                        @Override
                                        public void run() {
                                            if (ticks > 100 || entity.isDead()) {
                                                CustomMob.fromEntity(entity).setCharged(false);
                                                entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                                                entity.getWorld().spawnParticle(Particle.CRIMSON_SPORE, entity.getLocation(), 150, 0.5, 0.5, 0.5, 1D);
                                                entity.getWorld().spawnParticle(Particle.WARPED_SPORE, entity.getLocation(), 150, 0.5, 0.5, 0.5, 1D);
                                                entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, entity.getLocation(), 150, 0.5, 0.5, 0.5, 0.25D);

                                                List<Entity> entitiesToExplode = (List<Entity>) entity.getWorld().getNearbyEntities(entity.getLocation(), 3, 3, 3);

                                                for (Entity entity : entitiesToExplode) {
                                                    if (entity instanceof LivingEntity) {
                                                        if (entity instanceof Player || entity instanceof ArmorStand) {
                                                            continue;
                                                        }
                                                        player.sendMessage(entity.getCustomName());
                                                        ((LivingEntity) entity).damage(0);
                                                        heldWeapon.onDamageMob(player, entity, 10 * Math.pow(1.5, level));
                                                    }
                                                }
                                                cancel();
                                            }

                                            Location eyeLocation = ((LivingEntity) entity).getEyeLocation();
                                            Location locationOffset = new Location(player.getWorld(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D);
                                            Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(255, 255, 0), 1);
                                            eyeLocation.getWorld().spawnParticle(Particle.REDSTONE, eyeLocation.clone().add(locationOffset), 1, 0, 0, 0, dust);
                                            eyeLocation.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, eyeLocation.clone().add(locationOffset), 1, 0.5, 0.5, 0.5, 0.1D);
                                            eyeLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, eyeLocation.clone().add(locationOffset), 1, 0.5, 0.5, 0.5, 0D);

                                            eyeLocation.getWorld().playSound(eyeLocation, Sound.BLOCK_BEEHIVE_WORK, 0.5f + ticks / 70, 1.0f);
                                            ticks++;
                                        }
                                    }.runTaskTimer(Main.getInstance(), 0, 1);
                                    foundEntity = true; // an entity was found, so yes
                                    location = entity.getLocation(); // set the found entity's location as the location to start the next search from
                                    break; // break out of the for loop
                                }
                            }

                            // if no entity was found, therefore breaking the chain:
                            if (!foundEntity) {
                                cancel();
                            }

                            currentEntities++;
                        }
                    }.runTaskTimer(Main.getInstance(), 0, 5);
                }
                break;
            case AIR:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 10 * level, level - 1, false, false, false);
                    player.addPotionEffect(potionEffect);

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.0f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_SHOOT, 1.0f, 1.0f);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_HORSE_BREATHE, 2.0f, 1.0f);

                    Vector vector = player.getLocation().getDirection().multiply(0.5 + 0.5 * level);
                    if (vector.getY() < 0.5) {
                        vector.setY(0.5);
                    }
                    player.setVelocity(vector);
                }
                break;
            case CHAOS:
                if (Cooldowns.tryCooldown(player, "Ability", 125)) {
                    CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                    if (!customPlayer.removeAbilityCharge(2 + level)) {
                        return;
                    }

                    Location location = player.getLocation();
                    CustomWeapon heldWeapon = (CustomWeapon) CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    List<Entity> nearbyEntities = (List<Entity>) location.getWorld().getNearbyEntities(location, 2 + level, 2 + level, 2 + level);

                    for (Entity entity : nearbyEntities) {
                        if (entity instanceof LivingEntity) {
                            if (entity instanceof Player || entity instanceof ArmorStand) {
                                continue;
                            }
                            if (CustomMob.fromEntity(entity) != null) {
                                if (CustomMob.fromEntity(entity).isGripped()) {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                            // an entity is found
                            CustomMob.fromEntity(entity).setGripped(true); // the entity is now gripped

                            // create and start the particle runnable on the entity's eye location, with other necessary variable declarations.
                            // this runnable will also reduce the entity's defense by 0.1% each tick.
                            int maxTicks = (int) (10 * Math.pow(2, level));
                            new BukkitRunnable() {
                                int ticks = 0;
                                @Override
                                public void run() {
                                    if (ticks >= maxTicks) {
                                        CustomMob.fromEntity(entity).setGripped(false); // entity is no longer gripped after time runs out
                                        cancel();
                                    }
                                    if (entity.isDead()) {
                                        cancel();
                                    }
                                    Location eyeLocation = ((LivingEntity) entity).getEyeLocation();
                                    Location locationOffset = new Location(player.getWorld(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D);
                                    Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(0, 0, 0), 1);
                                    Particle.DustOptions dust2 = new Particle.DustOptions(Color.fromRGB(31, 0, 63), 1);
                                    // visual representation of being gripped by chaos. a bunch of black and purple
                                    eyeLocation.getWorld().spawnParticle(Particle.REDSTONE, eyeLocation.clone().add(locationOffset), 5, 0.1, 0.1, 0.1, dust);
                                    eyeLocation.getWorld().spawnParticle(Particle.REDSTONE, eyeLocation.clone().add(locationOffset), 5, 0.1, 0.1, 0.1, dust2);
                                    eyeLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, eyeLocation.clone().add(locationOffset), 1, 0.5, 0.5, 0.5, 0D);

                                    CustomMob.fromEntity(entity).removeDefensePercentage(0.1);

                                    ticks++;
                                }
                            }.runTaskTimer(Main.getInstance(), 0, 1);
                        }
                    }

                }
                break;
        }
    }

    public static Ability fromName(String name) {
        return NAME_MAP.get(name);
    }

}
