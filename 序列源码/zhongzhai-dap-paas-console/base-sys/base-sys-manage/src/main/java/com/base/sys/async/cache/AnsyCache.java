package com.base.sys.async.cache;

import com.base.sys.async.service.AsyncCacheService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnsyCache implements InitializingBean {


    @Autowired
    private AsyncCacheService asyncCacheService;

    @Override
    public void afterPropertiesSet() throws Exception {
//        asyncCacheService.asyncWriteCache();
//        asyncCacheService.asyncWriteUserCache();
//        asyncCacheService.asyncWriteUserDataRule();
    }
}
