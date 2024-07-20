package com.dap.paas.console.seq.check;

import javax.validation.groups.Default;

/**
 * @className ValidGroup
 * @description 注解分组校验
 * @author renle
 * @date 2023/12/06 10:14:29 
 * @version: V23.06
 */
public interface ValidGroup extends Default {

    /**
     * @className Valid
     * @description 分组类
     * @author renle
     * @date 2023/12/06 10:14:29
     * @version: V23.06
     */
    interface Valid extends ValidGroup {

        /**
         * @className Create
         * @description 创建分组
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface Create extends Valid {
        }

        /**
         * @className Update
         * @description 更新分组
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface Update extends Valid {
        }

        /**
         * @className Query
         * @description 查询分组
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface Query extends Valid {
        }

        /**
         * @className Delete
         * @description 删除分组
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface Delete extends Valid {
        }

        /**
         * @className SeqDesignIssue
         * @description 序列发布
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface SeqDesignIssue extends Valid {
        }

        /**
         * @className ClusterEnvCheck
         * @description 集群环境检查
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface ClusterEnvCheck extends Valid {
        }

        /**
         * @className NodeEnvCheck
         * @description 节点环境检查
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface NodeEnvCheck extends Valid {
        }

        /**
         * @className RuleConfig
         * @description 规则配置
         * @author renle
         * @date 2023/12/06 10:14:29
         * @version: V23.06
         */
        interface RuleConfig extends Valid {
        }
    }
}
