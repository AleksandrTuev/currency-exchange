package com.dev.util;

public class ValidationUtil {
    public static boolean validateParametersCurrency(String code, String name, String sign) {
        //TODO сделать логи
        if ((code == null) || (name == null) || (sign == null) || (code.isEmpty()) || (name.isEmpty()) ||
            (sign.isEmpty()))  {
            return false;
        }

        if (code.length() != 3) {
            return false;
        }

        if (sign.length() != 1) {
            return false;
        }

        for (char c : code.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c)) {
                if (!(c == '(' || c == ')' || c == ' ')) {
                    return false;
                }
            }
        }

        return true;
    }
}
