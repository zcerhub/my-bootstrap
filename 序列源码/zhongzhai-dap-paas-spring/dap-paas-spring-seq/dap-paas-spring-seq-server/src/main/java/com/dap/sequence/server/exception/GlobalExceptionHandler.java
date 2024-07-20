package com.dap.sequence.server.exception;

import com.base.api.exception.DaoException;
import com.base.api.exception.ServiceException;
import com.dap.sequence.api.exception.ExceptionEnum;
import com.dap.sequence.api.exception.ResultResponse;
import com.dap.sequence.api.exception.SequenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;

/**
 * @className GlobalExceptionHandler
 * @description 全局异常类
 * @author renle
 * @date 2023/11/24 18:03:45 
 * @version: V23.06
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 处理自定义的业务异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = SequenceException.class)
    @ResponseBody
    public ResultResponse seqExceptionHandler(HttpServletRequest req, SequenceException e) {
        log.error("发生业务异常！原因是：{}", e.getMessage(), e);
        return ResultResponse.error(e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = DaoException.class)
    @ResponseBody
    public ResultResponse daoExceptionHandler(HttpServletRequest req, DaoException e) {
        log.error("操作数据库异常！原因是：{}", e.getMessage(), e);
        return ResultResponse.error(ExceptionEnum.OPERATE_DB_ERROR.getResultCode(), ExceptionEnum.OPERATE_DB_ERROR.getResultMsg());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResultResponse serviceExceptionHandler(HttpServletRequest req, ServiceException e) {
        log.error("服务操作异常！原因是：{}", e.getMessage(), e);
        return ResultResponse.error(ExceptionEnum.OPERATE_SERVICE_ERROR.getResultCode(), ExceptionEnum.OPERATE_SERVICE_ERROR.getResultMsg());
    }

    ///**
    // * 处理自定义的业务异常
    // *
    // * @param req req
    // * @param e e
    // * @return ResultResponse
    // */
    //@ExceptionHandler(value = RestClientException.class)
    //@ResponseBody
    //public ResultResponse RestExceptionHandler(HttpServletRequest req, RestClientException e) {
    //    log.error("请求操作异常！原因是：{}", e.getMessage(), e);
    //    return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR.getResultCode(), e.getMessage());
    //}

    /**
     * 处理空指针的异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResultResponse.error(ExceptionEnum.BODY_NOT_MATCH);
    }

    /**
     * 处理其他异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultResponse exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }
}
