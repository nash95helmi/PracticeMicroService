package com.practice.moduleusermanagement.controller;

import com.practice.modulebase.constant.WebConstant;
import com.practice.modulebase.controller.AbstractCommonController;
import com.practice.modulebase.controller.JsonResponse;
import com.practice.modulebase.vm.JwtTokenVM;
import com.practice.modulebase.vm.TokenVM;
import com.practice.moduleusermanagement.api.UserManagementApi;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = WebConstant.UserManagement.ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserManagementController extends AbstractCommonController implements UserManagementApi {

    @Override
    public ResponseEntity<JsonResponse<JwtTokenVM>> getTokenLogin(String token, TokenVM tokenVM) throws Exception {
        return null;
    }
}
