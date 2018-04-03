package com.cx.HeadLine.service;

import org.springframework.stereotype.Service;

@Service
//一旦定义@service，每次系统加载都会创建这样一个对象
public class HeadLineService {
    public  String say(){
        return  "this is from HeadLineService";
    }
}
