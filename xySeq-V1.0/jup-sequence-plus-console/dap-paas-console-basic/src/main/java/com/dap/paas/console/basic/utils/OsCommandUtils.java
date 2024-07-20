package com.dap.paas.console.basic.utils;

import com.base.api.exception.ServiceException;
import com.dap.paas.console.basic.entity.Machine;
import org.apache.commons.lang3.StringUtils;

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
 * <td>2023/2/6 16:48</td>
 * <td>captjack</td>
 * <td>create</td>
 * </tr>
 * </table>
 * <br>
 * <font color="red">注意: 本内容仅限于[中电金信公司]内部使用，禁止转发</font> <br>
 *
 * @author captjack
 * @version 1.0.0
 * 2023/2/6 16:48
 * package com.dap.paas.console.common.util
 */
public final class OsCommandUtils {

    /**
     * 判断命令是否存在，不存在会抛出异常
     *
     * @param machine  机器信息
     * @param commands 多个命令
     * @throws Exception 异常
     */
    public static void commandCheckWithThrows(Machine machine, String... commands) throws Exception {
        // 执行检测
        boolean[] res = commandCheck(machine, commands);
        StringBuilder notFoundCommand = new StringBuilder();
        // 拼接提示错误信息
        for (int i = 0; i < res.length; i++) {
            if (!res[i]) {
                notFoundCommand.append(commands[i]).append(",");
            }
        }
        // 必备命令不存在
        if (notFoundCommand.length() > 0) {
            throw new ServiceException(String.format("machine %s miss required command %s ", machine.getHostIp(), notFoundCommand));
        }
    }

    /**
     * 判断命令是否存在
     *
     * @param machine  机器信息
     * @param commands 多个命令
     * @throws Exception 异常
     */
    public static boolean[] commandCheck(Machine machine, String... commands) throws Exception {
        if (commands.length == 0) {
            return new boolean[]{};
        }
        boolean[] res = new boolean[commands.length];
        // 检测
        for (int i = 0; i < commands.length; i++) {
            if (commandCheck(machine, commands[i])) {
                res[i] = true;
            }
        }
        // 检测结果
        return res;
    }

    /**
     * 判断命令是否存在
     *
     * @param command 命令
     * @return 是否存在
     * @throws Exception 异常
     */
    public static boolean commandCheck(Machine machine, String command) throws Exception {
        // !!! 部分命令需要先执行环境遍历配置
        // 搜索命令
        String line = SshDeployServerUtil.execCommand(machine, "source /etc/profile;source ~/.bash_profile;whereis " + command);
        // 读取处理结果
        String[] res = line.split(":");
        // 命令不存在
        if (res.length == 1) {
            return false;
        }
        // 结果不为空说明命令是存在的
        return StringUtils.isNotBlank(res[1]);
    }

    private OsCommandUtils() {

    }

}
