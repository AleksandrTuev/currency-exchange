package com.dev.util;

import com.dev.Main;
import com.dev.exception.DataAccessException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DataBaseUtil {

    private DataBaseUtil() {
    }

    public static Connection getConnection() {
        String dbUrl = null;
        String dbInit = null;
        Connection connection = null;
        Properties properties = new Properties();

        try {
            //Метод Class.getResourceAsStream() позволяет загружать ресурсы (например, файлы) из classpath
            // (пути к классам) в виде потока (InputStream). Это особенно полезно, когда файлы находятся внутри
            // JAR-архива или в папке resources проекта (например, в Maven/Gradle)
            InputStream fis = Main.class.getResourceAsStream("/application.properties");
            properties.load(fis);
            dbUrl = properties.getProperty("db.host");

            connection = DriverManager.getConnection(dbUrl);
            dbInit = properties.getProperty("db.init.script");
            init(connection, dbInit);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File 'application.properties' not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file 'application.properties'", e);
        } catch (SQLException e) {
            throw new DataAccessException("Cannot open DB connection", e);
        }

        return connection;
    }

    private static void init(Connection connection, String scriptPath) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = new String(Files.readAllBytes((Paths.get(scriptPath))));
            statement.executeUpdate(sql);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file 'init.sql'", e);
        }
    }
}
