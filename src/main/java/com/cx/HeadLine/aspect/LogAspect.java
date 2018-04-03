package com.cx.HeadLine.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private static  final Logger logger=LoggerFactory.getLogger(LogAspect.class);
    //*返回值  *所有方法 ，*表示通配符
    //JoinPoint可以记录下所有参数的数值
    @Before("execution(* com.cx.HeadLine.controller.*Controller.*(..))")
    public  void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb=new StringBuilder();
        for(Object arg:joinPoint.getArgs())
            sb.append("arg:"+arg.toString()+" |");
        logger.info("before method:"+sb.toString());
    }
    @After("execution(* com.cx.HeadLine.controller.IndexController.*(..))")
    public void afterMethod(JoinPoint joinPoint){

        logger.info("after method:");
    }

}
