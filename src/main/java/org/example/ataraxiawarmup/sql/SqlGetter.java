package org.example.ataraxiawarmup.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlGetter {

    MySQL mySQL = new MySQL();

    public boolean spawnerIdExists(int id) {
        try {
            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Spawners WHERE id=?");
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean playerIdExists(UUID uuid) {
        try {
            PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM PlayerStats WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
