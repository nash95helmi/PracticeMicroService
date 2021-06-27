package com.practice.module.api;

import com.practice.module.constant.WebConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface CommonApi {

    @GetMapping(value = WebConstant.Common.isRunning) //Equivalent to  @RequestMapping(value = "/isAppRunning", RequestMethod = GET)
    String getIsAppRunning() throws Exception;

}
