package ctbiyi.com.service.impl;

import ctbiyi.com.service.NodeInfoService;
import ctbiyi.com.vo.NetworkInterfaceInfo;
import ctbiyi.com.vo.NodeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@Service
@Slf4j
public class NodeInfoServiceImpl implements NodeInfoService {

    @Value("${yy.node.name}")
    private String nodeName;

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
                networkInterfaceInfos.add(new NetworkInterfaceInfo(ni.getDisplayName(), mac));
            }
        }

        return new NodeInfo(nodeName,networkInterfaceInfos);
    }


}
