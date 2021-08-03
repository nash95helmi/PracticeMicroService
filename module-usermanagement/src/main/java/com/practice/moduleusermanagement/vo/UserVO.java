package com.practice.moduleusermanagement.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "T_MT_USER")
@Data
@NoArgsConstructor
public class UserVO {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "MANAGER_ID")
    private String managerId;
    @Column(name = "USER_PWD")
    private String userPassword;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_STATUS")
    private String userStatus;
    @Column(name = "DT_CREATE")
    private Date createdDate;
    @Column(name = "CREATE_BY")
    private String createdBy;
    @Column(name = "DT_UPDATE")
    private Date updateDate;
    @Column(name = "UPDATE_BY")
    private String updateBy;
}
