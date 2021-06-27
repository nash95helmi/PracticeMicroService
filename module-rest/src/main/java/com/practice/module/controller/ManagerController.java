package com.practice.module.controller;

import com.practice.module.api.ManagerApi;
import com.practice.module.constant.WebConstant;
import com.practice.module.service.ManagerService;
import com.practice.module.vo.ManagerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = WebConstant.Manager.ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerController implements ManagerApi {

    @Autowired
    ManagerService managerService;

//    Need to configure ResponseEntity 1st
//    @Override
//    public ResponseEntity<List<ManagerVO>> getAllManagers() {
//        List<ManagerVO> vos = new ArrayList<>();
//        vos = managerService.getAllManagers();
//        return (ResponseEntity<List<ManagerVO>>) vos;
//    }

    @Override
    public List<ManagerVO> getAllManagers() {
        List<ManagerVO> vos = new ArrayList<>();
        vos = managerService.getAllManagers();
        return vos;
    }
}