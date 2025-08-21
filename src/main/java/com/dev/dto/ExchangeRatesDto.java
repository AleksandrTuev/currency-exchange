package com.dev.dto;

import java.math.BigDecimal;

public class ExchangeRatesDto {
    private final Integer id;
    private final CurrencyDto baseCurrencyId;
    private final CurrencyDto targetCurrencyId;
    private final BigDecimal rate;

    public ExchangeRatesDto(Integer id, CurrencyDto baseCurrencyId, CurrencyDto targetCurrencyId, BigDecimal rate){
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRatesDto{" +
               "id=" + id +
               ", baseCurrencyId=" + baseCurrencyId +
               ", targetCurrencyId=" + targetCurrencyId +
               ", rate=" + rate +
               '}';
    }
}
