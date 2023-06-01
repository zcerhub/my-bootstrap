package com.example.yylicenseweb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateController {


    @Autowired
    private TokenUtil tokenUtil;


    @GetMapping("/licenseInfo")
    public String licenseInfo(String userId,String userRole) {
        return tokenUtil.getToken(userId, userRole);
    }


}
