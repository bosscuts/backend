package com.bosscut.enums;

public enum InvoiceType {
    INTERNAL("INTERNAL"),
    EXTERNAL("ASSISTANT");

    private String name;

    InvoiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
