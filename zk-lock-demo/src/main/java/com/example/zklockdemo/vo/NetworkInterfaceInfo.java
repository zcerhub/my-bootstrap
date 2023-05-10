package com.example.zklockdemo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@ToString
@Data
@NoArgsConstructor
public class NetworkInterfaceInfo implements Serializable {

    private String displayName;
    private String hardwareAddress;

}
