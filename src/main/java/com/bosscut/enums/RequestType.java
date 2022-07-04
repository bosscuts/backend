package com.bosscut.enums;

public enum RequestType {
    SERVICE("SERVICE"),
    CASH("CASH"),
    HOLIDAY("HOLIDAY"),
    PAY_FINES("PAY_FINES");

    private String name;

    RequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
