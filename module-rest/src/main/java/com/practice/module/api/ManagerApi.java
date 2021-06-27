package com.practice.module.api;

import com.practice.module.constant.WebConstant;
import com.practice.module.vo.ManagerVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ManagerApi {

    @GetMapping(value = WebConstant.Manager.findAll)
//    ResponseEntity<List<ManagerVO>> getAllManagers();
    List<ManagerVO> getAllManagers();
}
