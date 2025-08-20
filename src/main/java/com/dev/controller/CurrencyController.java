package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ValidationException;
import com.dev.service.CurrenciesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.dev.util.ProjectConstants.INDEX_FIRST_LETTER_BASE_CURRENCY_CODE;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (req.getPathInfo().substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE).isEmpty()) {
                throw new ValidationException("сurrency not transferred");
            }

            String CurrencyCode = req.getPathInfo().substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE).toUpperCase();

            if (!ValidationUtil.validateParameterCode(CurrencyCode)) {
                throw new ValidationException("invalid currency code");
            }

            CurrencyDto currencyDto = CurrenciesService.getInstance().getCurrencyByCode(CurrencyCode);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(currencyDto));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("error: " + e.getMessage());
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("error: currency " + e.getMessage() + " not found");
        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
