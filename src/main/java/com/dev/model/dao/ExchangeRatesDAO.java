package com.dev.model.dao;

import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.DaoException;
import com.dev.util.DataBaseUtil;

import java.math.BigDecimal;
import java.sql.*;

public class ExchangeRatesDAO {
    private static final ExchangeRatesDAO INSTANCE = new ExchangeRatesDAO();
    private static final String SAVE_SQL = """
            INSERT INTO exchange_rates (base_currency_id,
                                    target_currency_id,
                                    rate)
            VALUES (?, ?, ?)
            """;

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
}
