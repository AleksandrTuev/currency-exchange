package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.model.entity.Currency;
import com.dev.service.CurrencyService;
import com.dev.util.ValidationUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet("/currencies")
public class CurrencyController extends BaseController {
    private static final String PARAMETER_CODE = "code";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_SIGN = "sign";

    //Взаимодействует с: CurrencyService, ValidationUtil
    //проверить если на ссылки приходят другие методы, то что выкидывается (д.б. ошибка 4**)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter(PARAMETER_CODE).toUpperCase().trim();
        String name = req.getParameter(PARAMETER_NAME).trim();
        String sign = req.getParameter(PARAMETER_SIGN).trim();

        if (!ValidationUtil.validateParametersCurrency(code, name, sign)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid parameters\"}");
            return;
        }

        CurrencyDto currencyDto = new CurrencyDto(code, name, sign);
        Currency currency = CurrencyService.getInstance().saveCurrency(currencyDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(new Gson().toJson(currency));
    }
}
