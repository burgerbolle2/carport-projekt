package app.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionPool {

    private static DataSource dataSource;

    public static DataSource getInstance(String username, String password, String urlTemplate, String dbName) {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setUsername(username);
            config.setPassword(password);
            config.setJdbcUrl(String.format(urlTemplate, dbName));
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}



