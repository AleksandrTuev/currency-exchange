package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DaoException;
import com.dev.exception.DataAccessException;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.entity.Currency;

import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private CurrenciesService() {
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<CurrencyDto> getCurrencies() throws DataAccessException, CurrencyNotFoundException {
        try {
            List<Currency> listCurrencies = CurrenciesDao.getInstance().findAll();
            if (listCurrencies.isEmpty()) {
                throw new CurrencyNotFoundException("currencies not found");
            }

            return listCurrencies.stream()
                    .map(currency -> new CurrencyDto(
                            currency.getId(),
                            currency.getCode(),
                            currency.getFullName(),
                            currency.getSign()
                    ))
                    .toList();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get list currencies", e);
        }
    }

    public CurrencyDto getCurrencyByCode(String code) throws CurrencyNotFoundException, DataAccessException {
        try {
            Currency currency = CurrenciesDao.getInstance().findByCode(code).orElseThrow(
                    () -> new CurrencyNotFoundException(code));
            return currency.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get currency", e);
        }
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
