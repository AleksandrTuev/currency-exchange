package com.dev.util;

import java.math.BigDecimal;

import static com.dev.util.ProjectConstants.*;

public class ValidationUtil {

    public static boolean validateParametersCurrency(String code, String name, String sign) {

        //TODO сделать логи
        if (!validateParameterCode(code)) {
            return false;
        }

        if (isNullOrEmpty(name)) {
            return false;
        }

        if (isNullOrEmpty(sign)) {
            return false;
        }

        if (!isLengthValid(sign)) {
            return false;
        }

        if (!isOnlyLettersAndBrackets(name)) {
            return false;
        }

        return true;
    }

    public static boolean validateParameterCode(String currencyCode) {
        if (isNullOrEmpty(currencyCode)) {
            return false;
        }

        if (hasExactLength(currencyCode)) {
            return false;
        }

        return !containsNonLetter(currencyCode);
    }

    private static boolean isNullOrEmpty(String str) {
        return str.isBlank();
    }

    private static boolean hasExactLength(String str) { //имеет точную длину
        return str.length() != LENGTH_CODE;
    }

    private static boolean isLengthValid(String str) {
        return str.length() <= LENGTH_SIGN;
    }

    private static boolean containsNonLetter(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOnlyLettersAndBrackets(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                if (!(c == '(' || c == ')' || c == ' ')) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isOnlyNumbersAndPoint(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                if (!(c == '.')) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validateParameterRate(String rate) {
        if (isNullOrEmpty(rate)) {
            return false;
        }

        if (!isOnlyNumbersAndPoint(rate)) {
            return false;
        }

        BigDecimal rateBigDecimal = new BigDecimal(rate);

        return rateBigDecimal.signum() > 0;
    }

    public static boolean isCurrencyPairValid(String currencyPair) {
        if (isNullOrEmpty(currencyPair)) {
            return false;
        }

        if (currencyPair.length() != LENGTH_STRING_REQUEST_CURRENCY_PAIR) {
            return false;
        }

        String baseCurrencyCode = currencyPair.substring(INDEX_FIRST_LETTER_BASE_CURRENCY_CODE,
                INDEX_LAST_LETTER_BASE_CURRENCY_CODE);
        String targetCurrencyCode = currencyPair.substring(INDEX_FIRST_LETTER_TARGET_CURRENCY_CODE,
                INDEX_LAST_LETTER_TARGET_CURRENCY_CODE);

        if (!validateParameterCode(baseCurrencyCode)) {
            return false;
        }

        return validateParameterCode(targetCurrencyCode);
    }
}
