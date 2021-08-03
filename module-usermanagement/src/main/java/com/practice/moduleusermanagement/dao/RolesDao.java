package com.practice.moduleusermanagement.dao;

import com.practice.moduleusermanagement.vo.RolesVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesDao extends JpaRepository<RolesVO, String> {
}
