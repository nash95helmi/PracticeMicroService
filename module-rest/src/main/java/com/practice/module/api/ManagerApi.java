package com.practice.module.api;

import com.practice.module.constant.WebConstant;
import com.practice.module.vo.ManagerVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ManagerApi {

    @GetMapping(value = WebConstant.Manager.findAll)
//    ResponseEntity<List<ManagerVO>> getAllManagers();
    List<ManagerVO> getAllManagers();

    ///findById?id=M00005
    @GetMapping(value = WebConstant.Manager.findByIdReqParam)
    ManagerVO getManagerByIdReqParam(@RequestParam String id);

    //findById/M00005
    @GetMapping(value = WebConstant.Manager.findByIdPathVar)
    ManagerVO getManagerByIdPathVar(@PathVariable("id") String id);
}
