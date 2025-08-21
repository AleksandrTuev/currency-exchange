package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.CurrenciesService;
import com.dev.service.ExchangeRatesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
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
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(new Gson().toJson(list));

        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } catch (ExchangeRateException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("error: " + e.getMessage());
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
            ValidationUtil.checkRate(rate);

            CurrencyDto currencyBaseDto = CurrenciesService.getInstance().getCurrencyByCode(baseCurrencyCode);
            CurrencyDto currencyTargetDto = CurrenciesService.getInstance().getCurrencyByCode(targetCurrencyCode);

            ExchangeRatesDto exchangeRatesDTO = ExchangeRatesService.getInstance().saveExchangeRates(currencyBaseDto,
                    currencyTargetDto, rate);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(exchangeRatesDTO));

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("error: " + e.getMessage());
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("error: currency " + e.getMessage() + " not found");
        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } catch (ExchangeRateException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write(e.getMessage());
        }
    }
}
