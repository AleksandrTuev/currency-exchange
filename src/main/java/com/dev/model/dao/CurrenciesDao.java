package com.dev.model.dao;

import com.dev.exception.DataBaseConnectionException;
import com.dev.model.entity.Currency;
import com.dev.exception.DaoException;
import com.dev.util.DataBaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dev.util.ProjectConstants.*;

public class CurrenciesDao {
    private static final CurrenciesDao INSTANCE = new CurrenciesDao();

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

    public List<Currency> findAll() {
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

    public Optional<Currency> findByCode(String currencyCode) {
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

    public int save(Currency currency) {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL,
                     Statement.RETURN_GENERATED_KEYS)) {

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
}
