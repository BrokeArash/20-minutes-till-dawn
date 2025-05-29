package com.tilldawn.model.enums;


public enum SignupEnums {
    CHECK_PASSWORD("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%&*)(_])[a-zA-Z0-9@#$%&*)(_]{8,}$"),
    ;


    String pattern;

    SignupEnums(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
