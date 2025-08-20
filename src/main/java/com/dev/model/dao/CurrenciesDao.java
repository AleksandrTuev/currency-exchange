package com.dev.model.dao;

import com.dev.exception.DataBaseConnectionException;
import com.dev.model.entity.Currency;
import com.dev.exception.DaoException;
import com.dev.util.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesDao {
    private static final CurrenciesDao INSTANCE = new CurrenciesDao(); //паттерн синглтон
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_FULL_NAME = "full_name";
    private static final String PARAMETER_SIGN = "sign";

    private static final String DELETE_SQL = """
            DELETE FROM currencies
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO currencies (code,
                                    full_name,
                                    sign)
            VALUES (?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE currencies
            SET code = ?,
                full_name = ?,
                sign = ?
            WHERE id = ?
            """;

    private static final String FIND_BY_CODE_SQL = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            WHERE code = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            """;


    private CurrenciesDao() {
    }

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }

    public List<Currency> findAll() throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Currency> currencies = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currencies.add(new Currency(resultSet.getInt(PARAMETER_ID), resultSet.getString(PARAMETER_CODE),
                        resultSet.getString(PARAMETER_FULL_NAME), resultSet.getString(PARAMETER_SIGN)));
            }
            return currencies;
        } catch (SQLException | DataBaseConnectionException e) {
            throw new DaoException("cannot open DB connection", e);
        }
    }

    public Optional<Currency> findByCode(String currencyCode) throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            preparedStatement.setString(1, currencyCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Currency(resultSet.getInt(PARAMETER_ID),
                        resultSet.getString(PARAMETER_CODE), resultSet.getString(PARAMETER_FULL_NAME),
                        resultSet.getString(PARAMETER_SIGN)));
            } else {
                return Optional.empty();
            }
        } catch (SQLException | DataBaseConnectionException e) {
            throw new DaoException("cannot open DB connection", e);
        }
    }

    public void update(Currency currency) {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.setLong(4, currency.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public int save(Currency currency) throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return generatedKeys.getInt(1);

        } catch (SQLException | DataBaseConnectionException e) {
            throw new DaoException("cannot open DB connection", e);
        }
    }

    public boolean delete(int id) {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) { //TODO подставить URL (в данном случае каждый раз открывается и закрывается соединения
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
