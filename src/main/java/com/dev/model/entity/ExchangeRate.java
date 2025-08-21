package com.dev.model.entity;

import com.dev.dto.ExchangeRatesDto;

import java.math.BigDecimal;

public class ExchangeRate {
    private Integer id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate(Integer id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public ExchangeRatesDto toDto() {
        return new ExchangeRatesDto(id, baseCurrency.toDto(), targetCurrency.toDto(), rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
               "id=" + id +
               ", baseCurrencyId='" + baseCurrency + '\'' +
               ", targetCurrencyId='" + targetCurrency + '\'' +
               ", rate=" + rate +
               '}';
    }
}
