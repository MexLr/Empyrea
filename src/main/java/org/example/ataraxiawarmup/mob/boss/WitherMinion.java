package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.PathfinderGoalAvoidTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLeapAtTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalCastSpell;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalFollowLeader;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalShootWitherSkull;
import org.example.ataraxiawarmup.mob.spell.Spell;

public class WitherMinion extends EntitySkeletonWither {

    public WitherMinion(Location loc) {
        super(EntityTypes.ba, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
    }

    public void initPathfinder() {
        // run these a tick later so the entity actually exists in CustomMob.ENTITY_MAP
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            this.bP.a(3, new PathfinderGoalFollowLeader(this, EntityType.WITHER_SKELETON, 400, 2, 5));
            this.bP.a(5, new PathfinderGoalCastSpell(this, Spell.WEAKCHAOSLIGHTNING, true, 150, 200, EntityType.PLAYER, true));
            this.bP.a(6, new PathfinderGoalShootWitherSkull(this, EntityType.PLAYER, 16.0f, 20));
        });
        this.bP.a(8, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.bP.a(4, new PathfinderGoalAvoidTarget<>(this, EntitySkeletonWither.class, 5, 1, 1));
    }
}
