package com.practice.modulebase.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter @Getter @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemInformation {

    private String errorCode;
    private String errorMessage;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date timeStamp;
    private String details;
    private String isSystemUnderMaintainance = "N";
}
