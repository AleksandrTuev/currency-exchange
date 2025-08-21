package com.dev.service;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DaoException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.model.dao.CurrenciesDao;
import com.dev.model.dao.ExchangeRatesDAO;
import com.dev.model.entity.Currency;
import com.dev.model.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

import static com.dev.util.ProjectConstants.CURRENCY_USD;

public class ExchangeRatesService {
    private static final ExchangeRatesService INSTANCE = new ExchangeRatesService();

    private ExchangeRatesService() {
    }

    public static ExchangeRatesService getInstance() {
        return INSTANCE;
    }

    public ExchangeRatesDto saveExchangeRates(
            CurrencyDto currencyBaseDto, CurrencyDto currencyTargetDto, String rate) throws ExchangeRateException,
            CurrencyNotFoundException, DataAccessException {
        try {
            BigDecimal rateBigDecimal = new BigDecimal(rate).setScale(6, BigDecimal.ROUND_CEILING);
            if (ExchangeRatesDAO.getInstance().findByIds(currencyBaseDto.getId(), currencyTargetDto.getId()).isPresent()) {
                throw new ExchangeRateException("exchange rate already exists");
            }
            int id = ExchangeRatesDAO.getInstance().save(currencyBaseDto.getId(), currencyTargetDto.getId(), rateBigDecimal);
            return new ExchangeRatesDto(id, currencyBaseDto, currencyTargetDto, rateBigDecimal);
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rate", e);
        }
    }

    public List<ExchangeRatesDto> getExchangeRates() throws DataAccessException {
        try {
            List<ExchangeRate> list = ExchangeRatesDAO.getInstance().findAll();
            return list.stream()
                    .map(ExchangeRate::toDto)
                    .toList();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeRatesDto getExchangeRate(
            String baseCurrencyCode, String targetCurrencyCode) throws CurrencyNotFoundException, DataAccessException,
            ExchangeRateException {
        try {
            Currency baseCurrency = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(baseCurrencyCode));
            Currency targetCurrency = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(targetCurrencyCode));

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId)
                    .orElseThrow(() -> new ExchangeRateException("exchange rate not found"));
            return exchangeRate.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeRatesDto updateExchangeRate(
            String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) throws CurrencyNotFoundException,
            DataAccessException, ExchangeRateException {
        try {
            Currency baseCurrency = CurrenciesDao.getInstance().findByCode(baseCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(baseCurrencyCode));
            Currency targetCurrency = CurrenciesDao.getInstance().findByCode(targetCurrencyCode).orElseThrow(
                    () -> new CurrencyNotFoundException(targetCurrencyCode));

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRate exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrencyId, targetCurrencyId)
                    .orElseThrow(() -> new ExchangeRateException("exchange rate not found"));
            ExchangeRatesDAO.getInstance().updateByIds(baseCurrencyId, targetCurrencyId, rate);
            exchangeRate.setRate(rate);
            return exchangeRate.toDto();
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }

    public ExchangeDto getExchange(String from, String to, String amount) throws DataAccessException,
            CurrencyNotFoundException, ExchangeRateException {
        try {
            Currency baseCurrency = CurrenciesDao.getInstance().findByCode(from).orElseThrow(
                    () -> new CurrencyNotFoundException(from)
            );
            Currency targetCurrency = CurrenciesDao.getInstance().findByCode(to).orElseThrow(
                    () -> new CurrencyNotFoundException(to)
            );

            BigDecimal amountBigDecimal = new BigDecimal(amount).setScale(6, BigDecimal.ROUND_CEILING);
            ExchangeRate exchangeRate;
            BigDecimal convertedAmount;

            if (ExchangeRatesDAO.getInstance().findByIds(baseCurrency.getId(), targetCurrency.getId()).isPresent()) {
                exchangeRate = ExchangeRatesDAO.getInstance().findByIds(baseCurrency.getId(), targetCurrency.getId()).get();
                convertedAmount = exchangeRate.getRate().multiply(amountBigDecimal).setScale(
                        6, BigDecimal.ROUND_CEILING);
                return new ExchangeDto(baseCurrency.toDto(), targetCurrency.toDto(), exchangeRate.getRate(),
                        amountBigDecimal, convertedAmount);
            } else if (ExchangeRatesDAO.getInstance().findByIds(targetCurrency.getId(), baseCurrency.getId()).isPresent()) {
                exchangeRate = ExchangeRatesDAO.getInstance().findByIds(targetCurrency.getId(), baseCurrency.getId()).get();
                BigDecimal rate = BigDecimal.ONE.divide((exchangeRate.getRate().multiply(amountBigDecimal))).setScale(
                        6, BigDecimal.ROUND_CEILING);
                convertedAmount = rate.multiply(amountBigDecimal).setScale(6, BigDecimal.ROUND_CEILING);
                return new ExchangeDto(baseCurrency.toDto(), targetCurrency.toDto(), rate, amountBigDecimal,
                        convertedAmount);
            } else {
                //USD-A
                //USD-B

                Currency currencyUSD = CurrenciesDao.getInstance().findByCode(CURRENCY_USD).orElseThrow(
                        () -> new CurrencyNotFoundException("currency not found")
                );
                ExchangeRate exchangeRate1 = ExchangeRatesDAO.getInstance().findByIds(currencyUSD.getId(),
                        baseCurrency.getId()).orElseThrow(
                        () -> new ExchangeRateException("exchange rate not found")
                );
                ExchangeRate exchangeRate2 = ExchangeRatesDAO.getInstance().findByIds(currencyUSD.getId(),
                        targetCurrency.getId()).orElseThrow(
                        () -> new ExchangeRateException("exchange rate not found")
                );
                BigDecimal rate = BigDecimal.ONE.divide(exchangeRate1.getRate()).multiply(BigDecimal.ONE.divide(
                        exchangeRate2.getRate())).setScale(6, BigDecimal.ROUND_CEILING);
                convertedAmount = rate.multiply(amountBigDecimal).setScale(6, BigDecimal.ROUND_CEILING);

                return new ExchangeDto(baseCurrency.toDto(), targetCurrency.toDto(), rate, amountBigDecimal,
                        convertedAmount);
            }
        } catch (DaoException e) {
            throw new DataAccessException("cannot get exchange rates", e);
        }
    }
}
