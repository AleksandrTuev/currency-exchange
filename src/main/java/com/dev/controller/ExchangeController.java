package com.dev.controller;

import com.dev.dto.ExchangeDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ExchangeRateException;
import com.dev.exception.ValidationException;
import com.dev.service.ExchangeRatesService;
import com.dev.util.JsonResponseWriter;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
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
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(exchangeDto));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CREATED, exchangeDto);

        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ValidationException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("error: " + e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException | ExchangeRateException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("error: " + e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
