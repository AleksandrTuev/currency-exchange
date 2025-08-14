package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.service.CurrenciesService;
import com.dev.service.ExchangeRatesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet ("/exchangeRates")
public class ExchangeRatesController extends HttpServlet {
    private static final String PARAMETER_BASE_CURRENCY_CODE = "baseCurrencyCode";
    private static final String PARAMETER_TARGET_CURRENCY_CODE = "targetCurrencyCode";
    private static final String PARAMETER_RATE = "rate";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        /exchangeRate/USDRUB #
        Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса.
        */
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        //TODO посмотреть когда и как используется DTO объект
//        ExchangeRatesDto exchangeRatesDTO = new ExchangeRatesDto(currencyBase.getId(), currencyTarget.getId(), rate);

        ExchangeRatesDto exchangeRatesDTO = ExchangeRatesService.getInstance().saveExchangeRates(currencyBaseDto,
                currencyTargetDto, rateBigDecimal);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(exchangeRatesDTO));
    }
}
