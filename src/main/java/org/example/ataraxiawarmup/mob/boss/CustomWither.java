package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalCastSpell;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalFollowTarget;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalSummonMinion;
import org.example.ataraxiawarmup.mob.spell.Spell;

public class CustomWither extends EntityWither {

    public CustomWither(Location loc) {
        super(EntityTypes.aZ, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        // run these a tick later so the entity actually exists in CustomMob.ENTITY_MAP
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            this.bP.a(3, new PathfinderGoalSummonMinion(this, BossType.WITHERMINION, 400, BossType.WITHER));
            this.bP.a(4, new PathfinderGoalCastSpell(this, Spell.CHAOSFIRERAIN, false, 300, 400, EntityType.WITHER, true));
            this.bP.a(5, new PathfinderGoalCastSpell(this, Spell.CHAOSLIGHTNING, false, 200, 200, EntityType.PLAYER, true));
            this.bP.a(6, new PathfinderGoalCastSpell(this, Spell.DEATHRAY, false, 800, 800, EntityType.PLAYER, true));
            this.bP.a(8, new PathfinderGoalFollowTarget(this, 0.6, 5, 0));
        });
        this.bP.a(11, new PathfinderGoalFloat(this));
        this.bP.a(12, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));

    }
}
