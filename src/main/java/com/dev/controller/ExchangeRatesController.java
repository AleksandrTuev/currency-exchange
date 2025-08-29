package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.CurrenciesService;
import com.dev.service.ExchangeRatesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.dev.util.ProjectConstants.*;

@WebServlet ("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<ExchangeRatesDto> list = ExchangeRatesService.getInstance().getExchangeRates();
            if (list.isEmpty()) {
                throw new ExchangeRateException("exchange rates not found");
            }
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, list);

        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ExchangeRateException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter(PARAMETER_BASE_CURRENCY_CODE).toUpperCase();
            String targetCurrencyCode = req.getParameter(PARAMETER_TARGET_CURRENCY_CODE).toUpperCase();
            String rate = req.getParameter(PARAMETER_RATE);

            ValidationUtil.checkCurrencyCode(baseCurrencyCode);
            ValidationUtil.checkCurrencyCode(targetCurrencyCode);
            ValidationUtil.checkBigDecimalNumber(rate);

            CurrencyDto currencyBaseDto = CurrenciesService.getInstance().getCurrencyByCode(baseCurrencyCode);
            CurrencyDto currencyTargetDto = CurrenciesService.getInstance().getCurrencyByCode(targetCurrencyCode);

            ExchangeRatesDto exchangeRatesDTO = ExchangeRatesService.getInstance().saveExchangeRates(currencyBaseDto,
                    currencyTargetDto, rate);

            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CREATED, exchangeRatesDTO);

        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ExchangeRateException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CONFLICT, e.getMessage());
        }
    }
}
