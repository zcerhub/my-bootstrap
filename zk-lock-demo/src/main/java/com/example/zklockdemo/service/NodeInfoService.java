package com.example.zklockdemo.service;

import com.example.zklockdemo.vo.NodeInfo;

import java.net.SocketException;
import java.util.List;

public interface NodeInfoService {

    NodeInfo getNodeInfo() throws SocketException;

}
