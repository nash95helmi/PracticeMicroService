package com.practice.module.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mngmst")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ManagerVO implements Persistable<String> {
    @Id
    @Column(name = "mngrid")
    private String managerId;

    @Column(name = "mnfnam")
    private String managerFirstName;

    @Column(name = "mnlnam")
    private String managerLastName;

    @Column(name = "mnpoid")
    private String managerPositionId;

    @Column(name = "mndept")
    private String managerDepartment;

    @Column(name ="mnasts")
    private String managerStatus;

    @Column(name = "mnemel")
    private String managerEmel;

    @Column(name = "mndocid")
    private String managerDocId;

    @Override
    public String getId() {
        return managerId;
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
