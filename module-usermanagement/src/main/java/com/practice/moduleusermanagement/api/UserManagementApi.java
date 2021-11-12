package com.practice.moduleusermanagement.api;

import com.practice.modulebase.constant.WebConstant;
import com.practice.modulebase.controller.JsonResponse;
import com.practice.modulebase.vm.JwtTokenVM;
import com.practice.modulebase.vm.TokenVM;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserManagementApi {
    @PostMapping(value = WebConstant.UserManagement.userLogin)
    ResponseEntity<JsonResponse<JwtTokenVM>> getTokenLogin(@RequestHeader(name = "authorization", required = false) String token,
                                                           @RequestBody TokenVM tokenVM) throws Exception;

    @GetMapping(value = WebConstant.UserManagement.refreshToken)
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
