package com.base.sys.auth.dataAuth;

import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.auth.PreUsernamePasswordAuthenticationFilter;
import com.base.sys.config.SysSecurityConfig;
import com.base.sys.utils.UserUtil;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class DataAuthInterceptor implements Interceptor {
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();
    private Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(DataAuthInterceptor.class);

    /**
     * 租户表的sql操作不做拦截直接执行
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        List<String> sqlFilter = SqlFilterEnum.toList();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String sqlId = ms.getId();

        if (sqlFilter.contains(sqlId)) {
            return invocation.proceed();
        }
        String url = PreUsernamePasswordAuthenticationFilter.urlThreadLocal.get();
        if (SysSecurityConfig.loginPage.equalsIgnoreCase(url)) {
            return invocation.proceed();
        }

        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        //原始sql
        String origSql = boundSql.getSql();
        logger.trace("原始SQL: {},请求url:{}", origSql, url);

        // 获取登录人信息
        String tenantQuerySql = "";
        if (!checkTenantId(origSql)) {
            AuthenticationUserDto userDto = UserUtil.getUser();
            if (userDto == null || userDto.getTenantId() == null) {
                throw new NullPointerException("拦截租户查询时,用户信息不存在");
            }
            String joinTenantSql = "tenant_id = " + "'" + userDto.getTenantId() + "'";
            tenantQuerySql = getTenantQuerySql(joinTenantSql, origSql);

        } else {
            tenantQuerySql = origSql;
        }
        AuthenticationUserDto userDto = UserUtil.getUser();
        //数据权限使用的sql
        String jointSql = checkDataPermession(userDto, url);
        //没有数据权限时，直接执行拼接租户id的sql
        if (StringUtils.isEmpty(jointSql)) {
            return excuteSql(invocation, ms, boundSql, tenantQuerySql);
        }
        String userId = userDto.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return invocation.proceed();
        }
        origSql = tenantQuerySql;
        String tempSql = getTenantQuerySql(jointSql, origSql);

        Map<String, String> replaceValue = new HashMap();
        replaceValue.put("userId", userId);
        StrSubstitutor strSubstitutor = new StrSubstitutor(replaceValue);
        String newSql = strSubstitutor.replace(tempSql);
        return excuteSql(invocation, ms, boundSql, newSql);
    }

    //获取当前请求是否存在与数据权限
    private String checkDataPermession(AuthenticationUserDto authenticationUserDTo, String url) {
        /**
         * 数据权限装载
         */
        if (authenticationUserDTo == null || authenticationUserDTo.getDataPermessionList() == null || StringUtils.isEmpty(url)) {
            return null;
        }
        Map<String, String> map = authenticationUserDTo.getDataPermessionList();
        String joinSql = null;
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                if (key != null && antPathMatcher.match(key.toLowerCase(), url.toLowerCase())) {
                    joinSql = map.get(key);
                }
            }
        }
        return joinSql;

    }

    boolean checkTenantId(String origSql) {
        String sql = origSql.toLowerCase();
        //判断删除where条件里是否已有tenant_id字段
        if (sql.contains("where")) {
            String whereSql = sql.split("where", 2)[1];
            if (whereSql.contains("tenant_id")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 定义一个内部辅助类，作用是包装 SQL
     */
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

    }


    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new
                MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }


    /**
     * 字符串修改
     *
     * @param stringBuilder 需要处理的原字符串
     * @param keyword       字符
     * @param before        在字符前加的字符串
     * @param rear          在字符后加的字符串
     * @return
     */
    public String ReplacementInfo(StringBuilder stringBuilder, String keyword, String before, String rear) {
        //字符第一次出现的位置
        int index = stringBuilder.indexOf(keyword);
        while (index != -1) {
            stringBuilder.insert(index, before);
            stringBuilder.insert(index + before.length() + keyword.length(), rear);
            //下一次出现的位置，
            index = stringBuilder.indexOf(keyword, index + before.length() + keyword.length() + rear.length() - 1);
        }
        return stringBuilder.toString();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 获取拼接tenantId查询条件的sql
     */
    public String getTenantQuerySql(String jointSql, String origSql) {
        String tempSql = "";
        //判断原始sql中有没有 order  by
        if (origSql.toLowerCase().contains("order by")) {
            //判断原sql中有没有 where
            if (origSql.toLowerCase().contains("where")) {
                //如果有where条件 就在原始sql最后加上  拼接条件
                jointSql = " " + "and" + " " + jointSql + " ";
            } else {
                jointSql = " " + "where" + " " + jointSql + " ";
            }
            StringBuilder stringBuilder = new StringBuilder(origSql.toLowerCase());
            tempSql = ReplacementInfo(stringBuilder, "order by", jointSql, "");

        } else {
            //判断原sql中有没有 where
            if (origSql.toLowerCase().contains("where")) {
                //如果有where条件 就在原始sql最后加上  拼接条件
                tempSql = origSql + " " + "and" + " " + jointSql;
            } else {
                tempSql = origSql + " " + "where" + " " + jointSql;
            }
        }


        return tempSql;
    }

    public Object excuteSql(Invocation invocation, MappedStatement ms, BoundSql boundSql, String sql) throws Throwable {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
                boundSql.getParameterMappings(), boundSql.getParameterObject());
        MappedStatement newMs = newMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        Object[] queryArgs = invocation.getArgs();
        queryArgs[0] = newMs;
        logger.trace("改写的SQL: {}", sql);

        return invocation.proceed();
    }


}
