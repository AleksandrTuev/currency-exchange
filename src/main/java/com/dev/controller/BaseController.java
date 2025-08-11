package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.model.entity.Currency;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/// GET /currencies #
/// Получение списка валют.
///
/// GET /currency/EUR #
/// Получение конкретной валюты.
///
///POST /currencies #
/// Добавление новой валюты в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded).
/// Поля формы - name, code, sign. Пример ответа - JSON представление вставленной в базу записи, включая её ID:
///
/// GET /exchangeRates #
/// Получение списка всех обменных курсов.
///
/// GET /exchangeRate/USDRUB #
/// Получение конкретного обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса.
///
/// POST /exchangeRates #
/// Добавление нового обменного курса в базу. Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded).
/// Поля формы - baseCurrencyCode, targetCurrencyCode, rate.
///
/// PATCH /exchangeRate/USDRUB #
/// Обновление существующего в базе обменного курса. Валютная пара задаётся идущими подряд кодами валют в адресе запроса.
/// Данные передаются в теле запроса в виде полей формы (x-www-form-urlencoded). Единственное поле формы - rate.
/// Пример ответа - JSON представление обновлённой записи в базе данных, включая её ID:
///
/// Обмен валюты #
/// GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT #
/// Расчёт перевода определённого количества средств из одной валюты в другую.
/// Пример запроса - GET /exchange?from=USD&to=AUD&amount=10.
///
///

public class BaseController extends HttpServlet {
    //handleError(), validateRequest(), sendJsonResponse()
    //Не должен быть синглетон

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("text/html");
//        try (PrintWriter out = resp.getWriter()) {
//            out.write("<h1>Hello World mother fucka</h1>");
//        }
//    }


    protected String serializeToJson(CurrencyDto currencyDto) {
        //Сделать проверку на null, на правильность данных (хотя может не понадобиться т к уже объект передаётся)
        Gson gson = new Gson();
        return gson.toJson(currencyDto);
    }

    protected CurrencyDto deserializeFromJson(String json) {
        //Сделать проверку на null, на правильность данных (на уровне BaseController)
        Gson gson = new Gson();
        return gson.fromJson(json, CurrencyDto.class);
    }
}
