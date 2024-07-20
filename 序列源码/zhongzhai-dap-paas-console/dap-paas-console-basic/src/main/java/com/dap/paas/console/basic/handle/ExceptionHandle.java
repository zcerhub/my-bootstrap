package com.dap.paas.console.basic.handle;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.base.sys.api.common.Result;
import com.base.sys.api.common.ResultEnum;
import com.base.sys.api.common.ResultUtils;
import com.dap.paas.console.common.exception.DapWebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * 异常统一处理类
 * @author zpj
 * @date 2021/10/28
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 指定捕获的异常类型
     */
    @ResponseBody
    @ExceptionHandler(value = DapWebServerException.class)
    public Result handle(DapWebServerException e) {
        LOGGER.error("【系统异常】", e);
        Integer code = e.getCode();
        if (code == null) {
            return ResultUtils.error(e.getMessage());
        }
        return ResultUtils.error(code, e.getMessage());
    }

    /**
     * 指定参数校验的异常
     * @param e 异常
     * @return 异常信息
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handle(MethodArgumentNotValidException e) {
        LOGGER.error("【参数校验异常】", e);
        StringBuilder sb = new StringBuilder();
        e.getFieldErrors().forEach(fieldError -> {
            sb.append(fieldError.getField()).append(": ");
            sb.append(fieldError.getDefaultMessage()).append(";");
        });
        return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), sb.toString());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result handleConstraintViolationException(ConstraintViolationException ex) {
        return ResultUtils.error(ResultEnum.INVALID_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler({ServiceException.class})
    @ResponseBody
    public Result serviceExceptionHandler(ServiceException e) {
        LOGGER.error("服务操作异常！原因是：{}", e.getMessage(), e);
        return ResultUtils.error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = DaoException.class)
    @ResponseBody
    public Result daoExceptionHandler(DaoException e) {
        LOGGER.error("操作数据库异常！原因是：{}", e.getMessage(), e);
        return ResultUtils.error(ResultEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 捕获所有异常类型
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        LOGGER.error("【系统异常】", e);
        return ResultUtils.error(ResultEnum.FAILED.getCode(), ResultEnum.FAILED.getMsg());
    }
}