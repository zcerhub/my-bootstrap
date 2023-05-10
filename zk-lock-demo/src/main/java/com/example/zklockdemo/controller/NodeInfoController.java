package com.example.zklockdemo.controller;

import com.example.zklockdemo.service.NodeInfoService;
import com.example.zklockdemo.vo.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.SocketException;
import java.net.UnknownHostException;

@RestController
public class NodeInfoController {

    @Autowired
    private NodeInfoService nodeInfoService;

    @GetMapping( value="nodeInfo")
    public NodeInfo nodeInfo() throws UnknownHostException, SocketException {

        return nodeInfoService.getNodeInfo();
    }

}
