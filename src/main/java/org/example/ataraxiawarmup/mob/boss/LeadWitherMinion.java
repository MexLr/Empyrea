package org.example.ataraxiawarmup.mob.boss;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntitySkeletonWither;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalCastSpell;
import org.example.ataraxiawarmup.mob.pathfinder.PathfinderGoalFollowTarget;
import org.example.ataraxiawarmup.mob.spell.Spell;

public class LeadWitherMinion extends EntitySkeletonWither {
    public LeadWitherMinion(Location loc) {
        super(EntityTypes.ba, ((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(), loc.getY(), loc.getZ());
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
    }

    public void initPathfinder() {
        // run these a tick later so the entity actually exists in CustomMob.ENTITY_MAP
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            this.bP.a(4, new PathfinderGoalCastSpell(this, Spell.CHAOSFIRERAIN, false, 200, 200, EntityType.WITHER_SKELETON, true));
            this.bP.a(5, new PathfinderGoalCastSpell(this, Spell.WEAKCHAOSLIGHTNING, true, 100, 100, EntityType.PLAYER, true));
        });
        this.bP.a(7, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }
}
