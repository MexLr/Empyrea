package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.boss.wither.EntityWither;
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

public class SpellTester extends EntityWither {
    public SpellTester(Location loc) {
        super(EntityTypes.aZ, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
    }

    public void initPathfinder() {
        // run these a tick later so the entity actually exists in CustomMob.ENTITY_MAP
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            this.bP.a(4, new PathfinderGoalCastSpell(this, Spell.DEATHRAY, false, 300, 300, EntityType.WITHER, true));
        });
    }
}
