package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.lang.reflect.Constructor;
import java.util.List;

public enum Spell {

    /**
     * Spell ideas:
     * Summon undead mobs, like they rise out of the ground or something
     * Send a wave forward, damaging players in its path
     * Healing spell
     * Cloning, lunatic cultist style
     *
     *
     */

    CHAOSLIGHTNING("Chaos Lightning", 1, ChaosLightning.class),
    CHAOSFIRERAIN("Chaos Fire Rain", 2, ChaosFireRain.class),
    WEAKCHAOSLIGHTNING("Weak Chaos Lightning", 3, WeakChaosLightning.class),
    SHOOTWITHERSKULL("Shoot Wither Skull", 4, ShootWitherSkull.class),
    DEATHRAY("Deathray", 5, Deathray.class),
    CHAOSVEXSPAWN("Chaos Vex Spawn", 6, ChaosVexSpawn.class);

    private String name;
    private int id;
    private Class<? extends MobSpell> clazz;

    Spell(String name, int id, Class<? extends MobSpell> clazz) {
        this.name = name;
        this.id = id;
        this.clazz = clazz;
    }

    public void use(CustomMob mob, Location location, boolean setCasting, Entity target) {
        try {
            Constructor constructor;
            constructor = this.clazz.getConstructor(new Class[]{double.class, List.class});
            MobSpell spell = (MobSpell) constructor.newInstance(30, List.of());
            spell.onUse(mob, location, setCasting, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
