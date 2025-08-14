package com.dev.dto;

import java.math.BigDecimal;

public class ExchangeRatesDto {
    private Integer id;
    private final CurrencyDto baseCurrencyId;
    private final CurrencyDto targetCurrencyId;
    private final BigDecimal rate;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public CurrencyDto getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public CurrencyDto getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
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
