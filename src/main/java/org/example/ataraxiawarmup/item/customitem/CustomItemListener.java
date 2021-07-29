package org.example.ataraxiawarmup.item.customitem;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

public class CustomItemListener implements Listener {

    private Main plugin;

    public CustomItemListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicks(PlayerInteractEvent event) {

        if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        if (CustomItem.fromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()) == null) {
            return;
        }
        CustomItem heldItem = CustomItem.fromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());

        if (heldItem instanceof CustomShortbow) {
            if (((CustomWeapon) heldItem).hasAbility()) {
                String itemName = heldItem.getItemMeta().getDisplayName();
                itemName = itemName.replace(itemName.split(" ")[0], "").trim();
                heldItem = CustomItem.fromName(itemName);
            }
        }

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            heldItem.onUseLeft(event.getPlayer());
        }

        heldItem = CustomItem.fromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            heldItem.onUseRight(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDamagesMob(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        CustomMob customMob = CustomMob.fromEntity(event.getEntity());
        if (customMob != null) {
            if (customMob.isInvulnerable()) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ((LivingEntity) event.getEntity()).setNoDamageTicks(0);
            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                return;
            }
            if (CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()) != null) {
                CustomItem heldItem = CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                if (heldItem instanceof CustomWeapon) {
                    if (heldItem instanceof CustomSword) {
                        ((CustomWeapon) heldItem).onDamageMob(player, event.getEntity(), 1.0D);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHitsMob(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        CustomMob customMob = CustomMob.fromEntity(event.getEntity());
        if (customMob != null) {
            if (customMob.isInvulnerable()) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (((Entity)projectile.getShooter()).getType().equals(EntityType.PLAYER)) {
                Player player = (Player) projectile.getShooter();
                if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                    return;
                }
                if (CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()) != null) {
                    CustomItem heldItem = CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                    if (heldItem instanceof CustomBow) {
                        if (projectile instanceof Arrow) {
                            double blocksTraveled = (int) event.getEntity().getLocation().distance(((Player) projectile.getShooter()).getLocation());
                            double multi = (blocksTraveled * 5) / 100 + 1;
                            Bukkit.getPlayer("MexLr").sendMessage("multi: " + multi);
                            ((CustomBow) heldItem).onArrowHitsMob(player, event.getEntity(), multi);
                        }
                    }
                    if (heldItem instanceof CustomShortbow) {
                        if (projectile instanceof Arrow) {
                            ((CustomShortbow) heldItem).onArrowHitsMob(player, event.getEntity());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShootsBow(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof AbstractArrow && event.getEntity().getShooter() instanceof Player) {
            ItemStack arrows = new ItemStack(Material.ARROW, 64);
            ItemMeta arrowMeta = arrows.getItemMeta();
            arrowMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            arrowMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            arrows.setItemMeta(arrowMeta);
            ((Player) event.getEntity().getShooter()).getInventory().setItem(8, arrows);
        }
    }

    @EventHandler
    public void onPlayerThrowsPearl(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEquipsArmor(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        if (event.getSlot() > 35 && event.getSlot() < 40)
        {
            Bukkit.getScheduler().runTask(plugin, () -> {
                customPlayer.updateAttributes();
            });
        }
        if (event.getCurrentItem() != null) {
            if (!event.getCurrentItem().getType().equals(Material.AIR)) {
                CustomItem customItem = CustomItem.fromName(event.getCurrentItem().getItemMeta().getDisplayName());
                if (customItem != null) {
                    if (customItem instanceof CustomArmor) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            customPlayer.updateAttributes();
                        });
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRightClicksWithArmor(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        if (event.getPlayer().getInventory().getItemInMainHand() != null) {
            if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                CustomItem customItem = CustomItem.fromName(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                if (customItem != null) {
                    if (customItem instanceof CustomArmor) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            customPlayer.updateAttributes();
                        });
                    } else {
                        if (customItem instanceof CustomWeapon) {

                        } else {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEatsFood(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerSwitchesItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        Bukkit.getScheduler().runTask(plugin, () -> {
            customPlayer.updateAttributes();
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) {
                player.getInventory().setItem(8, CustomItem.fromName("menu").toItemStack());
                return;
            }
            String itemName = event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getDisplayName();
            if (CustomItem.fromName(itemName) != null) {
                if (CustomItem.fromName(itemName) instanceof CustomBow) {
                    ItemStack arrows = new ItemStack(Material.ARROW, 64);
                    ItemMeta arrowMeta = arrows.getItemMeta();
                    arrowMeta.addEnchant(Enchantment.DURABILITY, 0, true);
                    arrowMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
                    arrows.setItemMeta(arrowMeta);
                    player.getInventory().setItem(8, arrows);
                } else {
                    player.getInventory().setItem(8, CustomItem.fromName("menu").toItemStack());
                }
            }
        });
    }

    @EventHandler
    public void onExplosionKillsItem(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().clear();
    }
}
