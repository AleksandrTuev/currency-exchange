package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ValidationException;
import com.dev.service.CurrenciesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
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
//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(currencyDto));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, currencyDto);
        } catch (ValidationException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("error: " + e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("error: currency " + e.getMessage() + " not found");
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
