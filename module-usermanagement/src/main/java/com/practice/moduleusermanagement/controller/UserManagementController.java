package com.practice.moduleusermanagement.controller;

import com.practice.modulebase.constant.WebConstant;
import com.practice.modulebase.controller.AbstractCommonController;
import com.practice.modulebase.controller.JsonResponse;
import com.practice.modulebase.security.filter.CustomAuthenticationFilter;
import com.practice.modulebase.security.filter.CustomAuthorizationFilter;
import com.practice.modulebase.vm.JwtTokenVM;
import com.practice.modulebase.vm.TokenVM;
import com.practice.moduleusermanagement.api.UserManagementApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = WebConstant.UserManagement.ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserManagementController extends AbstractCommonController implements UserManagementApi {

    @Override
    public ResponseEntity<JsonResponse<JwtTokenVM>> getTokenLogin(String token, TokenVM tokenVM) throws Exception {
        return null;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            CustomAuthenticationFilter filter = new CustomAuthenticationFilter(null);
            filter.getRefreshToken(authorizationHeader, request, response);
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
