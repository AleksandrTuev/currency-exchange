package com.dev.controller;

import com.dev.dto.ExchangeRatesDto;
import com.dev.service.ExchangeRatesService;
import com.dev.util.ProjectConstants;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static com.dev.util.ProjectConstants.*;

@WebServlet ("/exchangeRate/*")
public class ExchangeRateController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса
        String stringRequestCurrencyPair = req.getPathInfo();

        if (!ValidationUtil.isCurrencyPairValid(stringRequestCurrencyPair)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid exchange rate\"}");
            return;
        }

        String baseCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE,
                INDEX_LAST_LETTER_BASE_CURRENCY_CODE).toUpperCase();
        String targetCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_TARGET_CURRENCY_CODE,
                INDEX_LAST_LETTER_TARGET_CURRENCY_CODE).toUpperCase();

        ExchangeRatesDto exchangeRatesDto = ExchangeRatesService.getInstance().getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        //TODO выкидывать исключение если такой пары нет
        //TODO выкидывать исключение если даже одной валюты нет

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(exchangeRatesDto));

        int a = 123;

    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BigDecimal rate = new BigDecimal(req.getParameter(PARAMETER_RATE));

        String baseCurrencyCode = req.getPathInfo().substring(1, 4);
        String targetCurrencyCode = req.getPathInfo().substring(4, 7);

//        ExchangeRatesService.getInstance().

    }
}
