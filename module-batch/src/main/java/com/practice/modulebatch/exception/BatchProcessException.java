package com.practice.modulebatch.exception;

public class BatchProcessException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    protected BatchErrorType type;
    protected String message;
    protected String additionalInfo;

    public BatchProcessException(BatchErrorType type, String message) {
        this.type = type;
        this.message = message;
    }
}
