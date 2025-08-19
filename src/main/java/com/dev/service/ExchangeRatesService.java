package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
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

    private ExchangeRatesService() {}
    
    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto saveExchangeRates(CurrencyDto currencyBaseDto, CurrencyDto currencyTargetDto,
                                              String rate) {
        try {
            BigDecimal rateBigDecimal = new BigDecimal(rate).setScale(6, BigDecimal.ROUND_CEILING);
            if (ExchangeRatesDAO.getInstance().findByIds(currencyBaseDto.getId(),currencyTargetDto.getId()).isPresent()) {
                throw new ExchangeRateException("exchange rate already exists");
            }
            int id = ExchangeRatesDAO.getInstance().save(currencyBaseDto.getId(), currencyTargetDto.getId(), rateBigDecimal);
            return new ExchangeRatesDto(id, currencyBaseDto, currencyTargetDto, rateBigDecimal);
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rate", e);
        }
    }

    public List<ExchangeRatesDto> getExchangeRates() {
        try {
            List<ExchangeRate> list = ExchangeRatesDAO.getInstance().findAll();
            return list.stream()
                    .map(ExchangeRate::toDto)
                    .toList();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeRatesDto getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        int baseCurrencyId = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElse(null).getId();
        int targetCurrencyId = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElse(null).getId();
        //TODO сделать проверку что если одна из валют NULL выкидывать исключение
        ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId).orElse(null);
        //TODO сделать проверку exchangeRate на null
        return exchangeRate.toDto();
    }

    public ExchangeRatesDto updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        int baseCurrencyId = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElse(null).getId();
        int targetCurrencyId = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElse(null).getId();
        //TODO сделать проверку что если одна из валют NULL выкидывать исключение
        ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId).orElse(null);
        ExchangeRatesDAO.getInstance().updateByIds(baseCurrencyId, targetCurrencyId, rate);
        exchangeRate.setRate(rate);
        //TODO сделать проверку exchangeRate на null
        return exchangeRate.toDto();
    }
}
