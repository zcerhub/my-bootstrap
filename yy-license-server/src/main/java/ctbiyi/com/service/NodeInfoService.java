package ctbiyi.com.service;


import ctbiyi.com.vo.NodeInfo;

import java.net.SocketException;

public interface NodeInfoService {

    NodeInfo getNodeInfo() throws SocketException;

}
