package org.example.ataraxiawarmup.sql;

import org.bukkit.Bukkit;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.spawner.InvisibleSpawner;
import org.example.ataraxiawarmup.spawner.PlaceableSpawner;
import org.example.ataraxiawarmup.spawner.Spawner;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlSetter {

    MySQL mySQL = new MySQL();

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
                Bukkit.getPlayer("MexLr").sendMessage("" + spawner.getLevel());
                insert.setInt(4, spawner.getLevel());
                insert.setDouble(5, spawner.getLocation().getX());
                insert.setDouble(6, spawner.getLocation().getY());
                insert.setDouble(7, spawner.getLocation().getZ());
                if (spawner instanceof PlaceableSpawner) {
                    insert.setBoolean(8, true);
                } else {
                    insert.setBoolean(8, false);
                }
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
