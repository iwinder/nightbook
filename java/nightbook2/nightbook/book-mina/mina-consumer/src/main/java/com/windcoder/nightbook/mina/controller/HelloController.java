package com.windcoder.nightbook.mina.controller;


import com.windcoder.nightbook.mina.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HelloController {

    @Autowired
    private TestService testService;

    @RequestMapping("hello")
    public String  hello(String name) {
        return "Hello "+name;
    }

    @RequestMapping("add/1")
    public void add1(){
        testService.add();
    }
    @RequestMapping("add/2")
    public void add2(){
        testService.add2();
    }
    @RequestMapping("add/3")
    public void add3(){
        testService.add3();
    }
    @RequestMapping("add/4")
    public void add4(){
        testService.add4();
    }
}
