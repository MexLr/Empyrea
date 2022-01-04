package org.example.ataraxiawarmup.sql;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.example.ataraxiawarmup.item.customitem.CustomWeapon;
import org.example.ataraxiawarmup.item.customitem.Element;
import org.example.ataraxiawarmup.item.customitem.ItemAttribute;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.spawner.PlaceableSpawner;
import org.example.ataraxiawarmup.spawner.Spawner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("SuspiciousMethodCalls")
public class SqlSetter {

    MySQL mySQL = new MySQL();

    public void addWeapon(CustomWeapon weapon) {
        try {
            SqlGetter getter = new SqlGetter();

            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Weapons WHERE name=?");
            statement.setString(1, ChatColor.stripColor(weapon.getItemMeta().getDisplayName()));

            ResultSet results = statement.executeQuery();
            results.next();

            if (!getter.weaponNameExists(ChatColor.stripColor(weapon.getItemMeta().getDisplayName()))) {
                PreparedStatement insert = mySQL.getConnection().prepareStatement("INSERT INTO Weapons (name, rarity, fireDamageLow, waterDamageLow, earthDamageLow, thunderDamageLow, airDamageLow, chaosDamageLow, neutralDamageLow, fireDamageHigh, waterDamageHigh, earthDamageHigh, thunderDamageHigh, airDamageHigh, chaosDamageHigh, neutralDamageHigh, fireDamagePercent, waterDamagePercent, earthDamagePercent, thunderDamagePercent, airDamagePercent, chaosDamagePercent, fireDefense, waterDefense, earthDefense, thunderDefense, airDefense, chaosDefense, health, lootBonus, xpBonus, lifeSteal, abilityRegen) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                insert.setString(1, ChatColor.stripColor(weapon.getItemMeta().getDisplayName()));
                insert.setString(2, weapon.getRarity().getName());
                for (Element element : Element.getElementOrder()) {
                    if (weapon.getElements().contains(element)) {
                        insert.setInt(element.getId() + 2, weapon.getLowerBounds().get(weapon.getElements().indexOf(element)));
                        insert.setInt(element.getId() + 9, weapon.getUpperBounds().get(weapon.getElements().indexOf(element)));
                        continue;
                    }
                    insert.setInt(element.getId() + 2, 0);
                    insert.setInt(element.getId() + 9, 0);
                }
                for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
                    if (weapon.getAttributes().containsValue(attribute)) {
                        insert.setInt(16 + attribute.getId(), weapon.getAttributes().get(attribute));
                    }
                    insert.setInt(16 + attribute.getId(), 0);
                }
                insert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSpawner(Spawner spawner) {
        try {
            SqlGetter getter = new SqlGetter();

            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Spawners WHERE id=?");
            statement.setInt(1, spawner.getId());

            ResultSet results = statement.executeQuery();
            results.next();

            if (!getter.spawnerIdExists(spawner.getId())) {
                PreparedStatement insert = mySQL.getConnection().prepareStatement("INSERT INTO Spawners (id, mobName, mobLevel, spawnerLevel, x, y, z, isPlaced) VALUE (?, ?, ?, ?, ?, ?, ?, ?)");
                insert.setInt(1, spawner.getId());
                insert.setString(2, spawner.getMobType().getName());
                insert.setShort(3, (short) spawner.getMobType().getLevel());
                insert.setInt(4, spawner.getLevel());
                insert.setDouble(5, spawner.getLocation().getX());
                insert.setDouble(6, spawner.getLocation().getY());
                insert.setDouble(7, spawner.getLocation().getZ());
                insert.setBoolean(8, spawner instanceof PlaceableSpawner);
                insert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSpawner(Spawner spawner) {
        try {
            SqlGetter getter = new SqlGetter();

            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Spawners WHERE id=?");
            statement.setInt(1, spawner.getId());

            ResultSet results = statement.executeQuery();
            results.next();

            if (getter.spawnerIdExists(spawner.getId())) {
                PreparedStatement delete = mySQL.getConnection().prepareStatement("DELETE FROM Spawners WHERE id=?");
                delete.setInt(1, spawner.getId());
                delete.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(CustomPlayer player) {
        try {
            SqlGetter getter = new SqlGetter();

            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM PlayerStats WHERE uuid=?");
            statement.setString(1, player.getPlayer().getUniqueId().toString());

            ResultSet results = statement.executeQuery();
            results.next();

            if (!getter.playerIdExists(player.getPlayer().getUniqueId())) {
                PreparedStatement insert = mySQL.getConnection().prepareStatement("INSERT INTO PlayerStats (uuid, projectileTrail, combatExp, combatLevel, totalCombatExp, privateLocX, privateLocY, privateLocZ) VALUE (?, ?, ?, ?, ?, ?, ?, ?)");
                insert.setString(1, player.getPlayer().getUniqueId().toString());
                insert.setString(2, player.getTrail());
                insert.setDouble(3, player.getCurrentExp());
                insert.setInt(4, player.getCombatLevel());
                insert.setDouble(5, player.getTotalCombatExp());
                insert.setInt(6, (int) player.getPrivateAreaLocation().getX());
                insert.setInt(7, (int) player.getPrivateAreaLocation().getY());
                insert.setInt(8, (int) player.getPrivateAreaLocation().getZ());
                insert.executeUpdate();
            } else {
                PreparedStatement update = mySQL.getConnection().prepareStatement("UPDATE PlayerStats SET projectileTrail=?, combatExp=?, combatLevel=?, totalCombatExp=?, privateLocX=?, privateLocY=?, privateLocZ=? WHERE uuid=?");
                update.setString(1, player.getTrail());
                update.setDouble(2, player.getCurrentExp());
                update.setInt(3, player.getCombatLevel());
                update.setDouble(4, player.getTotalCombatExp());
                update.setInt(5, (int) player.getPrivateAreaLocation().getX());
                update.setInt(6, (int) player.getPrivateAreaLocation().getY());
                update.setInt(7, (int) player.getPrivateAreaLocation().getZ());
                update.setString(8, player.getPlayer().getUniqueId().toString());
                update.executeUpdate();
            }

            Bukkit.getConsoleSender().sendMessage("§a" + player.getPlayer().getName() + "'s stats successfully saved!");
            Bukkit.getConsoleSender().sendMessage("§a" + player.getTrail());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
