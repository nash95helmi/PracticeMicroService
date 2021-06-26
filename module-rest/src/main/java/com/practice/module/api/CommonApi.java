package com.practice.module.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface CommonApi {

    @GetMapping(value = "/isAppRunning") //Equivalent to  @RequestMapping(value = "/isAppRunning", RequestMethod = GET)
    String getIsAppRunning() throws Exception;

}
