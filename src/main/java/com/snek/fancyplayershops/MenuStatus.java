package com.snek.fancyplayershops;

public enum MenuStatus {
    IDLE(0),
    DETAILS(1),
    OWNER_EDIT(2),
    CLIENT_BUY(3);

    private final int value;

    MenuStatus(int value) {
        this.value = value;
    }
}
