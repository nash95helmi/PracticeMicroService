package com.practice.modulebase.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter @Getter @NoArgsConstructor
@JsonPropertyOrder(value = {
        "data", "pagination", "status", "error", "message", "isFirstTimeLogin", "systemInformation"
})
public class JsonResponse<T> {
    private ResponseStatus status;
    @JsonProperty("errorCode")
    private String errorCode;
    private String message;
    private T data;
    private Pagination pagination;
    private boolean isFirstTimeLogin;
    private String version = "1.0";
    List<FieldErrorVM> error;
    private SystemInformation systemInformation;

    Map<String, String> tokens;

    public static final class Builder<T> {
        List<FieldErrorVM> error;
        Map<String, String> tokens;
        private ResponseStatus status;
        private String errorCode;
        private String message;
        private T data;
        private Pagination pagination;
        private boolean isFirstTimeLogin;
        private String version = "1.0";
        private SystemInformation systemInformation;

        public Builder() { }

        public static Builder instance() { return new Builder(); }

        public Builder setStatus(ResponseStatus status) {
            this.status = status;
            return this;
        }

        public Builder setErrorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setData(T data) {
            this.data = data;
            return this;
        }

        public Builder setPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        public Builder setIsFirstTimeLogin(boolean isFirstTimeLogin) {
            this.isFirstTimeLogin = isFirstTimeLogin;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setError(List<FieldErrorVM> error) {
            this.error = error;
            return this;
        }

        public Builder setSystemInformation(SystemInformation systemInformation) {
            this.systemInformation = systemInformation;
            return this;
        }

        public Builder setTokens(Map<String, String> tokens) {
            this.tokens = tokens;
            return this;
        }

        public JsonResponse build() {
            JsonResponse jsonResponse = new JsonResponse();
            jsonResponse.setStatus(status);
            jsonResponse.setErrorCode(errorCode);
            jsonResponse.setMessage(message);
            jsonResponse.setData(data);
            jsonResponse.setPagination(pagination);
            jsonResponse.setError(error);
            jsonResponse.setSystemInformation(systemInformation);
            jsonResponse.setTokens(tokens);
            jsonResponse.isFirstTimeLogin = this.isFirstTimeLogin;
            jsonResponse.version = this.version;
            return jsonResponse;
        }
    }

}
