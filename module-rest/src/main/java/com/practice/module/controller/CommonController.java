package com.practice.module.controller;

import com.practice.module.api.CommonApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/appRest")
public class CommonController implements CommonApi {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Override
    public String getIsAppRunning() throws Exception {
        logger.info("[getIsAppRunning] isRunning");
        return "App is running";
    }

}
