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

    public ManagerVO getManagersById(String id) {
        ManagerVO managerVO = managerDao.findById(id).orElse(null);
        LOGGER.info("[getManagersById] Id : {}, Manager : {}", id, managerVO);
        return managerVO;
    }

    public String updateManagerDetail(String managerId, ManagerVO managerVO) {
        ManagerVO manager = getManagersById(managerId);

        if (manager == null)
            return "Update Failed, managerId not authorized";

        managerDao.saveAndFlush(managerVO);
        LOGGER.info("[updateManagerDetail] managerId : {}, updateDate : {}", manager, managerVO);
        return "Update successful";
    }
}
