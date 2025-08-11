package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.model.entity.Currency;
import com.dev.service.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet("/currencies")
public class CurrencyController extends BaseController {
    //Взаимодействует с: CurrencyService, ValidationUtil
    //проверить если на ссылки приходят другие методы, то что выкидывается (д.б. ошибка 4**)


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (InputStream inputStream = req.getInputStream()) {
            // Чтение JSON из запроса
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            // Десериализация в объект
            CurrencyDto currencyDto = deserializeFromJson(json);

            // Проверка, что объект создан
            if (currencyDto == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Failed to parse JSON\"}");
                return;
            }

            //dto отправлен в слой Service оттуда через слой DAO перенаправлен в БД
//            CurrencyService.getInstance().saveCurrency(currencyDto);
            Currency currency = CurrencyService.getInstance().saveCurrency(currencyDto);

            // Отправка ответа
            resp.setContentType("application/json");
//            resp.getWriter().write(new Gson().toJson(currencyDto));
            resp.getWriter().write(new Gson().toJson(currency));


        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


}
