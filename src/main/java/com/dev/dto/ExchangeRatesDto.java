package com.dev.dto;

import java.math.BigDecimal;

public class ExchangeRatesDto {
    private Integer id;
    private CurrencyDto baseCurrencyId;
    private CurrencyDto targetCurrencyId;
    private BigDecimal rate;

    public ExchangeRatesDto(CurrencyDto baseCurrencyId, CurrencyDto targetCurrencyId, BigDecimal rate){
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRatesDto(Integer id, CurrencyDto baseCurrencyId, CurrencyDto targetCurrencyId, BigDecimal rate){
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }
}
