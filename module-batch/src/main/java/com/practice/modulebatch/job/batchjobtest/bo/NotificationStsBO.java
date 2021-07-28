package com.practice.modulebatch.job.batchjobtest.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class NotificationStsBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String managerId;
    private String docId;
    private String createdBy;
    private Date dtCreate;
    private String status;
}
