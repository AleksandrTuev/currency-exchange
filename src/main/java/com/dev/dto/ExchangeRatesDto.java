package com.dev.dto;

import java.math.BigDecimal;

public class ExchangeRatesDto {
    private final Integer id;
    private final CurrencyDto baseCurrency;
    private final CurrencyDto targetCurrency;
    private final BigDecimal rate;

    public ExchangeRatesDto(Integer id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, BigDecimal rate){
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRatesDto{" +
               "id=" + id +
               ", baseCurrencyId=" + baseCurrency +
               ", targetCurrencyId=" + targetCurrency +
               ", rate=" + rate +
               '}';
    }
}
