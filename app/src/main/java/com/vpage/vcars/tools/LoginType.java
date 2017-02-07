package com.vpage.vcars.tools;

public enum LoginType {
    VCars(0),
    GoogleApp(1),
    FacebookApp(2);

    private final int LoginTypeCode;

    LoginType(int LoginTypeCode) {
        this. LoginTypeCode = LoginTypeCode;
    }

    public int getLoginTypeCode() {
        return LoginTypeCode;
    }
    }