package com.practice.modulebatch.job.maturityrollover.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MaturityRolloverBO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String policyNumber;
    private String transactionType;
    private String transactionRefNo;
    private String completionDate;
    private String status;
    private Date dateCreated;
    private String createdBy;
    private Date dateUpdated;
    private String updateBy;
}
