package com.example.yylicenseweb;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableScheduling
public class LicenseValidTimer {

    @Autowired
    private LicenseValidInterceptor licenseValidInterceptor;

    @Scheduled(cron ="*/6 * * * * ?")
    public void sayHello() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        String licenseResultStr="http://localhost:8081/licenseResult";
        System.out.println(licenseResultStr);

        String licenseResultJwt = JwtTokenUtil.parseToken(licenseResultStr);

        LicenseResult licenseResult = JSON.parseObject(licenseResultJwt, LicenseResult.class);
        System.out.println(licenseResult);

        licenseValidInterceptor.setLisenceValid(licenseResult.isLicenseValid());
    }

}
