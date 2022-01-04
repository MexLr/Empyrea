package org.example.ataraxiawarmup.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.sql.MySQL;
import org.example.ataraxiawarmup.sql.SqlGetter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpawnerInitializer {

    MySQL mySQL = new MySQL();

    public void initializeSpawners() {
        SqlGetter getter = new SqlGetter();
        int maxId = 0;
        try {
            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Spawners WHERE 1");
            ResultSet results = statement.executeQuery();
            if (results.last()) {
                maxId = results.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (maxId > 0) {
            try {
                if (getter.spawnerIdExists(maxId)) {
                    PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Spawners WHERE id=?");
                    statement.setInt(1, maxId);

                    ResultSet results = statement.executeQuery();
                    results.next();

                    int spawnerId = results.getInt("id");
                    String mobName = results.getString("mobName");
                    short mobLevel = results.getShort("mobLevel");
                    int level = results.getInt("spawnerLevel");
                    Location location = new Location(Bukkit.getWorld("Hub"), results.getDouble("x"), results.getDouble("y"), results.getDouble("z"));
                    boolean isPlaced = results.getBoolean("isPlaced");

                    if (isPlaced) {
                        PlaceableSpawner placedSpawner;
                        placedSpawner = new PlaceableSpawner(CustomMob.fromName(mobLevel + mobName), level, location, Main.getInstance(), spawnerId);
                        placedSpawner.startSpawning();
                    } else {
                        Spawner spawner = new Spawner(CustomMob.fromName(mobLevel + mobName), level, location, Main.getInstance(), spawnerId);
                        spawner.startSpawning();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            maxId--;
        }
    }

}
