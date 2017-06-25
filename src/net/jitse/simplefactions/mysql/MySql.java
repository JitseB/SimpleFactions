package net.jitse.simplefactions.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.jitse.simplefactions.utilities.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jitse on 23-6-2017.
 */
public class MySql {

    private final HikariDataSource hikariDataSource;

    public MySql(String host, int port, String username, String password, String database) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("useSSL", "false");
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public void close() {
        this.hikariDataSource.close();
    }

    public HikariDataSource getHikariDataSource() {
        return this.hikariDataSource;
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public void createTable(String name, String info) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + "(" + info + ");")) {
                statement.execute();
            } catch (SQLException exception) {
                Logger.log(Logger.LogLevel.ERROR, "An error occured while creating database table " + name + ".");
                exception.printStackTrace();
            }
        }).start();
    }

    public void execute(String query, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                statement.execute();
            } catch (SQLException exception) {
                Logger.log(Logger.LogLevel.ERROR, "An error occured while executing an update on the database.");
                exception.printStackTrace();
            }
        }).start();
    }

    public void select(String query, SelectCall call, Object... values) {
        new Thread(() -> {
            try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    statement.setObject((i + 1), values[i]);
                }
                call.call(statement.executeQuery());
            } catch (SQLException exception) {
                Logger.log(Logger.LogLevel.ERROR, "An error occured while executing a query on the database.");
                exception.printStackTrace();
            }
        }).start();
    }
}
