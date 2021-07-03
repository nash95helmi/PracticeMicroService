package com.practice.modulebatch.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "BATCH_JOB")
@Setter
@Getter
@ToString
public class BatchJobVO implements Serializable {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "LAST_TRIGGER_DATE")
    private Timestamp lastTriggerDate;
    @Column(name = "LAST_COMPLETE_DATE")
    private Timestamp lastCompleteDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "IS_ACTIVE")
    private Boolean active;
    @Column(name = "IS_SYSTEM")
    private Boolean visible;
    @Column(name = "INTERVAL")
    private Integer interval;
    @Column(name = "INTERVAL_TERM")
    private String intervalTerm;
}
