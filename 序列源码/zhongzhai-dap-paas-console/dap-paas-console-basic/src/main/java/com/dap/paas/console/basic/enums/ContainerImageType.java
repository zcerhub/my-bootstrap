package com.dap.paas.console.basic.enums;

/**
 * <p>
 * ----------------------------------------------------------------------------- <br>
 * project name ：dap-paas-console <br>
 * description：description。。。。。 <br>
 * copyright : © 2019-2023 <br>
 * corporation : 中电金信公司 <br>
 * ----------------------------------------------------------------------------- <br>
 * change history <br>
 * <table width="432" border="1">
 * <tr>
 * <td>version</td>
 * <td>time</td>
 * <td>author</td>
 * <td>change</td>
 * </tr>
 * <tr>
 * <td>1.0.0</td>
 * <td>2023/7/18 21:04</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/7/18 21:04
 * package com.dap.paas.console.cache
 */
public enum ContainerImageType{

    /**
     * kafka相关
     */
    ZOOKEEPER(1,"ZOOKEEPER"),

    KAFKA_BROKER(2,"KAFKA_BROKER"),

    KAFKA_MIRROR_MAKER(3,"KAFKA_MIRROR_MAKER"),

    /**
     * redis
     */
    REDIS(4,"REDIS"),

    /**
     * 序列
     */
    SEQ_SERVER(5,"SEQ_SERVER"),

    /**
     * rocket_mq
     */
    ROCKETMQ_NAMESERVER(6,"ROCKETMQ_NAMESERVER"),

    ROCKETMQ_BROKER(7,"ROCKETMQ_BROKER"),
    /**
     * sshd 为secure shell的简称；可以通过网络在主机中开机shell的服务
     */
    SSHD(8,"SSHD"),
    DEFAULT(-1,""),
    ;
    private  int value;
    private  String name;

    ContainerImageType(int value,String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static ContainerImageType getByValue(int value){
        for (ContainerImageType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

    public static ContainerImageType getDefault() {
        return DEFAULT; // 返回默认值
    }

}
