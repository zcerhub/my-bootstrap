package com.dap.paas.console.seq.exception;



import com.dap.paas.console.common.exception.ExceptionEnum;
import com.dap.paas.console.common.exception.ResultResponse;
import com.dap.paas.console.common.exception.SequenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @className GlobalExceptionHandler
 * @description 全局异常类
 * @author renle
 * @date 2023/11/24 18:03:45 
 * @version: V23.06
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 处理自定义的业务异常
     *
     * @param req req
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = SequenceException.class)
    @ResponseBody
    public ResultResponse<ExceptionEnum> seqExceptionHandler(HttpServletRequest req, SequenceException e) {
        logger.error("发生业务异常！原因是：",  e);
        return ResultResponse.error(e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param e e
     * @return ResultResponse
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultResponse<String> exceptionHandler(HttpServletRequest req, NullPointerException e) {
        logger.error("发生空指针异常！原因是:", e);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR.getResultCode(), e.getMessage());
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
    public ResultResponse<String> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:", e);
        return ResultResponse.error(ExceptionEnum.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.error("【参数校验异常】", e);
        return ResultResponse.error(ExceptionEnum.METHOD_ARGUMENT_NOT_VALID_EXCEPTION);
    }

}
