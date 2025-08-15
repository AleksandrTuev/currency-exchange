package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.service.CurrenciesService;
import com.dev.service.ExchangeRatesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet ("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    private static final String PARAMETER_BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String PARAMETER_TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String PARAMETER_RATE = "rate";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
        Получение списка всех обменных курсов. Пример ответа:
        */
        List<ExchangeRatesDto> list = ExchangeRatesService.getInstance().getExchangeRates();
        //TODO обработать ошибки
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(new Gson().toJson(list));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter(PARAMETER_BASE_CURRENCY_CODE).toUpperCase();
        String targetCurrencyCode = req.getParameter(PARAMETER_TARGET_CURRENCY_CODE).toUpperCase();
        String rate = req.getParameter(PARAMETER_RATE);

        if ((!ValidationUtil.validateParameterCode(baseCurrencyCode)) ||
            (!ValidationUtil.validateParameterCode(targetCurrencyCode)) ||
            (!ValidationUtil.validateParameterRate(rate))) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid parameters\"}");
            return;
        }

        BigDecimal rateBigDecimal = new BigDecimal(rate).setScale(6, BigDecimal.ROUND_CEILING);

        if (!CurrenciesService.getInstance().hasCurrency(baseCurrencyCode)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Currency " + baseCurrencyCode + " not found\"}");
            return;
        }

        if (!CurrenciesService.getInstance().hasCurrency(targetCurrencyCode)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Currency " + targetCurrencyCode + " not found\"}");
            return;
        }

        CurrencyDto currencyBaseDto = CurrenciesService.getInstance().getCurrencyByCode(baseCurrencyCode);
        CurrencyDto currencyTargetDto = CurrenciesService.getInstance().getCurrencyByCode(targetCurrencyCode);

        ExchangeRatesDto exchangeRatesDTO = ExchangeRatesService.getInstance().saveExchangeRates(currencyBaseDto,
                currencyTargetDto, rateBigDecimal);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(exchangeRatesDTO));
    }
}
