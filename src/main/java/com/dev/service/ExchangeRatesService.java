package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DaoException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.dao.ExchangeRatesDAO;
import com.dev.model.entity.Currency;
import com.dev.model.entity.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
//    private static final Logger log = LoggerFactory.getLogger(ExchangeRatesService.class);

    private ExchangeRatesService() {
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto saveExchangeRates(
            CurrencyDto currencyBaseDto, CurrencyDto currencyTargetDto, String rate) throws ExchangeRateException,
            CurrencyNotFoundException, DataAccessException {
        try {
            BigDecimal rateBigDecimal = new BigDecimal(rate).setScale(6, BigDecimal.ROUND_CEILING);
            if (ExchangeRatesDAO.getInstance().findByIds(currencyBaseDto.getId(), currencyTargetDto.getId()).isPresent()) {
                throw new ExchangeRateException("exchange rate already exists");
            }
            int id = ExchangeRatesDAO.getInstance().save(currencyBaseDto.getId(), currencyTargetDto.getId(), rateBigDecimal);
            return new ExchangeRatesDto(id, currencyBaseDto, currencyTargetDto, rateBigDecimal);
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rate", e);
        }
    }

    public List<ExchangeRatesDto> getExchangeRates() throws DataAccessException {
        try {
            List<ExchangeRate> list = ExchangeRatesDAO.getInstance().findAll();
            return list.stream()
                    .map(ExchangeRate::toDto)
                    .toList();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeRatesDto getExchangeRate(
            String baseCurrencyCode, String targetCurrencyCode) throws CurrencyNotFoundException, DataAccessException,
            ExchangeRateException {
        try {
            Currency baseCurrency = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(baseCurrencyCode));
            Currency targetCurrency = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(targetCurrencyCode));

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId)
                    .orElseThrow(() -> new ExchangeRateException("exchange rate not found"));
            return exchangeRate.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeRatesDto updateExchangeRate(
            String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws CurrencyNotFoundException,
            DataAccessException, ExchangeRateException {
        try {
            Currency baseCurrency = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(baseCurrencyCode));
            Currency targetCurrency = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(targetCurrencyCode));

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId)
                    .orElseThrow(() -> new ExchangeRateException("exchange rate not found"));
            ExchangeRatesDAO.getInstance().updateByIds(baseCurrencyId, targetCurrencyId, rate);
            exchangeRate.setRate(rate);
            return exchangeRate.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }
}
