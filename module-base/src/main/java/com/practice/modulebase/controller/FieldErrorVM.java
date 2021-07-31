package com.practice.modulebase.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter @Getter @NoArgsConstructor
public class FieldErrorVM {
    private String code;
    private String field;
    private String message;

    public FieldErrorVM(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public FieldErrorVM(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }
}
