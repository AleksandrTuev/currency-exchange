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
import java.util.List;

import static com.dev.util.ProjectConstants.*;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDto> list = CurrenciesService.getInstance().getCurrencies();

            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, list);
        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (CurrencyNotFoundException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter(PARAMETER_CODE).toUpperCase().trim();
            String name = req.getParameter(PARAMETER_NAME).trim();
            String sign = req.getParameter(PARAMETER_SIGN).trim();

            ValidationUtil.checkParametersCurrency(code, name, sign);

            CurrencyDto currencyDto = CurrenciesService.getInstance().saveCurrency(new CurrencyDto(code, name, sign));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CREATED, currencyDto);

        } catch (ValidationException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponseDto(e.getMessage()));
        } catch (CurrencyNotFoundException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CONFLICT, new ErrorResponseDto(e.getMessage()));
        } catch (DataAccessException e) {
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponseDto(e.getMessage()));
        }
    }
}
