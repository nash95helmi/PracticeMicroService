package com.practice.module.service;

import com.practice.module.dao.ManagerDao;
import com.practice.module.vo.ManagerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerService.class);

    @Autowired
    private ManagerDao managerDao;

    public List<ManagerVO> getAllManagers() {
        List<ManagerVO> managerVOS = managerDao.findAll();
        LOGGER.info("[getAllManagers] All Managers : {}", managerVOS);
        return managerDao.findAll();
    }
}
