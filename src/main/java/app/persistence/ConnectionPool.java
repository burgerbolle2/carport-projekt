package app.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    // Singleton setup
    public static ConnectionPool getInstance(String user, String password, String urlTemplate, String dbName) {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(String.format(urlTemplate, dbName));
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setLeakDetectionThreshold(15000);
            dataSource = new HikariDataSource(config);
        }
        return new ConnectionPool();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

