package com.practice.modulebatch.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BATCH_HISTORY")
@Setter
@Getter
@ToString
public class BatchHistoryVO implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "CREATE_BY")
    private String createdBy;
    @Column(name = "DT_CREATE")
    private Date createdDate;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "WORKING_DIRECTORY")
    private String workingDirectory;
    @Column(name = "ERROR_CODE")
    private String exceptionCode;
    @Lob
    @Column(name = "ERROR_MESSAGE", length = 10240)
    private String exceptionMessage;
    @Lob
    @Column(name = "ERROR_DETAIL", length = 10240)
    private String exceptionDetail;
    @Column(name = "FAILED_COUNT")
    private Integer failedCount;
    @Column(name = "SUCCESS_COUNT")
    private Integer successCount;
    @Column(name = "FILENAME")
    private String fileName;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STARTER_TYPE")
    private String starterType;
    @Column(name = "IS_WORKING_DAY")
    private String isWorkingDay;
    @OneToMany(mappedBy = "history", fetch = FetchType.EAGER)
    private BatchJobVO job;
}
