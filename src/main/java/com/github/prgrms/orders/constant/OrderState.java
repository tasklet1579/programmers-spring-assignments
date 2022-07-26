package com.github.prgrms.orders.constant;

public enum OrderState {
    REQUESTED("REQUESTED"),
    REJECTED("REJECTED"),
    ACCEPTED("ACCEPTED"),
    SHIPPING("SHIPPING"),
    COMPLETED("COMPLETED");

    private String state;

    OrderState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
