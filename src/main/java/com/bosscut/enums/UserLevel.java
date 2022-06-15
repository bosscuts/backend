package com.bosscut.enums;

public enum UserLevel {
    MASTER("MASTER"),
    ASSISTANT("ASSISTANT");

    private String name;

    UserLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
