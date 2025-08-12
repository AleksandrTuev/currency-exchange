package com.dev.controller;

import com.dev.model.entity.Currency;
import com.dev.service.CurrenciesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo().substring(1).isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Сurrency code is missing in the address\"}");
            return;
        }

        String CurrencyCode = req.getPathInfo().substring(1).toUpperCase();

        if (ValidationUtil.validateCurrencyCode(CurrencyCode)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid currency code\"}");
            return;
        }

        if (!CurrenciesService.getInstance().hasCurrency(CurrencyCode)) {
            //не отправляем страницу с ошибкой 404
//            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Currency not found\"}");
            return;
        }
        Currency currency = CurrenciesService.getInstance().getCurrencyByCode(CurrencyCode);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(new Gson().toJson(currency));
    }
}
