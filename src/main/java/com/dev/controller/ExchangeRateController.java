package com.dev.controller;

import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.ExchangeRatesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equals("PATCH")) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String stringRequestCurrencyPair = req.getPathInfo();

            ValidationUtil.checkCurrencyPair(stringRequestCurrencyPair);

            String baseCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE,
                    INDEX_LAST_LETTER_BASE_CURRENCY_CODE).toUpperCase();
            String targetCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_TARGET_CURRENCY_CODE,
                    INDEX_LAST_LETTER_TARGET_CURRENCY_CODE).toUpperCase();

            ExchangeRatesDto exchangeRatesDto = ExchangeRatesService.getInstance().getExchangeRate(baseCurrencyCode, targetCurrencyCode);

//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(exchangeRatesDto));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, exchangeRatesDto);

        } catch (ValidationException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("error: invalid exchange rate");
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException | ExchangeRateException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("error: currency " + e.getMessage() + " not found");
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (ExchangeRateException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write(e.getMessage());
//            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String tmp = req.getReader().readLine();
            String parameterRate = tmp.substring(INDEX_FIRST_LETTER_PARAMETER_RATE);

            ValidationUtil.checkBigDecimalNumber(parameterRate);

            BigDecimal rate = new BigDecimal(parameterRate);
            String stringRequestCurrencyPair = req.getPathInfo();

            ValidationUtil.checkCurrencyPair(stringRequestCurrencyPair);

            String baseCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE,
                    INDEX_LAST_LETTER_BASE_CURRENCY_CODE).toUpperCase();
            String targetCurrencyCode = stringRequestCurrencyPair.substring(INDEX_FIRST_LETTER_TARGET_CURRENCY_CODE,
                    INDEX_LAST_LETTER_TARGET_CURRENCY_CODE).toUpperCase();

            ExchangeRatesDto exchangeRatesDto = ExchangeRatesService.getInstance().updateExchangeRate(baseCurrencyCode,
                    targetCurrencyCode, rate);

//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(exchangeRatesDto));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, exchangeRatesDto);

        } catch (ValidationException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("error: " + e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException | ExchangeRateException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("error: currency " + e.getMessage() + " not found");
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (ExchangeRateException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write(e.getMessage());
//            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
