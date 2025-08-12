package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.model.dao.CurrencyDao;
import com.dev.model.entity.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private static final CurrencyService INSTANCE = new CurrencyService();
    /*
    * Singleton стоит использовать для:
        Service классы (если они stateless)
    *
    * Что значит "stateless"?
        Класс не хранит изменяемое состояние (т.е. нет полей, которые могут быть модифицированы после создания).
    * Все данные передаются через параметры методов.*/

    private CurrencyService() {
    }

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    public List<Currency> getCurrencies() {
        return CurrencyDao.getInstance().findAll();
    }

    public Currency saveCurrency(CurrencyDto currencyDto) {
        Currency currency = new Currency(currencyDto.code(), currencyDto.fullName(), currencyDto.sign());
        return CurrencyDao.getInstance().save(currency); //возвращена валюта со вставленным id
    }

    public boolean hasCurrency(String currencyCode) {
        Optional<Currency> currency = CurrencyDao.getInstance().findByCode(currencyCode);
        return currency.isPresent();
    }


}
