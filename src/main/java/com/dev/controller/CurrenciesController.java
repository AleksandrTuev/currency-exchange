package com.dev.controller;

import com.dev.dto.CurrencyDto;
import com.dev.dto.ErrorResponseDto;
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
import java.util.List;
import java.util.Map;

import static com.dev.util.ProjectConstants.*;

@WebServlet("/currencies")
public class CurrenciesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDto> list = CurrenciesService.getInstance().getCurrencies();

//            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(list));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_OK, list);
        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (CurrencyNotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            resp.getWriter().write("error: " + e.getMessage());
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
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            resp.setContentType("application/json");
//            resp.setCharacterEncoding("UTF-8");
//            resp.getWriter().write(new Gson().toJson(currencyDto));
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CREATED, currencyDto);

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resp.getWriter().write("error: " + e.getMessage());
            resp.getWriter().write(new Gson().toJson(Map.of("error", e.getMessage())));
////            resp.getWriter().write("{\"error\": \"Currency not found\"}");
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_BAD_REQUEST, new ErrorResponseDto(e.getMessage()));
        } catch (CurrencyNotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_CONFLICT);
//            resp.getWriter().write("error: " + e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_CONFLICT, new ErrorResponseDto(e.getMessage()));
        } catch (DataAccessException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            resp.getWriter().write(e.getMessage());
            JsonResponseWriter.writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, new ErrorResponseDto(e.getMessage()));
        }
    }
}
