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
@Table(name = "T_MT_ROLES")
@Data
@NoArgsConstructor
public class RolesVO {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "ROLE_ID")
    private String roleId;
    @Column(name = "ROLE_NAME")
    private String roleName;
    @Column(name = "ROLE_DESC")
    private String roleDesc;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "DT_CREATE")
    private Date createdDate;
    @Column(name = "CREATE_BY")
    private String createdBy;
    @Column(name = "DT_UPDATE")
    private Date updateDate;
    @Column(name = "UPDATE_BY")
    private String updateBy;
}
