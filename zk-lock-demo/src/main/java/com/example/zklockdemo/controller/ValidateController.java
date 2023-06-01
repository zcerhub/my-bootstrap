package com.example.zklockdemo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateController {

    @Value("#{jwt.validate}")
    private String validateJwt;

    @Value("#{jwt.invalidate}")
    private String invalidateJwt;

    @GetMapping("/licenseResult")
    public String licenseInfo() {

        boolean licenseValidate=true;

        if (licenseValidate) {
            return validateJwt;
        }else{
            return invalidateJwt;
        }

    }


}
