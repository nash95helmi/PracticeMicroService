package com.practice.modulebatch.exception;

public class BatchProcessException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    protected BatchErrorType type;
    protected String message;
    protected String additionalInfo;

    public BatchProcessException() {
        super();
    }

    public BatchProcessException(BatchErrorType type, String message) {
        this.type = type;
        this.message = message;
    }

    public BatchProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BatchProcessException(BatchErrorType systemSprbatchErr, Throwable e) {
        this.type = type;
        this.message = e.toString();
        if (e instanceof BatchProcessException) {
            this.additionalInfo = ((BatchProcessException)e).getAdditionalInfo();
        } else {
            this.additionalInfo = e.getCause() != null ? e.getCause().toString() : null;
        }
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

}
