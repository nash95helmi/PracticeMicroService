package com.practice.moduleusermanagement.api;

import com.practice.modulebase.constant.WebConstant;
import com.practice.modulebase.controller.JsonResponse;
import com.practice.modulebase.vm.JwtTokenVM;
import com.practice.modulebase.vm.TokenVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface UserManagementApi {
    @PostMapping(value = WebConstant.UserManagement.userLogin)
    ResponseEntity<JsonResponse<JwtTokenVM>> getTokenLogin(@RequestHeader(name = "authorization", required = false) String token,
                                                           @RequestBody TokenVM tokenVM) throws Exception;;
}
