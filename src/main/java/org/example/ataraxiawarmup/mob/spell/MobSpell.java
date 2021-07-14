package org.example.ataraxiawarmup.mob.spell;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MobSpell {

    private double damage;
    private List<Element> elements;
    private Spell spell;

    public MobSpell(double damage, List<Element> elements, Spell spell) {
        this.damage = damage;
        this.elements = elements;
        this.spell = spell;
    }

    abstract void onUse(CustomMob user, Location location, boolean setCasting, Entity target);

    public double getDamage() {
        return this.damage;
    }

    public List<Element> getElements() {
        return this.elements;
    }

}
