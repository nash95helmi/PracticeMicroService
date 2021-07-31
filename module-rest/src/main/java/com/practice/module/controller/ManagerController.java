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
import com.practice.modulebase.controller.AbstractCommonController;
import com.practice.modulebase.controller.JsonResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = WebConstant.Manager.ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerController extends AbstractCommonController implements ManagerApi {

    @Autowired
    ManagerService managerService;

    @Override
    public ResponseEntity<JsonResponse<List<ManagerVO>>> getAllManagers() {
        List<ManagerVO> vos = managerService.getAllManagers();
        return successResponse(vos);
    }

    @Override
    public List<ManagerVO> getAllManagersByEm() {
        return managerService.getAllManagersByEm();
    }

    @Override
    public ResponseEntity<JsonResponse<List<ManagerVO>>> getAllManagersCustom() {
        List<ManagerVO> vos = managerService.getAllManagers();
        return successResponse(vos);
    }

    @Override
    public ManagerVO getManagerByIdReqParam(String id) {
        return managerService.getManagersById(id);
    }

    @Override
    public ManagerVO getManagerByIdPathVar(String sessionToken, String token, String managerId, String id) {
        return managerService.getManagersById(token);
    }

    @Override
    public ResponseEntity<JsonResponse<ManagerVO>> getManagerByIdPathVarCustom(String sessionToken, String token,
                                                                               String managerId, String id) {
        ManagerVO result = managerService.getManagersById(token);
        return result != null ? successResponse(result) :
                failResponse("Mo data found", "1001", token+" is invalid", null, null);
//        Optional<ManagerVO> opt = Optional.of(result);
//        return opt.map(this::successResponse)
//                .orElse(failResponse("Mo data found", "1001", token+" is invalid", null, null));
//        return successResponse(result);s
    }

    @Override
    public String updateManagerDetail(String token, String managerId, ManagerVO managerVO) throws Exception {
        return managerService.updateManagerDetail(token, managerVO);
    }

    @Override
    public ManagerVO createManagerDetail(String token, String managerId, ManagerVO managerVO) throws Exception {
        return managerService.createManagerVO(token, managerVO);
    }
}
