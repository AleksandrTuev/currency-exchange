package com.dev.dao;

import com.dev.exception.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CurrencyDao {
    private static final CurrencyDao INSTANCE = new CurrencyDao(); //паттерн синглтон
    private static final String DELETE_SQL = """
            DELETE FROM currencies
            WHERE id = ?
            """;

    private CurrencyDao() {}

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    public boolean delete(int id) {
        try (Connection connection = DriverManager.getConnection("url");
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) { //TODO подставить URL (в данном случае каждый раз открывается и закрывается соединения

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
