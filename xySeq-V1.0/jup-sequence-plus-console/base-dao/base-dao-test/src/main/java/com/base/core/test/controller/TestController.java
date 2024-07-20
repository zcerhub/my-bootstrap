package com.base.core.test.controller;

import com.base.core.test.entity.Test;
import com.base.core.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/queryPage")
    public void test(){
       //
    }
    @GetMapping("/save")
    public void save(){
        Test test= new Test();
        test.setName("123456");
         testService.saveObject(test);
    }
    @GetMapping("/AA")
    public void aa(){
       //
    }


}
