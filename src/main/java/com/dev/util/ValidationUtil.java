package com.dev.util;

import com.dev.exception.ValidationException;

import java.math.BigDecimal;

import static com.dev.util.ProjectConstants.*;

public class ValidationUtil {

    public static void checkParametersCurrency(String code, String name, String sign) {
        checkCurrencyCode(code);
        checkCurrencyName(name);
        checkCurrencySign(sign);
    }

    public static void checkCurrencyPair(String currencyPair) {
        checkOnNullAndEmpty(currencyPair);
        checkExactLength(currencyPair, LENGTH_STRING_REQUEST_CURRENCY_PAIR);

        String baseCurrencyCode = currencyPair.substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE,
                INDEX_LAST_LETTER_BASE_CURRENCY_CODE);
        String targetCurrencyCode = currencyPair.substring(INDEX_FIRST_LETTER_TARGET_CURRENCY_CODE,
                INDEX_LAST_LETTER_TARGET_CURRENCY_CODE);

        checkCurrencyCode(baseCurrencyCode);
        checkCurrencyCode(targetCurrencyCode);
    }

    public static void checkBigDecimalNumber(String rate) {
        checkOnNullAndEmpty(rate);
        checkOnNumbersAndPoint(rate);
        BigDecimal rateBigDecimal = new BigDecimal(rate);
        checkNumber(rateBigDecimal);
    }

    public static void checkCurrencyCode(String currencyCode) {
        checkOnNullAndEmpty(currencyCode);
        checkExactLength(currencyCode, LENGTH_CODE);
        checkOnNonLetter(currencyCode);
    }

    private static void checkCurrencyName(String currencyName){
        checkOnNullAndEmpty(currencyName);
        checkOnLetterAndBrackets(currencyName);
    }

    private static void checkCurrencySign(String CurrencySign){
        checkOnNullAndEmpty(CurrencySign);
        checkLengthValid(CurrencySign);
    }

    private static void checkOnNullAndEmpty(String str) {
        if (str.isBlank()) {
            throw new ValidationException(String.format("%s is blank", str));
        }
    }

    private static void checkExactLength(String str, int length) {
        if (str.length() != length) {
            throw new ValidationException(String.format("length %s is not %d", str, length));
        }
    }

    private static void checkLengthValid(String str) {
        if (str.length() > LENGTH_SIGN) {
            throw new ValidationException(String.format("length %s is too long", str));
        }
    }

    private static void checkOnNonLetter(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new ValidationException(String.format("invalid %s", str));
            }
        }
    }

    private static void checkOnLetterAndBrackets(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                if (!(c == '(' || c == ')' || c == ' ')) {
                    throw new ValidationException(String.format("invalid %s", str));
                }
            }
        }
    }

    private static void checkOnNumbersAndPoint(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (!(c == '.')) {
                    throw new ValidationException(String.format("invalid %s (\",\" -> \".\")", str));
                }
            }
        }
    }

    private static void checkNumber(BigDecimal rate) {
        if (rate.signum() <= 0) {
            throw new ValidationException(String.format("%s <= 0", rate));
        }
    }
}
