package com.dev.dto;

public class CurrencyDto {
    private Integer id;
    private final String code;
    private final String name;
    private final String sign;

    public CurrencyDto (Integer id, String code, String name, String sign) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public CurrencyDto (String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "CurrencyDto{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", name='" + name + '\'' +
               ", sign='" + sign + '\'' +
               '}';
    }
}
