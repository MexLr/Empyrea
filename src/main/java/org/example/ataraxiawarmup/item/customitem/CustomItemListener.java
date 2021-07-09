package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.example.ataraxiawarmup.Main;

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
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ((LivingEntity) event.getEntity()).setNoDamageTicks(0);
            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                return;
            }
            if (CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()) != null) {
                CustomItem heldItem = CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                if (heldItem instanceof CustomWeapon) {
                    if (heldItem instanceof CustomBow) {

                    } else {
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
                            ((CustomBow) heldItem).onArrowHitsMob(player, event.getEntity());
                        }
                        if (projectile instanceof Fireball) {
                            ((CustomWeapon) heldItem).onFireballHitsMob(player, event.getEntity());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerThrowsPearl(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            event.setCancelled(true);
        }
    }

}
