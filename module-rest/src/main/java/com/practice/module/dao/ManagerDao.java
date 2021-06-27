package com.practice.module.dao;

import com.practice.module.vo.ManagerVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerDao extends JpaRepository<ManagerVO, String> {

}
