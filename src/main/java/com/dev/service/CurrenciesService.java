package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.entity.Currency;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<CurrencyDto> getCurrencies() {
        List<Currency> listCurrencies = CurrenciesDao.getInstance().findAll();
        List<CurrencyDto> listCurrenciesDto = listCurrencies.stream()
                .map(currency -> new CurrencyDto(
                        currency.getId(),
                        currency.getCode(),
                        currency.getFullName(),
                        currency.getSign()
                ))
                .toList();
        return listCurrenciesDto;
    }

    public CurrencyDto getCurrencyByCode(String code) {
        Currency currency = CurrenciesDao.getInstance().findByCode(code).orElse(null);
        return currency.toDto();
    }

    public CurrencyDto saveCurrency(CurrencyDto currencyDto) {
        Currency currency = new Currency(currencyDto.getCode(), currencyDto.getName(), currencyDto.getSign());
        CurrenciesDao.getInstance().save(currency);
        return currency.toDto(); //возвращена валюта Dto со вставленным id
    }

    public boolean hasCurrency(String currencyCode) {
        Optional<Currency> currency = CurrenciesDao.getInstance().findByCode(currencyCode);
        return currency.isPresent();
    }


}
