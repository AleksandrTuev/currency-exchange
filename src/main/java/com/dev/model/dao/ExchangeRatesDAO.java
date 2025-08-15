package com.dev.model.dao;

import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.DaoException;
import com.dev.model.entity.Currency;
import com.dev.model.entity.ExchangeRate;
import com.dev.util.DataBaseUtil;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDAO {
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_BASE_CURRENCY_ID = "base_currency_id";
    private static final String PARAMETER_TARGET_CURRENCY_ID = "target_currency_id";
    private static final String PARAMETER_RATE = "rate";
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_FULL_NAME = "full_name";
    private static final String PARAMETER_SIGN = "sign";

    private static final ExchangeRatesDAO INSTANCE = new ExchangeRatesDAO();
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id,
                                    target_currency_id,
                                    rate)
            VALUES (?, ?, ?)
            """;
    private static final String FIND_ALL = """
            SELECT id,
                   base_currency_id,
                   target_currency_id,
                   rate
            FROM exchange_rates
            """;
    private static final String FIND_BY_ID = """
            SELECT id,
                   code,
                   full_name,
                   sign
            FROM currencies
            WHERE id = ?""";

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
            int id = generatedKeys.getInt(1);
//            if (generatedKeys.next()) {
//                exchangeRatesDTO.(generatedKeys.getInt(1));
//            }
            return id;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<ExchangeRate> findAll() throws DaoException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = DataBaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(PARAMETER_ID);
                int baseCurrencyId = resultSet.getInt(PARAMETER_BASE_CURRENCY_ID);
                int targetCurrencyId = resultSet.getInt(PARAMETER_TARGET_CURRENCY_ID);
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

        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
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
            throw new DaoException(e);
        }
    }
}
