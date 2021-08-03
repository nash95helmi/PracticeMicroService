package com.practice.moduleusermanagement.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "T_MT_USER_ROLES")
@Data
@NoArgsConstructor
public class UserRolesVO {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "MANAGER_ID")
    private String managerId;
    @Column(name = "ROLES_ID")
    private String rolesId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DT_CREATE")
    private Date createdDate;
    @Column(name = "CREATE_BY")
    private String createBy;
    @Column(name = "DT_UPDATE")
    private Date updatedDate;
    @Column(name = "UPDATE_BY")
    private String updateBy;
}
