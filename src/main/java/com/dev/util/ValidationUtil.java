package com.dev.util;

public class ValidationUtil {
    private static final int LENGTH_CODE = 3;
    private static final int LENGTH_SIGN = 1;

    public static boolean validateParametersCurrency(String code, String name, String sign) {

        //TODO сделать логи
        if (isNullOrEmpty(code)) {
            return false;
        }

        if (isNullOrEmpty(name)) {
            return false;
        }

        if (isNullOrEmpty(sign)) {
            return false;
        }

        if (hasExactLength(code, LENGTH_CODE)) {
            return false;
        }

        if (hasExactLength(sign, LENGTH_SIGN)) {
            return false;
        }

        if (containsNonLetter(code)) {
            return false;
        }

        if (isOnlyLettersAndBrackets(name)) {
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

    private static boolean hasExactLength(String str, int length) {
        return str.length() != length;
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
}
