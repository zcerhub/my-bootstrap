package com.dap.sequence.client.controller;

import com.dap.sequence.client.service.SeqCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/v1/seq/client")
public class SeqClientController implements Controller {
    @Autowired
    private SeqCacheService seqCacheService;

    @PostMapping ("/getRule")
    public void getMap(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        String  mapToCacheAndDb = seqCacheService.mapToCacheAndDb(map);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }
}
