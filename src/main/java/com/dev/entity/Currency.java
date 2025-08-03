package com.dev.entity;

public class Currency {
    private final int id;
    private final String code;
    private final String fullName;
    private final String sign;

    public Currency(String fullName, String sign, String code, int id) {
        this.fullName = fullName;
        this.sign = sign;
        this.code = code;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", fullName='" + fullName + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
