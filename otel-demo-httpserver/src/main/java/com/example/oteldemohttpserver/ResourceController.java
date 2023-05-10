package com.example.oteldemohttpserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ResourceController {


    @GetMapping("/resources")
    public String resources(@RequestHeader Map<String,String> headers) {
        headers.forEach((key,val)->{
            System.out.println(key+":"+val);
        });
        return "hello";
    }

}
