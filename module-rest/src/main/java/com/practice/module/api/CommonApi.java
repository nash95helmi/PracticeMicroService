package com.practice.module.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface CommonApi {

//    @GetMapping(value = "/isAppRunning")
//    boolean getIsAppRunning() throws Exception;

    @RequestMapping(value = "/isAppRunning")
    String getIsAppRunning() throws Exception;
}
