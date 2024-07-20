/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dap.sequence.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * @className NetUtils
 * @description 网卡工具
 * @date 2023/11/23 09:58:44
 * @version: V23.06
 */
public class NetUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NetUtils.class);

    private static final int TIME_OUT = 3000;



    /** Ping主机 */
    public static boolean ping(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(TIME_OUT);
        } catch (Exception e) {
            LOG.error("Ping [" + host + "] server has crash or not exist.", e);
            return false;
        }
    }

    /** 获取本地ip */
    public static String ip() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            LOG.error("Get local server ip has error.msg = {}", e.getMessage(), e);
        }
        return ip;
    }

}
