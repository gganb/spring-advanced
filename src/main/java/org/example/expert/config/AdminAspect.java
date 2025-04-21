package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AdminAspect {
    /**
     * - 로깅 내용에는 다음이 포함되어야 합니다:
     *     - 요청한 사용자의 ID
     *     - API 요청 시각
     *     - API 요청 URL
     *     - 요청 본문(`RequestBody`)
     *     - 응답 본문(`ResponseBody`)
     */
    @Pointcut("execution(* *..controller.CommentAdminController.*(..))")
    public void commentAdminControllerMethods() {}

    @Pointcut("execution(* *..controller.UserAdminController.*(..))")
    public void userAdminControllerMethods() {}

    @Pointcut("commentAdminControllerMethods() || userAdminControllerMethods()")
    public void adminControllersMethods() {}


    @Around("adminControllersMethods()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable{

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String,Object> params = new HashMap<>();
        try {
            String decodedURI = URLDecoder.decode(request.getRequestURI(),"UTF-8");

            params.put("log_time",System.currentTimeMillis());
            params.put("URI",decodedURI);
            params.put("RequestBody",)
        }
        }
       // long start = System.currentTimeMillis();    // 시작 시간

}
