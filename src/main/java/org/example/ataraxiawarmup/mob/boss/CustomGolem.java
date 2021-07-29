package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalCastSpell;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalFollowTarget;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalSummonMinion;
import org.example.ataraxiawarmup.mob.spell.Spell;

public class CustomGolem extends EntityIronGolem {
    public CustomGolem(Location loc) {
        super(EntityTypes.P, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        super.initPathfinder();
        // run these a tick later so the entity actually exists in CustomMob.ENTITY_MAP
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            this.bP.a(2, new PathfinderGoalFollowTarget(this, 1.5, 5, 0));
        });
        this.bP.a(11, new PathfinderGoalFloat(this));
        this.bP.a(12, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

    }
}
