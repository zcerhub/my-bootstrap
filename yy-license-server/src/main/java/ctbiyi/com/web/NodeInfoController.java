package ctbiyi.com.web;

import ctbiyi.com.service.NodeInfoService;
import ctbiyi.com.vo.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;

@RestController
@RequestMapping("api")
public class NodeInfoController {

    @Autowired
    private NodeInfoService nodeInfoService;

    @GetMapping( value="nodeInfo")
    public NodeInfo nodeInfo() throws SocketException {
        return nodeInfoService.getNodeInfo();
    }

}
