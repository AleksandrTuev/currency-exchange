package com.dev.util;

import com.dev.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DataBaseUtil {

    private DataBaseUtil() {}

    public static Connection getConnection() {
        String dbUrl = null;
        String dbInit = null;
        Connection connection = null;
//        FileInputStream fis;
        Properties properties = new Properties();

        try {
            System.out.println("Рабочая директория: " + System.getProperty("user.dir"));
//            fis = new FileInputStream(new File("/resources/application.properties"));
            //Метод Class.getResourceAsStream() позволяет загружать ресурсы (например, файлы) из classpath
            // (пути к классам) в виде потока (InputStream). Это особенно полезно, когда файлы находятся внутри
            // JAR-архива или в папке resources проекта (например, в Maven/Gradle)
            InputStream fis = Main.class.getResourceAsStream("/application.properties");
//            fis = new FileInputStream(new File("/resources/application.properties"));
//            "src/main/resources/application.properties"
//            "C:\\Users\\fisch\\dev\\testMyPetProject\\currency-exchange\\src\\main\\resources\\application.properties"
            properties.load(fis);
            dbUrl = properties.getProperty("db.host");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(dbUrl);
            dbInit = properties.getProperty("db.init.script");
            init(connection, dbInit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    private static void init(Connection connection, String scriptPath) {
        try (Statement statement = connection.createStatement()) {

            String sql = new String(Files.readAllBytes((Paths.get(scriptPath))));
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
