package com.practice.moduleusermanagement.dao;

import com.practice.moduleusermanagement.vo.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<UserVO, String> {

    Optional<UserVO> findBymanagerId(String managerId);
    Optional<UserVO> findByuserId(String userId);
}
