package com.dev.util;

import java.math.BigDecimal;

public class ValidationUtil {
    private static final int LENGTH_CODE = 3;
    private static final int LENGTH_SIGN = 3;

    public static boolean validateParametersCurrency(String code, String name, String sign) {

        //TODO сделать логи
        if (!validateParameterCode(code)) {
            return false;
        }
//        if (isNullOrEmpty(code)) {
//            return false;
//        }

        if (isNullOrEmpty(name)) {
            return false;
        }

        if (isNullOrEmpty(sign)) {
            return false;
        }

//        if (hasExactLength(code)) {
//            return false;
//        }

        if (!isLengthValid(sign)) {
            return false;
        }

//        if (containsNonLetter(code)) {
//            return false;
//        }

        if (!isOnlyLettersAndBrackets(name)) {
            return false;
        }

        return true;
    }

    public static boolean validateCurrencyCode(String currencyCode) {
        if (isNullOrEmpty(currencyCode)) {
            return false;
        }
        return containsNonLetter(currencyCode);
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

    private static boolean isOnlyNumbersAndPeriod(String str) {
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

        if (!isOnlyNumbersAndPeriod(rate)) {
            return false;
        }

        BigDecimal rateBigDecimal = new BigDecimal(rate);

        return rateBigDecimal.signum() > 0;
    }

    public static boolean validateParameterCode(String code) {
        if (isNullOrEmpty(code)) {
            return false;
        }

        if (hasExactLength(code)) {
            return false;
        }

        return !containsNonLetter(code);
    }
}
