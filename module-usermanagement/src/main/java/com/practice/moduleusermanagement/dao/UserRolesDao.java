package com.practice.moduleusermanagement.dao;

import com.practice.moduleusermanagement.vo.UserRolesVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesDao extends JpaRepository<UserRolesVO, String> {
}
