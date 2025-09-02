package com.dev.controller;

import com.dev.dto.ErrorResponseDto;
import com.dev.dto.ExchangeDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.ExchangeRatesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.dev.util.ProjectConstants.*;

@WebServlet ("/exchange")
public class ExchangeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String from = req.getParameter(PARAMETER_FROM);
            String to = req.getParameter(PARAMETER_TO);
            String amount = req.getParameter(PARAMETER_AMOUNT);

            ValidationUtil.checkCurrencyCode(from);
            ValidationUtil.checkCurrencyCode(to);
            ValidationUtil.checkBigDecimalNumber(amount);

            ExchangeDto exchangeDto = ExchangeRatesService.getInstance().getExchange(from, to, amount);
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CREATED, exchangeDto);

        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponseDto(e.getMessage()));

        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponseDto(e.getMessage()));

        } catch (CurrencyNotFoundException | ExchangeRateException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    new ErrorResponseDto(e.getMessage()));
        }
    }
}
