package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.exception.CurrencyException;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DaoException;
import com.dev.exception.DataAccessException;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.entity.Currency;

import java.util.List;

public class CurrenciesService {
    private static final CurrenciesService INSTANCE = new CurrenciesService();

    private CurrenciesService() {
    }

    public static CurrenciesService getInstance() {
        return INSTANCE;
    }

    public List<CurrencyDto> getCurrencies() {
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

    public CurrencyDto getCurrencyByCode(String code) {
        try {
            Currency currency = CurrenciesDao.getInstance().findByCode(code).orElseThrow(
                    () -> new CurrencyNotFoundException(code));
            return currency.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get currency", e);
        }
    }

    public CurrencyDto saveCurrency(CurrencyDto currencyDto) {
        try {
            if (CurrenciesDao.getInstance().findByCode(currencyDto.getCode()).isPresent()) {
                throw new CurrencyException("currency already exist");
            }

            Currency currency = new Currency(currencyDto.getCode(), currencyDto.getName(), currencyDto.getSign());
            int id = CurrenciesDao.getInstance().save(currency);
            currency.setId(id);
            return currency.toDto();

        } catch (DaoException e) {
            throw new DataAccessException("cannot save currency", e);
        }
    }
}
