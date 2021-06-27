package com.practice.module.aspect;

import com.practice.module.vo.ManagerVO;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Aspect
@Component
//@EnableAspectJAutoProxy
public class ManagerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerAspect.class);

    /**
     * Intercept method with specific annotation
     */
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) ")
    public void restControllers() {
        //Return all controller with RequestMapping/PostMapping
    }

    @Around(value = "execution(* com.practice.module.controller..*(..))) && restControllers()")
    public Object logController(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("[ManagerAspect] coming into ManagerAspect");

        StopWatch watch = new StopWatch();
        watch.start();

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();

        String className = pjp.getSignature().getDeclaringType().getSimpleName();
        String methodName = pjp.getSignature().getName();
        String httpMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        String argsStr = StringUtils.arrayToCommaDelimitedString(pjp.getArgs());

        LOGGER.info("[ManagerAspect] className : {}, methodName : {}, httpMethod : {}, requestUri : {}, argStr : {}",
                className, methodName, httpMethod, requestUri, argsStr);

        Object output = pjp.proceed();

        watch.stop();
        LOGGER.info("[ManagerAspect] Execution Time : {}", watch.getTime());

        return output;
    }
}
