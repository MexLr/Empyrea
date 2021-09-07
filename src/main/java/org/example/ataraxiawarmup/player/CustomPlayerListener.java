package org.example.ataraxiawarmup.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.sql.SqlSetter;

import java.util.List;

public class CustomPlayerListener implements Listener {
    private Main plugin;

    public CustomPlayerListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoins(PlayerJoinEvent event) {
        CustomPlayer player = new CustomPlayer(event.getPlayer());
        event.getPlayer().getInventory().setItem(8, CustomItem.fromName("Menu").toItemStack());
        event.getPlayer().setFoodLevel(20);
    }

    @EventHandler
    public void onPlayerLeaves(PlayerQuitEvent event) {
        SqlSetter setter = new SqlSetter();
        CustomPlayer player = CustomPlayer.fromPlayer(event.getPlayer());
        if (player != null) {
            setter.addPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerLosesHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerRegensNaturally(EntityRegainHealthEvent event) {
        if (event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakesFallDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                CustomPlayer customPlayer = CustomPlayer.fromPlayer((Player) event.getEntity());
                int fallDistance = (int) event.getEntity().getFallDistance();
                double damage = 0.01 + (fallDistance * fallDistance) / 1000D;
                damage *= customPlayer.getMaxHealth();
                customPlayer.damage(damage, List.of(Element.EARTH));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerGainsEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getNewEffect() == null) {
                return;
            }
            if (event.getNewEffect().getType() == null) {
                return;
            }
            if (event.getNewEffect().getType().equals(PotionEffectType.WITHER)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerGainsExp(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerDamagesPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof AbstractArrow) {
                if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                    event.getDamager().remove();
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerClicksMenu(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().hasItemMeta()) {
                if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("menu")) {
                    event.setCancelled(true);
                }
                if (event.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DURABILITY) && event.getCurrentItem().getType().equals(Material.ARROW)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropsMenu(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().hasItemMeta()) {
            if (ChatColor.stripColor(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()).equalsIgnoreCase("menu")) {
                event.setCancelled(true);
            }
        }
    }
}
