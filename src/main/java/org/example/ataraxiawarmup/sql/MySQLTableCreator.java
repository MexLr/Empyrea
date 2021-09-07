package org.example.ataraxiawarmup.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLTableCreator {

    MySQL mySQL = new MySQL();

    public void createSpawnerTable() throws SQLException {
        PreparedStatement dropStatement = mySQL.getConnection().prepareStatement("DROP TABLE IF EXISTS Spawners");
        dropStatement.executeUpdate();
        PreparedStatement createStatement = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Spawners (id int PRIMARY KEY, mobName varchar(20), mobLevel smallint, spawnerLevel int, x double, y double, z double, isPlaced boolean)");
        createStatement.executeUpdate();
    }

    public void createPlayerStatsTable() throws SQLException {
        PreparedStatement dropStatement = mySQL.getConnection().prepareStatement("DROP TABLE IF EXISTS PlayerStats");
        dropStatement.executeUpdate();
        PreparedStatement createStatement = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerStats (uuid varchar(36) PRIMARY KEY, projectileTrail varchar(20), combatExp double, combatLevel int, totalCombatExp double, privateLocX int, privateLocY int, privateLocZ int)");
        createStatement.executeUpdate();
    }

    public void createWeaponsTable() throws SQLException {
        PreparedStatement dropStatement = mySQL.getConnection().prepareStatement("DROP TABLE IF EXISTS Weapons");
        dropStatement.executeUpdate();
        PreparedStatement createStatement = mySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Weapons (name varchar(30) PRIMARY KEY, rarity varchar(15), fireDamageLow int, waterDamageLow int, earthDamageLow int, thunderDamageLow int, airDamageLow int, chaosDamageLow int, neutralDamageLow int, fireDamageHigh int, waterDamageHigh int, earthDamageHigh int, thunderDamageHigh int, airDamageHigh int, chaosDamageHigh int, neutralDamageHigh int, fireDamagePercent smallint, waterDamagePercent smallint, earthDamagePercent smallint, thunderDamagePercent smallint, airDamagePercent smallint, chaosDamagePercent smallint, fireDefense smallint, waterDefense smallint, earthDefense smallint, thunderDefense smallint, airDefense smallint, chaosDefense smallint, health smallint, lootBonus smallint, xpBonus smallint, lifeSteal smallint, abilityRegen smallint)");
        createStatement.executeUpdate();
    }

}
