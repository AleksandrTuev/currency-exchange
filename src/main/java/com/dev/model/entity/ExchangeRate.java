package com.dev.model.entity;

import com.dev.dto.ExchangeRatesDto;

import java.math.BigDecimal;

public class ExchangeRate {
    private Integer id;
    //TODO можно сделать базовую и целевую ставки типа Currency, но по ТЗ это int переменные
    private Currency baseCurrencyId;
    private Currency targetCurrencyId;
    //TODO по ТЗ rate имеет тип decimal, но в sql есть только REAL - типо double (меньшая точность). Можно попробовать
    // в DB сделать ту переменную типу TEXT, а на уровне сущности переводить текст в DECIMAL (BigDecimal) для точных
    // расчётов
    private BigDecimal rate; //Курс обмена единицы базовой валюты к единице целевой валюты

//    public ExchangeRate() {}
    public ExchangeRate(Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public ExchangeRate(Integer id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal rate) {
        super();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Currency getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Currency baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Currency getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Currency targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public ExchangeRatesDto toDto() {
        return new ExchangeRatesDto(id, baseCurrencyId.toDto(), targetCurrencyId.toDto(), rate);
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
               "id=" + id +
               ", baseCurrencyId='" + baseCurrencyId + '\'' +
               ", targetCurrencyId='" + targetCurrencyId + '\'' +
               ", rate=" + rate +
               '}';
    }
}
