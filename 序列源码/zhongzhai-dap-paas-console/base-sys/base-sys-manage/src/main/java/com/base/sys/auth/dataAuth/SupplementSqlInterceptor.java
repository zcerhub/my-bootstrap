package com.base.sys.auth.dataAuth;


import com.base.api.entity.BaseEntity;
import com.base.sys.api.dto.AuthenticationUserDto;
import com.base.sys.utils.UserUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拦截修改和新增的sql  加上新增人、修改人
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SupplementSqlInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SupplementSqlInterceptor.class);
    private Properties properties;
    /**
     * 创建时间
     */
    private static final String CREATE_TIME = "createDate";
    /**
     * 创建人id
     */
    private static final String CREATER = "createUserId";
    /**
     * 更新时间
     */
    private static final String UPDATE_TIME = "updateDate";

    /**
     * 更新人id
     */
    private static final String UPDATE_BY = "updateUserId";

    /**
     * 租户Id
     */
    private static final String TENANT_ID = "tenantId";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        List<String> sqlFilter  = SqlFilterEnum.toList();
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        String sqlId = ms.getId();

        if (sqlFilter.contains(sqlId)){
            return invocation.proceed();
        }
        if(ms.getSqlCommandType().equals(SqlCommandType.DELETE)){
                return invokeDelete(invocation);
        }else if (ms.getSqlCommandType().equals(SqlCommandType.UPDATE) || ms.getSqlCommandType().equals(SqlCommandType.INSERT) ){
                return invokeUpdate(invocation);
        } else {
            return invocation.proceed();
        }
    }
    //删除操作
    private Object invokeDelete(Invocation invocation) throws Throwable{
        Object target = invocation.getTarget();
        // 获取登录人信息
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        //原始sql
        String origSql = boundSql.getSql();
        //判断删除where条件里是否已有tenant_id字段，若有直接执行
        if (origSql.toLowerCase().contains("where")){
             String whereSql = origSql.toLowerCase().split("where",2)[1];
             if (whereSql.contains("tenant_id")){
                 return invocation.proceed();
             }
        }
        logger.trace("原始SQL: {}", origSql);
        //凭借包含tenantId的sql
        AuthenticationUserDto userDto = UserUtil.getUser();
        if (userDto==null||userDto.getTenantId()==null){
            throw  new NullPointerException("删除操作时，用户信息不存在");
        }
        String tenantId = userDto.getTenantId();
        String joinTenantSql = "tenant_id = "+"'" +tenantId+"'" ;
        String resultSql = getTenantSql(joinTenantSql,origSql);


        return excuteSql(invocation,ms,boundSql,resultSql);
    }

    // 修改操作
    private Object invokeUpdate(Invocation invocation) throws Throwable {
        // 获取第一个参数
        Object[] args1 = invocation.getArgs();
        Object parameterObject = args1[1];

        // 去除null
        List<Object> objList = Arrays.stream(args1).filter(x -> x != null).collect(Collectors.toList());
        MappedStatement ms = null;
        Object args = null;   // 获取参数
        // 赋值
        for (Object obj : objList) {
            if (obj instanceof MappedStatement) {
                ms = (MappedStatement) obj;
            } else if (obj instanceof Object) {
                args = obj;
            }
        }

        if (ms == null || args == null) {
            return invocation.proceed();
        }
        if (!checkArgsValue(args,invocation)){
            // 判断参数类型  是不是MapperMethod.ParamMap 是 就循环更改  不是就是对象直接更改
            if (args instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> mapObj = (MapperMethod.ParamMap<Object>) args;
                for (Map.Entry<String, Object> obj : mapObj.entrySet()) {
                    Object paramObj = mapObj.get(obj.getKey());
                    if (paramObj instanceof List) {
                        this.objList(paramObj, ms);
                    } else {
                        Field[] fields = paramObj.getClass().getDeclaredFields();
                        this.upField(fields, ms, args);
                    }
                }
            } else if (args instanceof DefaultSqlSession.StrictMap) {
                DefaultSqlSession.StrictMap<Object> mapObj = (DefaultSqlSession.StrictMap<Object>) args;
                for (Map.Entry<String, Object> obj : mapObj.entrySet()) {
                    Object paramObj = mapObj.get(obj.getKey());
                    if (paramObj instanceof List) {
                        this.objList(paramObj, ms);
                    } else {
                        Field[] fields =   getAllFields(args);
                        this.upField(fields, ms, args);
                    }
                }
            } else {
                Field[] fields =   getAllFields(args);
                this.upField(fields, ms, args);
            }
        }


        //当操作为更新时 操作条件增加tenantId
        if (ms.getSqlCommandType().equals(SqlCommandType.UPDATE)) {
            Object[] newArgs = invocation.getArgs();
            MappedStatement newMs = (MappedStatement) newArgs[0];
            BoundSql boundSql = newMs.getBoundSql(parameterObject);
            //原始sql
            String origSql = boundSql.getSql();
            if (origSql.toLowerCase().contains("where")){
                String whereSql = origSql.toLowerCase().split("where",2)[1];
                if (whereSql.contains("tenant_id")){
                    return invocation.proceed();
                }
            }
            AuthenticationUserDto userDto = UserUtil.getUser();
            if (userDto==null||userDto.getTenantId()==null){
                throw  new NullPointerException("修改操作时，用户信息不存在");
            }
            String tenantId = userDto.getTenantId();
            String joinTenantSql = "tenant_id = "+"'" +tenantId+"'" ;

            String resultSql = getTenantSql(joinTenantSql,origSql);
            return excuteSql(invocation,ms,boundSql,resultSql);
        }
        return invocation.proceed();
    }

    /**
     * 反射得到对象的属性（包含父类）
     * @param object
     * @return
     */
    public static Field[] getAllFields(Object object){
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null){
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        fieldList.toArray(fields);
        return fields;
    }



    /**
     * 如果是集合就递归
     *
     * @param paramObj List 对象
     * @param ms       MappedStatement
     */
    private void objList(Object paramObj, MappedStatement ms) {
        List<Object> listObj = (ArrayList<Object>) paramObj;
        for (Object obj : listObj) {
            if (obj instanceof List) {
                this.objList(obj, ms);
            }
            Field[] fields =   getAllFields(obj);
            this.upField(fields, ms, obj);
        }
    }

    /**
     * 开始执行修改方法
     *
     * @param fields 反射获取字段列表
     * @param ms     MappedStatement
     * @param args   对象参数
     */
    private void upField(Field[] fields, MappedStatement ms, Object args) {
        // 如果 insert 语句  则添加创建时间创建人 修改时间和修改人
       if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
            this.setAllParams(fields, args, CREATE_TIME, new Date());
            this.setAllParams(fields, args, CREATER, UPDATE_BY);
            this.setAllParams(fields, args, UPDATE_BY, UPDATE_BY);
            this.setAllParams(fields, args, UPDATE_TIME, new Date());
            this.setAllParams(fields, args, TENANT_ID, TENANT_ID);
            // 如果是update语句  则添加修改时间修改人
        } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
            this.setAllParams(fields, args, UPDATE_BY, UPDATE_BY);
            this.setAllParams(fields, args, UPDATE_TIME, new Date());
        }
    }

    /**
     * 根据传递参数放进行修改
     *
     * @param fields   反射存在的参数
     * @param obj      需要改变的对象
     * @param valueKey 变更的字段
     * @param valObj   变更参数类型
     */
    private void setAllParams(Field[] fields, Object obj, String valueKey, Object valObj) {
        BaseEntity baseEntity = new BasicEntity();
        BeanUtils.copyProperties(obj, baseEntity);
        if (org.apache.commons.lang.StringUtils.isNotBlank(baseEntity.getTenantId())) {
            return;
        }
        AuthenticationUserDto authenticationUserDto = UserUtil.getUser();
        AuthenticationUserDto userDto = UserUtil.getUser();
        if (userDto==null||userDto.getTenantId()==null){
            throw  new NullPointerException("新增操作时，用户信息不存在");
        }
        for (int i = 0; i < fields.length; i++) {
            if (valueKey.toLowerCase().equals(fields[i].getName().toLowerCase())) {
                try {
                    if (valObj instanceof Date) {
                        fields[i].setAccessible(true);
                        fields[i].set(obj, new Date());
                        fields[i].setAccessible(false);
                    }
                    if (authenticationUserDto != null && !StringUtils.isEmpty(authenticationUserDto.getUserId())) {
                        if (valObj instanceof String) {
                            if (valueKey.equals(TENANT_ID)){
                                fields[i].setAccessible(true);
                                fields[i].set(obj, authenticationUserDto.getTenantId());
                                fields[i].setAccessible(false);
                            }else {
                                fields[i].setAccessible(true);
                                fields[i].set(obj, authenticationUserDto.getUserId());
                                fields[i].setAccessible(false);
                            }
                        }
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static String getTenantSql(String jointSql,String origSql){
        String tempSql = "";
        //判断原sql中有没有 where
            if (origSql.toLowerCase().contains("where")) {
                //如果有where条件 就在原始sql最后加上  拼接条件
                tempSql = origSql + " " + "and" + " " + jointSql;
            } else {
                tempSql = origSql + " " + "where" + " " + jointSql;
            }
            return tempSql;
    }

    public Object excuteSql(Invocation invocation, MappedStatement ms,BoundSql boundSql,String sql) throws Throwable{
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
     * 检查新增数据中是否已包含用户ID和租户ID
     * @param args
     * @return
     */
    private boolean checkArgsValue(Object args,Invocation invocation){
        BasicEntity baseEntity = new BasicEntity();
        BeanUtils.copyProperties(args, baseEntity);
        if (!StringUtils.isEmpty(baseEntity.getTenantId()) && !StringUtils.isEmpty(baseEntity.getUpdateUserId()) && !StringUtils.isEmpty(baseEntity.getCreateUserId())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 内部类，用于获取插入对象基本信息的value
     */
    class BasicEntity extends BaseEntity{

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
}
