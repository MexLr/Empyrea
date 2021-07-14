package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;

public class ShootWitherSkull extends MobSpell {
    public ShootWitherSkull(double damage, List<Element> elements) {
        super(damage, elements, Spell.SHOOTWITHERSKULL);
    }

    @Override
    void onUse(CustomMob user, Location location, boolean setCasting, Entity target) {
        if (setCasting) {
            user.setCasting(true);
        }
        Location thisLocation = user.getEntity().getLocation().clone();
        Location targetLocation = location.clone();
        WitherSkull skull = ((ProjectileSource) user.getEntity()).launchProjectile(WitherSkull.class, targetLocation.clone().toVector().subtract(thisLocation.clone().toVector()).normalize().divide(new Vector(10.0, 10.0, 10.0)));
    }
}
