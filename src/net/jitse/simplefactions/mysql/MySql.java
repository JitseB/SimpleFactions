package net.jitse.simplefactions.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.jitse.simplefactions.SimpleFactions;
import net.jitse.simplefactions.utilities.Logger;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jitse on 23-6-2017.
 */
public class MySql {

    private HikariDataSource hikariDataSource;

    public boolean connect(String host, int port, String username, String password, String database){
        try{
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + database);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.addDataSourceProperty("useSSL", "false");
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariDataSource = new HikariDataSource(hikariConfig);
        } catch (Exception exception) {
            hikariDataSource = null;
            return false;
        }
        return true;
    }

    public boolean isConnected(){
        return hikariDataSource == null ? false : !hikariDataSource.isClosed();
    }

    public void close() {
        this.hikariDataSource.close();
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

    public void selectSync(String query, SelectCall call, Object... values) {
        try (Connection resource = getConnection(); PreparedStatement statement = resource.prepareStatement(query)) {
            for (int i = 0; i < values.length; i++) {
                statement.setObject((i + 1), values[i]);
            }
            call.call(statement.executeQuery());
        } catch (SQLException exception) {
            Logger.log(Logger.LogLevel.ERROR, "An error occured while executing a query on the database.");
            exception.printStackTrace();
        }
    }
}
