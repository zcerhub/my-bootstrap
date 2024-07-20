package com.base.sys.menu.service.impl;

import com.base.core.service.impl.AbstractBaseServiceImpl;
import com.base.sys.api.entity.BaseSysApp;
import com.base.sys.menu.dao.BaseSysAppDao;
import com.base.sys.menu.service.BaseSysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class BaseSysAppServiceImpl extends AbstractBaseServiceImpl<BaseSysApp, String> implements BaseSysAppService {
    @Autowired
    private BaseSysAppDao baseSysAppDao;
    @Override
    public void watchInstructionBookById(String id, HttpServletResponse response){
        try(OutputStream os = response.getOutputStream()){
            BaseSysApp productManageEntity = baseSysAppDao.getObjectById(id);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline;filename=" + java.net.URLEncoder.encode(productManageEntity.getInstructionBookName(), "UTF-8"));
            os.write(productManageEntity.getProductInstructionBook());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
