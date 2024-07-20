package com.base.core.init;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;


import java.util.Collection;

public class MybastisConfig extends MapperRegistry {
    public MybastisConfig(Configuration config) {
        super(config);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return super.getMapper(type, sqlSession);
    }

    @Override
    public <T> boolean hasMapper(Class<T> type) {
        return super.hasMapper(type);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        super.addMapper(type);
    }

    @Override
    public Collection<Class<?>> getMappers() {
        return super.getMappers();
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        super.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        super.addMappers(packageName);
    }
}
