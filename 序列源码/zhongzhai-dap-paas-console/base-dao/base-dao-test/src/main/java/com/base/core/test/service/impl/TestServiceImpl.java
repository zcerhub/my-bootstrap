package com.base.core.test.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.core.test.entity.Test;
import com.base.core.test.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends AbstractBaseServiceImpl<Test,String> implements TestService {

}
