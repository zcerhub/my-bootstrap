package com.example.zklockdemo.service;

import com.example.zklockdemo.vo.NetworkInterfaceInfo;
import com.example.zklockdemo.vo.NodeInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@Service
public class NodeInfoServiceImpl  implements NodeInfoService{

    @Value("${node.name}")
    private String nodeName;

    @PostConstruct
    public void test() {
        System.out.println(nodeName);
    }

    @Override
    public NodeInfo getNodeInfo() throws SocketException {

        List<NetworkInterfaceInfo> networkInterfaceInfos = new ArrayList<>();

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            byte[] hardwareAddress = ni.getHardwareAddress();
            if (hardwareAddress != null) {
                String[] hexadecimalFormat = new String[hardwareAddress.length];
                for (int i = 0; i < hardwareAddress.length; i++) {
                    hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                }
                String mac=String.join("-", hexadecimalFormat);
                System.out.println(ni.getDisplayName() + " " + mac);
                networkInterfaceInfos.add(new NetworkInterfaceInfo(ni.getDisplayName(), mac));
            }

        }

        return new NodeInfo(nodeName,networkInterfaceInfos);
    }


}
