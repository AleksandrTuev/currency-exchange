package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ErrorResponseDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ValidationException;
import com.dev.service.CurrenciesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.dev.util.ProjectConstants.INDEX_FIRST_LETTER_BASE_CURRENCY_CODE;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (req.getPathInfo().substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE).isEmpty()) {
                throw new ValidationException("сurrency not transferred");
            }

            String CurrencyCode = req.getPathInfo().substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE).toUpperCase();

            ValidationUtil.checkCurrencyCode(CurrencyCode);

            CurrencyDto currencyDto = CurrenciesService.getInstance().getCurrencyByCode(CurrencyCode);
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, currencyDto);
        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new ErrorResponseDto(e.getMessage()));

        } catch (CurrencyNotFoundException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    new ErrorResponseDto(e.getMessage()));

        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    new ErrorResponseDto(e.getMessage()));
        }
    }
}
