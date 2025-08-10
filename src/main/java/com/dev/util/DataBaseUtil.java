package com.dev.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseUtil {

    private DataBaseUtil() {}

    public static Connection getConnection() {
        String dbUrl = null;
        String dbInit = null;
        Connection connection = null;
        FileInputStream fis;
        Properties properties = new Properties();

        try {
            fis = new FileInputStream("src/resources/application.properties");
            properties.load(fis);
            dbUrl = properties.getProperty("db.host");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(dbUrl);
            dbInit = properties.getProperty("db.init.script");
            init(connection, dbInit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static void init(Connection connection, String scriptPath) {
        try (Statement statement = connection.createStatement()) {
            String sql = new String(Files.readAllBytes((Paths.get(scriptPath))));
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
