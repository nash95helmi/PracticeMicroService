package com.practice.module.dao;

import com.practice.module.vo.ManagerVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerQueryCall {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<ManagerVO> getManagerListByEM() {
        String sqlString = "SELECT mngrid, mnfnam, mnlnam, mnpoid, mndept, mnasts, mnemel, mndocid " +
                "FROM mngmst";
        List<Tuple> resultList = entityManager.createNativeQuery(sqlString, Tuple.class).getResultList();
        List<ManagerVO> managerVOS = resultList.stream().map(this::mapToManagerVO).collect(Collectors.toList());
        return managerVOS;
    }

    private final ManagerVO mapToManagerVO(final Tuple tuple) {
        if (tuple == null) return null;

        ManagerVO vo = new ManagerVO();
        vo.setManagerId(tuple.get("mngrid").toString());
        vo.setManagerFirstName(tuple.get("mnfnam").toString());
        vo.setManagerLastName(tuple.get("mnlnam").toString());
        vo.setManagerPositionId(tuple.get("mnpoid").toString());
        vo.setManagerDepartment(tuple.get("mndept").toString());
        vo.setManagerStatus(tuple.get("mnasts").toString());
        vo.setManagerEmel(tuple.get("mnemel").toString());
        return vo;
    }
}
