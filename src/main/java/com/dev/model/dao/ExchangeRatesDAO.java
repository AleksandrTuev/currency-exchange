package com.dev.model.dao;

import com.dev.exception.DaoException;
import com.dev.model.entity.Currency;
import com.dev.model.entity.ExchangeRate;
import com.dev.util.DataBaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dev.util.ProjectConstants.*;

public class ExchangeRatesDAO {

    private static final ExchangeRatesDAO INSTANCE = new ExchangeRatesDAO();
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id,
                                    target_currency_id,
                                    rate)
            VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            WHERE id = ?""";
    private static final String FIND_BY_IDS_SQL = """
            SELECT id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            WHERE base_currency_id = ?
            AND target_currency_id = ?""";
    private static final String UPDATE_BY_IDS_SQL = """
            UPDATE exchange_rates
            SET rate = ?
            WHERE base_currency_id = ?
            AND target_currency_id = ?""";

    public static ExchangeRatesDAO getInstance() {
        return INSTANCE;
    }

    public int save(int currencyBaseId, int currencyTargetId, BigDecimal rate) throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setInt(1, currencyBaseId);
            preparedStatement.setInt(2, currencyTargetId);
            preparedStatement.setBigDecimal(3, rate);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            return generatedKeys.getInt(1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<ExchangeRate> findAll() throws DaoException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = DataBaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(PARAMETER_ID);
                int baseCurrencyId = resultSet.getInt(PARAMETER_BASE_CURRENCY_ID_SQL);
                int targetCurrencyId = resultSet.getInt(PARAMETER_TARGET_CURRENCY_ID_SQL);
                BigDecimal rate = resultSet.getBigDecimal(PARAMETER_RATE);

                Currency baseCurrency = getCurrencyById(connection, baseCurrencyId);
                Currency targetCurrency = getCurrencyById(connection, targetCurrencyId);

                ExchangeRate exchangeRate = new ExchangeRate(id, baseCurrency, targetCurrency, rate);
                exchangeRates.add(exchangeRate);
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return exchangeRates;
    }

    private Currency getCurrencyById(Connection connection, int currencyId) throws DaoException {
        int id = 0;
        String code = "";
        String fullName = "";
        String sign = "";

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, currencyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(PARAMETER_ID);
                code = resultSet.getString(PARAMETER_CODE);
                fullName = resultSet.getString(PARAMETER_FULL_NAME);
                sign = resultSet.getString(PARAMETER_SIGN);
            }
            return new Currency(id, code, fullName, sign);
        } catch (SQLException e) {
            throw new DaoException(e); //TODO добавить инфо о том что валюта по id не найдена
        }
    }

    public Optional<ExchangeRate> findByIds(int baseCurrencyId, int targetCurrencyId) throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_IDS_SQL)) {
            ExchangeRate exchangeRate = null;

            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            //TODO выкинуть исключение когда нет пары


            if (resultSet.next()) {
                int id = resultSet.getInt(PARAMETER_ID);
                BigDecimal rate = resultSet.getBigDecimal(PARAMETER_RATE);

                Currency baseCurrency = getCurrencyById(connection, baseCurrencyId);
                Currency targetCurrency = getCurrencyById(connection, targetCurrencyId);

                exchangeRate = new ExchangeRate(id, baseCurrency, targetCurrency, rate);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void updateByIds(int baseCurrencyId, int targetCurrencyId, BigDecimal rate) throws DaoException {
        try (Connection connection = DataBaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BY_IDS_SQL)) {

            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, baseCurrencyId);
            preparedStatement.setInt(3, targetCurrencyId);
            preparedStatement.executeUpdate();
//            //TODO выкинуть исключение когда нет пары
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
