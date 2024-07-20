package com.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class TestController {
    @RequestMapping("/hello")
    public void aa(){
    //
    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(TestController.class,args);
//    }
}
