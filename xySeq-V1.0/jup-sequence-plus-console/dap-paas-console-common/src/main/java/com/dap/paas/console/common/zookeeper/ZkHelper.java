package com.dap.paas.console.common.zookeeper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLBackgroundPathAndBytesable;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.client.ZKClientConfig;
import org.apache.zookeeper.data.Stat;

import javax.validation.constraints.NotBlank;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: zookeeper处理类
 * 可使用 {@link } 获取实例
 * @Author: luotong
 * @Date: 2019/7/23 15:37
 **/
@Slf4j
public class ZkHelper {

    private final ZkConnectConfig conn;

    private CuratorFramework client;

    public ZkHelper(ZkConnectConfig conn) {
        boolean isConnected = false;
        this.conn = conn;
        client = newClient();
        client.start();
    }

    public CuratorFramework getClient() {
        if (client == null || client.getState() == CuratorFrameworkState.STOPPED) {
            if (log.isDebugEnabled()) {
                log.debug(conn.toString());
            }
            client = newClient();
        }
        if (client.getState() == CuratorFrameworkState.LATENT) {
            client.start();
        }
        return client;
    }

    private CuratorFramework newClient() {
        ZKClientConfig zkClientConfig = new ZKClientConfig();
        zkClientConfig.setProperty(ZKClientConfig.ZOOKEEPER_SERVER_PRINCIPAL,"zookeeper/"+conn.getConnectString());
        return CuratorFrameworkFactory.newClient(
                conn.getConnectString(),
                conn.getSessionTimeoutMs(),
                conn.getConnectionTimeoutMs(),
                new RetryOneTime(conn.getSleepMsBetweenRetry()));
    }

    /**
     * 获取节点数据
     *
     * @param path 节点路径
     * @return 节点数据
     */
    public String getNodeData(@NotBlank String path) throws Exception {
        if (!checkExists(path)) {
            return null;
        }
        byte[] resultByte = getClient().getData().forPath(path);
        String resultString = (resultByte == null) ? null : (new String(resultByte, Charset.defaultCharset()));
        if (log.isDebugEnabled()) {
            log.debug("query node: {} , result string: {}", path, resultString);
        }
        return resultString;
    }

    /**
     * 获取节点数据
     * json格式
     *
     * @param path 节点路径
     * @return 节点数据
     */
    public JSONObject getJsonNodeData(@NotBlank String path) throws Exception {
        return JSON.parseObject(getNodeData(path));
    }

    /**
     * 获取节点数据
     * json格式，转为目标格式
     *
     * @param path  节点路径
     * @param clazz 目标class
     * @return 节点数据
     */
    public <T> T getNodeDataByClass(@NotBlank String path, Class<T> clazz) throws Exception {
        return JSON.parseObject(getNodeData(path), clazz);
    }

    /**
     * 创建持久化节点
     *
     * @param path 节点路径
     * @return true: 成功, false: 失败
     */
    public boolean createPersistentNode(@NotBlank String path) throws Exception {
        return createNode(path, null, null);
    }

    /**
     * 创建持久化节点并初始化数据
     *
     * @param path 节点路径
     * @param data 初始化数据
     * @return true: 成功, false: 失败
     */
    public boolean createPersistentNodeWithData(@NotBlank String path, @NotBlank String data) throws Exception {
        return createNode(path, null, data);
    }

    /**
     * 创建临时节点
     *
     * @param path 节点路径
     * @return true: 成功, false: 失败
     */
    public boolean createEphemeralNode(@NotBlank String path) throws Exception {
        return createNode(path, CreateMode.EPHEMERAL, null);
    }

    /**
     * 创建临时节点并初始化数据
     *
     * @param path 节点路径
     * @param data 初始化数据
     * @return true: 成功, false: 失败
     */
    public boolean createEphemeralNodeWithData(@NotBlank String path, @NotBlank String data) throws Exception {
        return createNode(path, CreateMode.EPHEMERAL, data);
    }

    /**
     * 创建节点
     *
     * @param path 节点路径
     * @param mode 创建类型 {@link CreateMode}，默认为PERSISTENT
     * @param data 初始化数据
     * @return true: 成功, false: 失败
     */
    public boolean createNode(@NotBlank String path, CreateMode mode, String data) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("create node path: {}", path);
            log.debug("create node mode: {}", mode);
            log.debug("create node data: {}", data);
        }
        if (checkExists(path)) {
            return false;
        }
        if (mode == null) {
            mode = CreateMode.PERSISTENT;
        }
        ACLBackgroundPathAndBytesable<String> builder = getClient().create().creatingParentsIfNeeded().withMode(mode);
        if (data == null) {
            builder.forPath(path);
        } else {
            builder.forPath(path, data.getBytes());
        }
        return true;
    }

    /**
     * 更新节点数据
     *
     * @param path 节点路径
     * @param data 更新数据
     * @return true: 成功, false: 失败
     */
    public boolean updateNode(@NotBlank String path, @NotBlank String data) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("update node path: {}", path);
            log.debug("update node data: {}", data);
        }
        getClient().setData().forPath(path, data.getBytes());
        return true;
    }

    /**
     * 删除节点数据
     *
     * @param path 节点路径
     * @return true: 成功, false: 失败
     */
    public boolean deleteNode(@NotBlank String path) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Delete node with children: {}", path);
        }
        if (!checkExists(path)) {
            return true;
        }
        getClient().delete().forPath(path);
        return true;
    }

    /**
     * 删除节点数据，级联删除子节点
     *
     * @param path 节点路径
     */
    public boolean deleteNodeWithChildren(@NotBlank String path) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Delete node path: {}", path);
        }
        if (!checkExists(path)) {
            return true;
        }
        getClient().delete().deletingChildrenIfNeeded().forPath(path);
        return true;
    }

    /**
     * 检查node是否存在
     *
     * @param path node路径
     * @return true: 存在, false: 不存在
     */
    public boolean checkExists(@NotBlank String path) throws Exception {
        boolean checkResult = getClient().checkExists().forPath(path) != null;
        if (log.isDebugEnabled()) {
            log.debug("Node path {} is {}.", path, checkResult ? "exist" : "not exist");
        }
        return checkResult;
    }

    public boolean isConnected() {
        ZooKeeper zooKeeper;
        try {
            zooKeeper = getClient().getZookeeperClient().getZooKeeper();
        } catch (Exception e) {
            log.error("Get zookeeper state error.", e);
            return false;
        }
        ZooKeeper.States states = zooKeeper.getState();
        return (states != null && states.isConnected());
    }

    /**
     * 获取子节点列表
     *
     * @param path 节点路径
     * @return children
     */
    public List<String> getChildren(String path)throws Exception{
        if (log.isDebugEnabled()) {
            log.debug("update node path: {}", path);
        }
        List<String> children = getClient().getChildren().forPath(path);
        return children;
    }

    public Map<String, Object> getDataStat(String path) {
    	Map<String, Object> map = new HashMap<String, Object>();
        try {
            Stat stat = new Stat();
            byte[] bytes = getClient().getData().storingStatIn(stat).forPath(path);

            map.put("data", bytes == null ? "" : new String(bytes, Charset.defaultCharset()));
            map.put("stat", stat);
            return map;
        } catch (Exception e) {
            log.error("获取节点数据异常, {}", e.getMessage());
        }
        return map;
    }


}
