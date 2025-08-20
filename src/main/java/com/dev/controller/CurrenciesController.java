package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.exception.CurrencyNotFoundException;
import com.dev.exception.DataAccessException;
import com.dev.exception.ValidationException;
import com.dev.model.entity.Currency;
import com.dev.service.CurrenciesService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.dev.util.ProjectConstants.*;

@WebServlet("/currencies")
public class CurrenciesController extends BaseController {
    //TODO либо сделать единый базовый класс, либо убрать

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDto> list = CurrenciesService.getInstance().getCurrencies();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(list));
        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter(PARAMETER_CODE).toUpperCase().trim();
            String name = req.getParameter(PARAMETER_NAME).trim();
            String sign = req.getParameter(PARAMETER_SIGN).trim();

            if (!ValidationUtil.validateParametersCurrency(code, name, sign)) {
                throw new ValidationException("invalid parameters");
            }

            CurrencyDto currencyDto = CurrenciesService.getInstance().saveCurrency(new CurrencyDto(code, name, sign));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(currencyDto));

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("error: " + e.getMessage());
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("error: " + e.getMessage());
        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
