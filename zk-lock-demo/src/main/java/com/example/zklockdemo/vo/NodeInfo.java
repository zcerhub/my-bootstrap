package com.example.zklockdemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
public class NodeInfo  implements Serializable {

    private String nodeName;
    private List<NetworkInterfaceInfo> networkInterfaceInfos;


}
