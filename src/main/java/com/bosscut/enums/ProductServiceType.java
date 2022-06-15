package com.bosscut.enums;

public enum ProductServiceType {
    SERVICE("SERVICE"),
    PRODUCT("PRODUCT");

    private String name;

    ProductServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
