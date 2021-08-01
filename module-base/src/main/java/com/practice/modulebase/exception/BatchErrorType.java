package com.practice.modulebase.exception;

public enum BatchErrorType {
    APPLICATION_GENERAL_ERR("A01","General Error"),
    APPLICATION_DATA_NOT_FOUND("A02", "Data not found"),
    APPLICATION_FILE_NOT_FOUND("A03", "File not found"),
    SYSTEM_GENERAL_ERROR("A04", "System general error"),
    SYSTEM_JPA_ERROR("A05", "System Jpa error"),
    SYSTEM_SPRBATCH_ERR("A06", "System Spring Batch Error");

    private String value;
    private String label;

    private BatchErrorType(String value, String label) {
        this.value = value;
        this.label = label;

        if(value.length() > 3)
            throw new IllegalArgumentException("Must be 3 chars or less");
    }

    public static BatchErrorType getEnum(String value) {
        if (value != null) {
            value = value.trim();
            BatchErrorType[] var1 = values();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                BatchErrorType e = var1[var3];
                if (e.value.equals(value)) {
                    return e;
                }
            }
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }
}
