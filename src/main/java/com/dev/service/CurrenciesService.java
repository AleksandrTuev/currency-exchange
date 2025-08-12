package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.entity.Currency;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();
    /*
    * Singleton стоит использовать для:
        Service классы (если они stateless)
    *
    * Что значит "stateless"?
        Класс не хранит изменяемое состояние (т.е. нет полей, которые могут быть модифицированы после создания).
    * Все данные передаются через параметры методов.*/

    private CurrenciesService() {
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<Currency> getCurrencies() {
        return CurrenciesDao.getInstance().findAll();
    }

    public Currency getCurrencyByCode(String code) {
        return CurrenciesDao.getInstance().findByCode(code).orElse(null);
    }

    public Currency saveCurrency(CurrencyDto currencyDto) {
        Currency currency = new Currency(currencyDto.code(), currencyDto.fullName(), currencyDto.sign());
        return CurrenciesDao.getInstance().save(currency); //возвращена валюта со вставленным id
    }

    public boolean hasCurrency(String currencyCode) {
        Optional<Currency> currency = CurrenciesDao.getInstance().findByCode(currencyCode);
        return currency.isPresent();
    }


}
