package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.model.dao.ExchangeRatesDAO;

import java.math.BigDecimal;

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
}
