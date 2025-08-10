package com.dev.model.entity;

public class ExchangeRate {
    private int id;
    //TODO можно сделать базовую и целевую ставки типа Currency, но по ТЗ это int переменные
    private int baseCurrencyId;
    private int targetCurrencyId;
    //TODO по ТЗ rate имеет тип decimal, но в sql есть только REAL - типо double (меньшая точность). Можно попробовать
    // в DB сделать ту переменную типу TEXT, а на уровне сущности переводить текст в DECIMAL (BigDecimal) для точных
    // расчётов

    private double rate; //Курс обмена единицы базовой валюты к единице целевой валюты
}
