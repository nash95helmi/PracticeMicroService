package com.practice.moduleusermanagement.service;

import com.practice.moduleusermanagement.dao.UserDao;
import com.practice.moduleusermanagement.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDao userDao;

    public UserVO findUserByManagerId(String managerId) {
        return userDao.findBymanagerId(managerId).orElse(null);
    }

    public UserVO findUserByUserId(String userId) {
        return userDao.findByuserId(userId).orElse(null);
    }


}
