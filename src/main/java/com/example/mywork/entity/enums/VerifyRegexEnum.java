package com.example.mywork.entity.enums;

public enum VerifyRegexEnum {

    NO("","不校验"),
    EMALL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$","邮箱检验"),
    PASSWORD("^[a-zA-Z0-9!@#$%^&*(),.?\\\":{}|<>]{8,18}$","密码检验");
    private String regex;

    private String desc;

    VerifyRegexEnum(String regex, String desc) {
        this.regex = regex;
        this.desc = desc;
    }
    public String getRegex() {
        return this.regex;
    }
    public String getDesc() {
        return this.desc;
    }
}
