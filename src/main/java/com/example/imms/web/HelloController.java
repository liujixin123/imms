package com.example.imms.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(String name){
        String msg = "hello word ! "+name;
        System.out.println("--->>>"+msg);
        return msg;
    }

    @RequestMapping("/hello2")
    public String hello2(String name){
        String msg = "hello word 2 ! "+name;
        System.out.println("--->>>"+msg);
        return msg;
    }
}
