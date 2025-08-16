package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.dao.ExchangeRatesDAO;
import com.dev.model.entity.Currency;
import com.dev.model.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();
    
    private ExchangeRatesService() {}
    
    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto saveExchangeRates(CurrencyDto currencyBaseDto, CurrencyDto currencyTargetDto,
                                              BigDecimal rate) {
        int id = ExchangeRatesDAO.getInstance().save(currencyBaseDto.getId(), currencyTargetDto.getId(), rate);
        return new ExchangeRatesDto(id, currencyBaseDto, currencyTargetDto, rate);
    }

    public List<ExchangeRatesDto> getExchangeRates() {
        List<ExchangeRate> list = ExchangeRatesDAO.getInstance().findAll();
        return list.stream()
                .map(ExchangeRate::toDto)
                .toList();
    }

    public ExchangeRatesDto getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        int baseCurrencyId = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElse(null).getId();
        int targetCurrencyid = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElse(null).getId();
        //TODO сделать проверку что если одна из валют NULL выкидывать исключение
        ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyid).orElse(null);
        //TODO сделать проверку exchangeRate на null
        return exchangeRate.toDto();
    }
}
