package com.practice.modulebase.constant;

public class WebConstant {
    public static final String appContextPath = "/app";

    public class Common {
        public static final String ROOT = "/appRest";
        public static final String isRunning = "/isRunning";
    }

    public class Manager {
        public static final String ROOT = "/manager";
        public static final String findAll = "/findAll";
        public static final String findAllByEm = "/findAllByEm";
        public static final String findAllCustom = "/findAllCustom";
        public static final String findByIdReqParam = "/findById";
        public static final String findByIdPathVar = "/findById/{id}";
        public static final String findByIdPathVarCustom = "/findByIdCustom/{id}";
        public static final String updateManagerDetail = "/update";
        public static final String createManagerDetail = "/create";
    }

    public class UserManagement {
        public static final String ROOT = "/user";
        public static final String userLogin = "/login";
        public static final String refreshToken = "/refreshToken";
        public static final String getToken = "/getToken";
    }
}
