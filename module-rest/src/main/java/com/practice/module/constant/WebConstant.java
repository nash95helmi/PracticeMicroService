package com.practice.module.constant;

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
        public static final String findByIdReqParam = "/findById";
        public static final String findByIdPathVar = "/findById/{id}";
        public static final String updateManagerDetail = "/update";
        public static final String createManagerDetail = "/create";
    }
}
