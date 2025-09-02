package com.dev.controller;

import com.dev.dto.ErrorResponseDto;
import com.dev.dto.ExchangeRatesDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.ExchangeRatesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

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

            ExchangeRatesDto exchangeRatesDto = ExchangeRatesService.getInstance().getExchangeRate(baseCurrencyCode,
                    targetCurrencyCode);

            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, exchangeRatesDto);

        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponseDto(e.getMessage()));

        } catch (CurrencyNotFoundException | ExchangeRateException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    new ErrorResponseDto(e.getMessage()));

        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponseDto(e.getMessage()));

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

            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, exchangeRatesDto);

        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponseDto(e.getMessage()));

        } catch (CurrencyNotFoundException | ExchangeRateException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    new ErrorResponseDto(e.getMessage()));

        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponseDto(e.getMessage()));

        }
    }
}
