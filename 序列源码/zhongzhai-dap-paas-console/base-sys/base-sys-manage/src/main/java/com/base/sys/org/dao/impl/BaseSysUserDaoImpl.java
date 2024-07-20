package com.base.sys.org.dao.impl;

import com.base.api.exception.DaoException;
import com.base.api.util.SqlSessionMapperLabel;
import com.base.core.dao.impl.AbstractBaseDaoImpl;
import com.base.sys.api.entity.BaseSysUser;
import com.base.sys.org.dao.BaseSysUserDao;
import org.springframework.stereotype.Repository;

/**
 * @Author: scalor
 * @Date: 2021/1/15:16:22
 * @Descricption:
 */
@Repository
public class BaseSysUserDaoImpl extends AbstractBaseDaoImpl<BaseSysUser,String> implements BaseSysUserDao {



    private  static  String sqlCode= "getByAccount" ;

    @Override
    public BaseSysUser getByAccount(String account) throws DaoException {
           BaseSysUser result=null;
            try {
                result= this.getSqlSession().selectOne(getSqlNamespace()+"."+ sqlCode, account);
            }catch (Exception e){
                String msg= sqlCode.toString()+":"+sqlCode+e.getMessage();
                throw new DaoException(msg);
            }
            return result;
    }

    @Override
    public Integer selectAccountTotal(String account) throws DaoException{
        Integer result=null;
        try {
            result= this.getSqlSession().selectOne(getSqlNamespace()+".selectAccountTotal", account);
        }catch (Exception e){

            throw new DaoException("selectAccountTotal执行异常:"+e.getMessage());
        }
        return result;
    }

}
