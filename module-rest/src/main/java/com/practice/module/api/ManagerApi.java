package com.practice.module.api;

import com.practice.module.constant.WebConstant;
import com.practice.module.vo.ManagerVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.practice.modulebase.controller.JsonResponse;

import java.util.List;

public interface ManagerApi {

    @GetMapping(value = WebConstant.Manager.findAll)
    ResponseEntity<JsonResponse<List<ManagerVO>>> getAllManagers();

    @GetMapping(value = WebConstant.Manager.findAllByEm)
    List<ManagerVO> getAllManagersByEm();

    @GetMapping(value = WebConstant.Manager.findAllCustom)
    ResponseEntity<JsonResponse<List<ManagerVO>>> getAllManagersCustom();

    ///findById?id=M00005
    @GetMapping(value = WebConstant.Manager.findByIdReqParam)
    ManagerVO getManagerByIdReqParam(@RequestParam String id);

    //findById/M00005
    @GetMapping(value = WebConstant.Manager.findByIdPathVar)
    ManagerVO getManagerByIdPathVar(@RequestHeader(name = "sessionToken", required = false) String sessionToken,
                                    @RequestHeader(name = "authorization", required = false) String token,
                                    @RequestAttribute(name = "managerId", required = false) String managerId,
                                    @PathVariable(value = "id", required = true) String id);

    //findById/M00005
    @GetMapping(value = WebConstant.Manager.findByIdPathVarCustom)
    ResponseEntity<JsonResponse<ManagerVO>> getManagerByIdPathVarCustom(@RequestHeader(name = "sessionToken", required = false) String sessionToken,
                                    @RequestHeader(name = "authorization", required = false) String token,
                                    @RequestAttribute(name = "managerId", required = false) String managerId,
                                    @PathVariable(value = "id", required = true) String id);

    @PutMapping(value = WebConstant.Manager.updateManagerDetail)
    String updateManagerDetail(@RequestHeader(name = "authorization", required = false) String token,
                               @RequestAttribute(name = "ManagerId", required = false) String managerId,
                               @RequestBody ManagerVO managerVO) throws Exception;

    @PutMapping(value = WebConstant.Manager.createManagerDetail)
    ManagerVO createManagerDetail(@RequestHeader(name = "authorization", required = false) String token,
                                  @RequestAttribute(name = "ManagerId", required = false) String managerId,
                                  @RequestBody ManagerVO managerVO) throws Exception;
}
