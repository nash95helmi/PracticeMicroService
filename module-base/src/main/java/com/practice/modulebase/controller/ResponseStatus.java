package com.practice.modulebase.controller;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {
    SUCCESS("success"), FAILED("failed");

    private String label;
    ResponseStatus(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
