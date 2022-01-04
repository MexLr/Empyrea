package org.example.ataraxiawarmup.mob;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.player.CustomPlayer;

import javax.swing.plaf.IconUIResource;
import java.util.List;

public abstract class CustomBaseMob extends CustomMob {
    public CustomBaseMob(String name, EntityType entityType, List<Element> elements, int damage, int level, int defense, int maxHealth, List<CustomLootTable> lootTables, boolean template, double experience) {
        super(name, entityType, elements, damage, level, defense, maxHealth, lootTables, template, experience);
    }

    @Override
    public void onAttackPlayer(Player player) {

    }

    @Override
    public void onDamagePlayer(Player player) {
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        customPlayer.damage(this.getDamage(), this.getElements());
        player.sendMessage(customPlayer.getHealth() + "");
    }
}
