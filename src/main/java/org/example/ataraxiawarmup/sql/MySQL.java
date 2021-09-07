package org.example.ataraxiawarmup.sql;

import jdk.jshell.spi.ExecutionControl;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL {
    private static Connection connection;
    public String host, database, username, password;
    public int port;

    public void mySqlSetup() {
        host = "localhost";
        port = 3306;
        database = "empyrea";
        username = "root";
        password = "password";

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.username, this.password));

            Bukkit.getConsoleSender().sendMessage("§aMySQL Successfully Connected");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void refreshConnection() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM Dual");
            ResultSet valid = statement.executeQuery();
            if (valid.next()) {
                return;
            }
            mySqlSetup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
