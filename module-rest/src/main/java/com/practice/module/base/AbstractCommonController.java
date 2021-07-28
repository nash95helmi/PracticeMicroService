package com.practice.module.base;

import org.springframework.http.ResponseEntity;

import java.util.Calendar;

public abstract class AbstractCommonController {

    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity<JsonResponse<T>> successResponse(T data) {
        JsonResponse<T> resp = JsonResponse.Builder.instance().setStatus(ResponseStatus.SUCCESS).setMessage("success")
                .setSystemInformation(createSystemInformation(null, null, null)).setData(data).build();
        return ResponseEntity.ok().body(resp);
    }

    @SuppressWarnings("unchecked")
    protected <T> ResponseEntity<JsonResponse<T>> failResponse(String message, String errorCode, String errorMessage,
                                                               String detail, T data) {
        JsonResponse<T> resp = JsonResponse.Builder.instance().setStatus(ResponseStatus.FAILED).setMessage(message)
                .setSystemInformation(createSystemInformation(errorCode, errorMessage, detail)).setData(data).build();
        return ResponseEntity.ok().body(resp);
    }

    protected SystemInformation createSystemInformation(String errorCode, String errorMessage, String detail) {
        SystemInformation systemInformation = new SystemInformation();
        systemInformation.setErrorCode(errorCode);
        systemInformation.setErrorMessage(errorMessage);
        systemInformation.setDetails(detail);
        systemInformation.setTimeStamp(Calendar.getInstance().getTime());
        return  systemInformation;
    }

}
