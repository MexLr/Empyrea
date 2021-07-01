package org.example.ataraxiawarmup.mob;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.Element;

import java.util.List;

public abstract class CustomBaseMob extends CustomMob {
    public CustomBaseMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, CustomLootTable lootTable, boolean template) {
        super(name, entityType, elements, damage, level, defense, maxHealth, lootTable, template);
    }

    @Override
    public void onAttackPlayer(Player player) {

    }

    @Override
    public void onDamagePlayer(Player player) {

    }
}
